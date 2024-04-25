package com.augustxun.safe.model.vo;

import com.augustxun.safe.model.entity.Customer;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.springframework.beans.BeanUtils;

@Data
public class CustomerVO {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String lastName;

    private String firstName;

    private String zipcode;

    private String unit;

    private String street;

    private String city;

    private String state;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public static CustomerVO objToVo(Customer customer) {
        if (customer == null) {
            return null;
        }
        CustomerVO customerVO = new CustomerVO();
        BeanUtils.copyProperties(customer, customerVO);
        return customerVO;
    }
}
