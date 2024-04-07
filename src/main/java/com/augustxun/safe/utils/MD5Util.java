package com.augustxun.safe.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class MD5Util {
    /**
     * 加密，不可逆
     *
     * @param password 需要加密的秘密
     * @return /
     */
    public static String encode(String password) {
        if (Objects.nonNull(password)) {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                // 生成 8位 的数字的数组，数组中有 16 个数
                byte[] digest = md5.digest(password.getBytes(StandardCharsets.UTF_8));
                // 将 16 个 8 位数组成一个 BigInteger 正整数，并转成 16 进制字符串输出
                return new BigInteger(1, digest).toString(16);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 校验密码是否正确
     *
     * @param password 输入的原始秘密
     * @param digest   存起来的加密后秘闻
     * @return /
     */
    public static boolean validate(String password, String digest) {
        if (Objects.nonNull(password) && Objects.nonNull(digest)) {
            return Objects.equals(encode(password), digest);
        }
        return false;
    }
}
