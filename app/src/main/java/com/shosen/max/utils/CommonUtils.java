package com.shosen.max.utils;

import android.text.TextUtils;
import android.util.Log;

import com.bin.david.form.data.form.IForm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

public class CommonUtils {

    public static String getFormatPrice(double price, boolean isPoint) {
        DecimalFormat df = null;
        if (isPoint) {
            df = new DecimalFormat("0.00");
            return df.format(price) + "积分";
        } else {
            df = new DecimalFormat("0.00");
            return "¥ " + df.format(price);
        }
    }

    public static String getFormatPrice(String price, boolean isPoint) {

//        if (index != -1) {
//            String after = price.substring(index, price.length());
//            if (after.length() >= 3) {
//                if (isPoint) {
//                    return price.substring(0, index + 2) + "积分";
//                } else {
//                    return "¥ " + price;
//                }
//            }
//        }
//        Log.d("format", price);
        if (!RegexUtils.isMatch("^\\d+(\\.\\d+)?$", price)) {
            //不是非负浮点数
            return price;
        }
        DecimalFormat df = null;
        if (isPoint) {
            df = new DecimalFormat("0.00");
            return df.format(Double.valueOf(price)) + "积分";
        } else {
            df = new DecimalFormat("0.00");
            return "¥ " + df.format(Double.valueOf(price));
        }
    }

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
