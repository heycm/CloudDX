package cn.heycm.common.web.call;

import cn.heycm.common.tools.Jackson;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

/**
 * 远程/方法调用包装，打印参数 & 耗时
 * @author heycm
 * @version 1.0
 * @since 2025/3/24 22:26
 */
@Slf4j
public class CallWrapper {

    /**
     * 远程/方法调用包装，打印出入参、耗时、异常堆栈
     * @param point    调用点
     * @param function 远程请求/调用方法
     * @param args     请求参数/调用参数
     * @return 调用方法返回值
     */
    public static <T, R> R call(String point, Function<T, R> function, T args) {
        StopWatch stopWatch = new StopWatch();
        R response = null;
        try {
            stopWatch.start();
            response = function.apply(args);
            stopWatch.stop();
        } catch (Throwable e) {
            log.error("CallWrapper Error: {}", e.getMessage(), e);
            throw e;
        } finally {
            if (log.isInfoEnabled()) {
                log.info("[调用点]: {} [耗时]: {}ms [参数]: {} [返回]: {}", point, stopWatch.getTotalTimeMillis(), Jackson.toJson(args),
                        Jackson.toJson(response));
            }
        }
        return response;
    }

    /**
     * 远程/方法调用包装，打印出入参、耗时、异常堆栈
     * @param point    调用点
     * @param function 远程请求/调用方法
     * @return 调用方法返回值
     */
    public static <T, R> R call(String point, Function<T, R> function) {
        return call(point, function, null);
    }

    /**
     * 远程/方法调用包装，打印出入参、耗时、异常堆栈
     * @param function 远程请求/调用方法
     * @param argument 调用参数
     * @return 调用方法返回值
     */
    public static <R> R call(Function<CallArgument, R> function, CallArgument argument) {
        return call(argument.name(), function, argument);
    }
}
