package cn.heycm.common.tools.lambda;

import java.io.Serializable;

/**
 * Lambda表达式通过方法引用获取属性名称
 * <p>必须继承 Serializable 接口，才可以生成 writeReplace() 返回 SerializedLambda 对象（该对象记录Lambda上下文信息）</p>
 * @author hey
 * @version 1.0
 * @since 2025/2/19 15:15
 */
@FunctionalInterface
public interface LambdaMethodRef<T, R> extends Serializable {

    R apply(T t);

}
