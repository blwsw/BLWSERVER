package com.hopedove.commons.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class StringUtil {

	public static Map<String, Object> stringToMapO(String str){
		Map<String, Object> map = new HashMap<String, Object>();
		if(org.apache.commons.lang.StringUtils.isNotEmpty(str)){
			str = str.replaceAll("\"","").trim();
		}else{
			return map;
		}
		String array[] = str.substring(str.indexOf("{") + 1, str.indexOf("}")).split(",");
		for (String i : array) {
			String []keyv = i.split(":");
			//有值的加入
			if(keyv.length >1){
				map.put(i.split(":")[0],(Object)i.substring(i.indexOf(":")+1).trim());
			}
		}
		return map;
	}
	
	public static Map<String, String> stringToMap(String str){
		Map<String, String> map = new HashMap<String, String>();
		if(StringUtils.isNotEmpty(str)){
			str = str.replaceAll("\"","").trim();
		}else{
			return map;
		}
		String array[] = str.substring(str.indexOf("{") + 1, str.indexOf("}")).split(",");
		for (String i : array) {
			String []keyv = i.split(":");
			//有值的加入
			if(keyv.length >1){
				map.put(i.split(":")[0],i.substring(i.indexOf(":")+1).trim());
			}
		}
		return map;
	}
}
