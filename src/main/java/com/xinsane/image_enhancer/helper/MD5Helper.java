package com.xinsane.image_enhancer.helper;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Helper {
    public static String md5(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            String result = new BigInteger(1, md.digest()).toString(16);
            StringBuilder builder = new StringBuilder(32);
            for (int i = 0; i < 32 - result.length(); ++i)
                builder.append('0'); // MD5值补足位数
            builder.append(result);
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
