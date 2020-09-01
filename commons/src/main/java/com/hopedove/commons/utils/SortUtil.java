package com.hopedove.commons.utils;

import org.springframework.util.StringUtils;

/**
 * 排序字段处理
 */
public class SortUtil {

    public final static String format(String str) {
        String sort = "";//-create_date,+update_date
        if (StringUtils.isEmpty(str)) {
            return sort;
        }
        String[] strs = str.split(",");
        for (String str_ : strs) {
            if (str_.contains("-")) {
                sort += str_.substring(1) + " DESC,";
            } else if (str_.contains("+")) {
                sort += str_.substring(1) + " ASC,";
            } else {
                //默认按顺序处理
                sort += str_.substring(1) + " ASC,";
            }
        }

        return sort.substring(0, sort.length() - 1);
    }

    public static void main(String[] args) {
        System.out.println(format("-create_date,+update_date"));
    }
}
