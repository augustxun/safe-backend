package com.augustxun.safe;

import com.augustxun.safe.utils.MD5Util;

public class MD5Test {
    public static void main(String[] args) {

        String encode = MD5Util.encode("123456");
        System.out.println(encode);

    }
}
