package com.augustxun.safe.model.vo;

import cn.hutool.json.JSONUtil;
import com.augustxun.safe.model.entity.Account;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AccountVO {

    @TableId(type = IdType.AUTO)
    private Integer acctNo;

    private String acctName;

    private String zipcode;

    private String unit;

    private String street;

    private String city;

    private String state;

    private Date dateOpened;

    private String type;

    private Long userId;

    private BigDecimal balance;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    private static final long serialVersionUID = 1L;

    /**
     * 对象转包装类
     *
     * @param account
     * @return
     */
    public static AccountVO objToVo(Account account) {
        if (account == null) {
            return null;
        }
        AccountVO accountVO = new AccountVO();
        BeanUtils.copyProperties(account, accountVO);
        return accountVO;
    }
}
