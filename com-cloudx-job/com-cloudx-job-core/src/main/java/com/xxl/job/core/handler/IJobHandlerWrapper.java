package com.xxl.job.core.handler;

import com.cloudx.common.entity.constant.AppConstant;
import com.cloudx.common.tools.UUIDUtil;
import org.slf4j.MDC;

/**
 * job handler wrapper
 * @author heycm
 * @version 1.0
 * @since 2025/6/7 22:25
 */
public class IJobHandlerWrapper extends IJobHandler {

    private final IJobHandler jobHandler;

    public IJobHandlerWrapper(IJobHandler jobHandler) {
        this.jobHandler = jobHandler;
    }

    @Override
    public void execute() throws Exception {
        MDC.put(AppConstant.TRACE_ID, UUIDUtil.getId());
        try {
            jobHandler.execute();
        } finally {
            MDC.remove(AppConstant.TRACE_ID);
        }
    }

    @Override
    public void init() throws Exception {
        jobHandler.init();
    }

    @Override
    public void destroy() throws Exception {
        jobHandler.destroy();
    }
}
