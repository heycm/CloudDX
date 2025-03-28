package cn.heycm.platform.mybatis.plugins;

import cn.heycm.common.entity.constant.AppConstant;
import cn.heycm.platform.mybatis.plugins.context.SQLMarkingContext;
import java.lang.reflect.Field;
import java.sql.Connection;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.MDC;
import org.springframework.util.ReflectionUtils;

/**
 * SQL染色插件
 * @author heycm
 * @version 1.0
 * @since 2025/3/28 22:57
 */
@Slf4j
@Intercepts({
        /**
         * 在预编译之前拦截：方法签名 {@link StatementHandler#prepare(Connection, Integer)}
         */
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class SQLMarkingInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        RoutingStatementHandler routingStatementHandler = (RoutingStatementHandler) this.getProxyTarget(statementHandler);

        Field delegateField = ReflectionUtils.findField(RoutingStatementHandler.class, "delegate");
        ReflectionUtils.makeAccessible(delegateField);
        BaseStatementHandler delegate = (BaseStatementHandler) ReflectionUtils.getField(delegateField, routingStatementHandler);

        Field mappedStatementField = ReflectionUtils.findField(BaseStatementHandler.class, "mappedStatement");
        ReflectionUtils.makeAccessible(mappedStatementField);
        MappedStatement mappedStatement = (MappedStatement) ReflectionUtils.getField(mappedStatementField, delegate);

        // 染色
        this.marking(delegate.getBoundSql(), mappedStatement);

        if (log.isDebugEnabled()) {
            log.debug("[SQLMarking] {}", delegate.getBoundSql().getSql());
        }

        return invocation.proceed();
    }

    private Object getProxyTarget(Object target) {
        MetaObject metaObject = SystemMetaObject.forObject(target);
        while (metaObject.hasGetter("h")) {
            target = metaObject.getValue("h.target");
            metaObject = SystemMetaObject.forObject(target);
        }
        return target;
    }

    /**
     * 染色：增加 Mapper 接口方法签名、TraceId、用户ID、租户ID，以及拓展上下文数据
     */
    private void marking(BoundSql boundSql, MappedStatement mappedStatement) {

        // 原始SQL：SELECT * FROM user WHERE id = ?
        // 染色SQL：SELECT * FROM user WHERE id = ?
        //         /* [SQLMarking] statementId: cn.heycm.user.infrastructure.db.mapper.UserMapper.selectById, tenantId: 10000, traceId: 123xxx, uId: 10000001 */

        // 原始SQL
        String sql = boundSql.getSql().trim();
        // Mapper 接口方法签名
        String statementId = mappedStatement.getId();

        // 染色代码片段
        StringBuilder joinCode = new StringBuilder();

        // 设置方法签名
        joinCode.append("\n/* [SQLMarking] statementId: ").append(statementId);
        // 租户ID
        if (!SQLMarkingContext.get().containsKey(AppConstant.TENANT_ID) && MDC.get(AppConstant.TENANT_ID) != null) {
            joinCode.append(", tenantId: ").append(MDC.get(AppConstant.TENANT_ID));
        }
        // 链路ID
        if (!SQLMarkingContext.get().containsKey(AppConstant.TRACE_ID) && MDC.get(AppConstant.TRACE_ID) != null) {
            joinCode.append(", traceId: ").append(MDC.get(AppConstant.TRACE_ID));
        }
        // 用户ID
        if (!SQLMarkingContext.get().containsKey(AppConstant.UID) && MDC.get(AppConstant.UID) != null) {
            joinCode.append(", uId: ").append(MDC.get(AppConstant.UID));
        }
        // 其他业务扩展上下文数据
        SQLMarkingContext.get().forEach((k, v) -> {
            joinCode.append(", ").append(k).append(": ").append(v);
        });
        joinCode.append(" */");

        // 拼接染色，重新设置SQL
        sql = sql + joinCode;
        Field sqlField = ReflectionUtils.findField(BoundSql.class, "sql");
        ReflectionUtils.makeAccessible(sqlField);
        ReflectionUtils.setField(sqlField, boundSql, sql);
    }
}
