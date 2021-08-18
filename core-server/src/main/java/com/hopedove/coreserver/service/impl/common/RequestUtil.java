package com.hopedove.coreserver.service.impl.common;


import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class RequestUtil {

    public static Map<String, Object> orderSendBefore(Map<String, Object> map) {
        Map<String, Object> treeMap = new TreeMap<String, Object>() {
            private static final long serialVersionUID = 1L;
            public String toString() {
                Iterator<Entry<String, Object>> iterator = this.entrySet().iterator();
                StringBuffer sb = new StringBuffer();
                while (iterator.hasNext()) {
                    Entry<String, Object> entry = iterator.next();
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    sb.append(key + '=' + value + '&');
                }
                return sb.substring(0, sb.length() - 1).toString();
            }
        };
        treeMap.putAll(map);
        return treeMap;
    }

    public static Map<String, Object> orderForMap(Map<String, Object> map) {
        Map<String, Object> treeMap = new TreeMap<String, Object>() {
            private static final long serialVersionUID = 1L;
            public String toString() {
                Iterator<Entry<String, Object>> iterator = this.entrySet().iterator();
                StringBuffer sb = new StringBuffer();
                while (iterator.hasNext()) {
                    Entry<String, Object> entry = iterator.next();
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    sb.append(key + '=' + value + '&');
                }
                return sb.substring(0, sb.length() - 1).toString();
            }
        };
        treeMap.putAll(map);
        return treeMap;
    }
    public static Map<String, Object> stringToMap(String str){
        Map<String, Object> map = new HashMap<String, Object>();
        if(StringUtils.isNotEmpty(str)){
          //  str = str.replaceAll("\"","").trim();
        }else{
            return map;
        }
        String array[] = str.substring(str.indexOf("{") + 1, str.indexOf("}")).split("\",\"");
        for (String i : array) {
            i=i.replaceAll("\"","").trim();
            String []keyv = i.split(":");
            //有值的加入
            if(keyv.length >1){
                map.put(i.split(":")[0],(Object)i.substring(i.indexOf(":")+1).trim());
            }
        }
        return map;
    }
    public static void main(String[] args){
//    	String a = Base64.decodeToString("MDFlNmE2ZDYyZTk0NGI5ZWVhYzRlNmZlYjIyMjVhZDk=");
//    	System.out.println(a);
//    	String b = Base64.encodeString("01e6a6d62e944b9eeac4e6feb2225ad9");
//    	System.out.println(b);
    }
}
