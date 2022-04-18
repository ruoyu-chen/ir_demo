package cn.edu.bistu.cs.ir.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chenruoyu
 */
public class StringUtil {

    private static final Logger log = LoggerFactory.getLogger(StringUtil.class);


    /**
     * 判断字符串是否为空（包括null或trim后为""的字符串）
     *
     * @param str 待判断的字符串
     * @return 若str为null或者trim后为""，返回true，否则返回false
     */
    public static boolean isEmpty(String str) {
        return str == null || "".equalsIgnoreCase(str.trim());
    }

}