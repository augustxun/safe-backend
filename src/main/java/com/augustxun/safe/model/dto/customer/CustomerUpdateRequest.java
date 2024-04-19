package com.augustxun.safe.model.dto.customer;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 更新请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class CustomerUpdateRequest implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;


    private String lastname;

    private String firstname;

    private String zipcode;

    private String unit;

    private String street;

    private String city;

    private String state;
}