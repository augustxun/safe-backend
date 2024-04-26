package com.augustxun.safe.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除请求
 *

 */
@Data
public class DeleteRequest implements Serializable {
    /**
     * id
     */
    private String id;

    private static final long serialVersionUID = 1L;
}
