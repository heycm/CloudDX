package com.cloudx.platform.mybatis.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

/**
 * 流查询结果集批处理
 * @author heycm
 * @version 1.0
 * @since 2025/3/27 22:19
 */
public abstract class StreamBatchHandler<T> implements ResultHandler<T> {

    /**
     * 批次大小
     */
    private final int batchSize;

    /**
     * 批次数据
     */
    private final List<T> batchData;

    /**
     * 总读取行数
     */
    private int rows;

    public StreamBatchHandler(int batchSize) {
        this.batchSize = batchSize;
        this.batchData = new ArrayList<>(batchSize);
    }

    /**
     * 开始流读取，查询DB
     * @param streamReading Mapper读取流方法
     */
    public void openStreamRead(Consumer<StreamBatchHandler<T>> streamReading) {
        streamReading.accept(this);
        this.flush();
    }

    /**
     * 读取流数据
     * @param resultContext 数据行
     */
    @Override
    public void handleResult(ResultContext<? extends T> resultContext) {
        rows++;
        batchData.add(resultContext.getResultObject());
        if (rows % batchSize == 0 || resultContext.isStopped()) {
            this.flush();
        }
    }

    /**
     * 批次处理
     */
    private void flush() {
        if (batchData.isEmpty()) {
            return;
        }
        this.batchHandler(batchData);
    }

    /**
     * 批次处理自定义业务逻辑
     * @param batchData 批次数据
     */
    protected abstract void batchHandler(List<T> batchData);
}
