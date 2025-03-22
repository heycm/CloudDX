package cn.heycm.common.tools.lambda;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

/**
 * Lambda 工具
 * @author hey
 * @version 1.0
 * @since 2025/2/19 15:20
 */
public class LambdaUtil {

    /**
     * Lambda表达式通过方法引用获取属性名称，User::getUserId -> userId
     * @param lambda 方法引用
     * @return 属性名称
     */
    public static <T> String getFieldName(LambdaMethodRef<T, ?> lambda) {
        try {
            Method method = lambda.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(lambda);
            String methodName = serializedLambda.getImplMethodName();
            if (methodName.startsWith("get") || methodName.startsWith("set")) {
                return resolveFieldName(methodName.substring(3));
            } else if (methodName.startsWith("is")) {
                return resolveFieldName(methodName.substring(2));
            }
            throw new IllegalArgumentException("Invalid getter method: " + methodName);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to parse lambda", e);
        }
    }

    private static String resolveFieldName(String methodSuffix) {
        if (methodSuffix.isEmpty()) {
            return "";
        }
        char firstChar = methodSuffix.charAt(0);
        return Character.toLowerCase(firstChar) + methodSuffix.substring(1);
    }
}
