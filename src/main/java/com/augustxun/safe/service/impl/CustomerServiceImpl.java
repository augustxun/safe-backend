package com.augustxun.safe.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.augustxun.safe.common.R;
import com.augustxun.safe.constant.RedisConstant;
import com.augustxun.safe.mapper.CustomerMapper;
import com.augustxun.safe.model.dto.LoginFormDTO;
import com.augustxun.safe.model.entity.Customer;
import com.augustxun.safe.service.CustomerService;
import com.augustxun.safe.utils.RegexUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

import static com.augustxun.safe.constant.RedisConstant.LOGIN_CODE_KEY;
import static com.augustxun.safe.constant.RedisConstant.LOGIN_CODE_TTL;

/**
 * @author augustxun
 * @description 针对表【customer】的数据库操作Service实现
 * @createDate 2024-04-05 17:14:01
 */
@Service
@Slf4j
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public R<String> sendCode(String phone, HttpSession session) {
        // 1.校验手机号
        if (RegexUtils.isPhoneInvalid(phone)) {
            // 2.如果不符合，返回错误信息
            return R.error("手机号格式错误！");
        }
        // 3. 符合，生成验证码
        String code = RandomUtil.randomNumbers(6);
        // 4.保存验证码到Redis, 并设置有效期
        stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY + phone, code, LOGIN_CODE_TTL, TimeUnit.MINUTES);
        // 5.发送验证码
        log.debug("发送短信验证码成功，验证码:{}", code);
        // 返回OK
        return R.success("成功");
    }

    @Override
    public R login(LoginFormDTO loginFormDTO, HttpSession session) {
        String phone = loginFormDTO.getPhone();
        // 1. 校验手机号格式
        if (!RegexUtils.isPhoneInvalid(phone)) {
            return R.error("wrong phone number");
        }
        // 2.查询用户是否存在
        Customer customer = query().eq("phone", phone).one();
        if (customer==null) {
            return R.error("user not exist");
        }
        // 2. 如果传输的是验证码，比对验证码
        String code = loginFormDTO.getCode();
        if (StrUtil.isNotBlank(code)) {
            String key = LOGIN_CODE_KEY + phone;
            String cacheCode = stringRedisTemplate.opsForValue().get(key);
            if (!cacheCode.equals(loginFormDTO.getCode())) {
                return R.error("verification code error");
            }

            return R.success("login successfully");
        }


        String pwd = customer.getPassword();
        if (!pwd.equals(loginFormDTO.getPassword())) {
            return R.error("wrong password");
        }
        return  R.success("登录成功");

    }


}




