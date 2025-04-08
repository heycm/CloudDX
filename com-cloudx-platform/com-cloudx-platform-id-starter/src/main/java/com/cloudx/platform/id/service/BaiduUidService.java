package com.cloudx.platform.id.service;

import com.baidu.fsg.uid.UidGenerator;

/**
 * 百度UID生成服务
 */
public class BaiduUidService implements IdService {

    private final UidGenerator uidGenerator;

    public BaiduUidService(UidGenerator uidGenerator) {
        this.uidGenerator = uidGenerator;
    }

    @Override
    public long nextId() {
        return uidGenerator.getUID();
    }
}
