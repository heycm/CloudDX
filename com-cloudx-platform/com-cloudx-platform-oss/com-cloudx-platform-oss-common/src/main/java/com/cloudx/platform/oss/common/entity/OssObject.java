package com.cloudx.platform.oss.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import lombok.Data;

/**
 * 存储对象
 * @author heycm
 * @version 1.0
 * @since 2025/4/16 22:21
 */
@Data
public class OssObject implements Closeable {

    /**
     * Object name
     */
    private String name;

    /**
     * Object's bucket name
     */
    private String bucketName;

    /**
     * Object's metadata.
     */
    private ObjectMetadata metadata = new ObjectMetadata();

    /**
     * Object's content
     */
    @JsonIgnore
    private InputStream objectContent;

    @Override
    public void close() throws IOException {
        if (objectContent != null) {
            objectContent.close();
        }
    }

    @Override
    public String toString() {
        return "OssObject [name=" + getName() + ", bucket=" + (bucketName == null ? "<Unknown>" : bucketName) + ", metadata=" + metadata + "]";
    }
}
