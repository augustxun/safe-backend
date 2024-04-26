package com.augustxun.safe.model.vo;

import com.augustxun.safe.model.entity.Account;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SavingsAccountVO {
    private static final long serialVersionUID = 1L;
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
    // Checking 独有属性
    private BigDecimal balance;
    private BigDecimal interestRate;
    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 对象转包装类
     *
     * @param account
     * @return
     */
    public static SavingsAccountVO objToVo(Account account) {
        if (account == null) {
            return null;
        }
        SavingsAccountVO accountVO = new SavingsAccountVO();
        BeanUtils.copyProperties(account, accountVO);
        return accountVO;
    }
}
