package com.augustxun.safe.model.dto.account;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class AccountQueryRequest implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String lastname;

    private String firstname;

    private String street;

    private String city;

    private String state;

    private Long userId;
}