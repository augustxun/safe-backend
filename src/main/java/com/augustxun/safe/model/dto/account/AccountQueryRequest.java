package com.augustxun.safe.model.dto.account;

import com.augustxun.safe.common.PageRequest;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class AccountQueryRequest extends PageRequest implements Serializable {

    private String acctName;

    private String city;

    private String state;

    private Date dateOpened;

    private String type;

    private Long userId;
}