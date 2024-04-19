package com.augustxun.safe.model.dto.customer;

import java.io.Serializable;
import java.util.List;

import com.augustxun.safe.common.PageRequest;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class CustomerQueryRequest extends PageRequest implements Serializable {

    private String lastName;
    private String firstName;
    private String street;
    private String city;
    private String state;
}