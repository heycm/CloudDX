package cn.heycm.platform.mybatis.batch;

import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;

/**
 * 批处理
 * @author heycm
 * @version 1.0
 * @since 2025/3/27 22:04
 */
public class MyBatisBatchHelper {

    private static final int DEFAULT_BATCH_SIZE = 1000;

    private static volatile SqlSessionFactory sqlSessionFactory;

    private MyBatisBatchHelper() {
    }

    public static void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        if (MyBatisBatchHelper.sqlSessionFactory == null) {
            MyBatisBatchHelper.sqlSessionFactory = sqlSessionFactory;
        }
    }

    /**
     * 批量执行，并返回影响行数
     * @param list        数据集合
     * @param mapperClass Mapper类
     * @param consumer    执行方法
     */
    public <E, M> int batch(Collection<E> list, Class<M> mapperClass, BiConsumer<E, M> consumer) {
        SqlSession session = null;
        int i = 0;
        int size = list.size();
        int rows = 0;
        try {
            session = sqlSessionFactory.openSession(ExecutorType.BATCH);
            M mapper = session.getMapper(mapperClass);
            for (E e : list) {
                consumer.accept(e, mapper);
                i++;
                if (i == size || i % DEFAULT_BATCH_SIZE == 0) {
                    List<BatchResult> results = session.flushStatements();
                    rows += MyBatisBatchHelper.getRows(results);
                }
            }
            // 非事务环境下强制commit，事务情况下该commit会跟随事务提交
            session.commit(!TransactionSynchronizationManager.isSynchronizationActive());
        } catch (Exception e) {
            if (session != null) {
                session.rollback();
            }
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return rows;
    }

    private static int getRows(List<BatchResult> results) {
        int rows = 0;
        if (CollectionUtils.isEmpty(results)) {
            return rows;
        }
        for (BatchResult result : results) {
            if (result.getUpdateCounts() != null) {
                for (int updateCount : result.getUpdateCounts()) {
                    rows += updateCount;
                }
            }
        }
        return rows;
    }
}
