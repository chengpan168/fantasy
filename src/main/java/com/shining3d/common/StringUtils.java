package com.shining3d.common;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;

import com.google.common.io.Files;


/**
 * 
 * @Date: Sep 20, 2014 9:41:35 AM<br>
 * @Copyright (c) 2014 shining3d.com <br>
 *            *
 * @since 1.0
 * @author renyg
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

	public static final String CHARSET_UTF8 = "UTF-8";

	public static boolean isEmpty(Long value) {
		return null == value || 0 == value;
	}

	/**
	 * 判断一个字符串是否为整型数字
	 * 
	 * @param value
	 * @return
	 * @author coraldane
	 */
	public static boolean isInteger(String value) {
		if (StringUtils.isEmpty(value)) {
			return false;
		}
		Pattern pattern = Pattern.compile("[+|-]?[0-9]+");
		return pattern.matcher(value).matches();
	}
	
	/**
	 * 判断是否为手机号
	 * @param value
	 * @return
	 */
	public static boolean isMobile(String value){
		if (StringUtils.isEmpty(value)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^0?1(([3578][0-9]{1})|(59)){1}[0-9]{8}$");
		return pattern.matcher(value).matches();
	}

	/**
	 * Trim leading and trailing whitespace from the given String.
	 * 
	 * @param str
	 *            the String to check
	 * @return the trimmed String
	 * @see java.lang.Character#isWhitespace
	 */
	public static String trimWhitespace(String str) {
		if (null == str || "".equals(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		while (buf.length() > 0 && Character.isWhitespace(buf.charAt(0))) {
			buf.deleteCharAt(0);
		}
		while (buf.length() > 0 && Character.isWhitespace(buf.charAt(buf.length() - 1))) {
			buf.deleteCharAt(buf.length() - 1);
		}
		return buf.toString();
	}

	/**
	 * 判断字符串是否符合邮件地址的格式
	 * 
	 * @param value
	 * @return
	 * @author tianlg
	 */
	public static boolean isEmail(String value) {
		String[] array;
		Pattern pattern = Pattern.compile("[\\w[.-]]+");
		if (value.indexOf("@") < 0) {
			return false;
		}

		if (value.contains("..")) {
			return false;
		}

		array = value.split("@");
		if (array.length != 2) {
			return false;
		}

		for (String name : array) {
			name = array[0];
			if (!pattern.matcher(name).matches()) {
				return false;
			}
			if (name.indexOf(".") == 0 || name.endsWith(".")) {
				return false;
			}
		}

		String name = array[1];
		if (!name.contains(".")) {
			return false;
		}

		int suffixLength = name.length() - name.lastIndexOf(".");
		if (suffixLength != 4 && suffixLength != 3) {
			return false;
		}
		return true;
	}

	/**
	 * 判断是否为数字型字符串
	 * 
	 * @param value
	 * @return
	 * @author tianlg
	 */
	public static boolean isNumeric(String value) {
		Pattern pattern = Pattern.compile("[+|-]?[0-9]+(.[0-9]+)?");
		return pattern.matcher(value).matches();
	}

	/**
	 * 将对象toString().欢迎扩展
	 * 
	 * @param o
	 * @return
	 * @date 2011-11-2 下午12:23:43
	 * @author jiaxiao
	 */
	public static String toString(Object o) {
		return toString(o, ",");
	}

	public static String toString(Object o, String spliter) {
		if (null == o) {
			return "";
		} else {
			if (o instanceof Integer || o instanceof Double || o instanceof Boolean || o instanceof Float
					|| o instanceof Long || o instanceof Short || o instanceof StringBuffer
					|| o instanceof StringBuilder) {
				return o.toString();
			} else if (o.getClass().isArray()) {
				StringBuffer buffer = new StringBuffer();
				int len = Array.getLength(o);
				for (int index = 0; index < len; index++) {
					buffer.append(Array.get(o, index));
					if (index < len - 1) {
						buffer.append(spliter);
					}
				}
				return buffer.toString();
			} else if (o instanceof Collection) {
				StringBuffer buffer = new StringBuffer();
				int len = CollectionUtils.size(o);
				for (int index = 0; index < len; index++) {
					buffer.append(CollectionUtils.get(o, index));
					if (index < len - 1) {
						buffer.append(spliter);
					}
				}
				return buffer.toString();
			} else {
				return o.toString();
			}
		}
	}

	/**
	 * 百分比格式化
	 * 
	 * @param number
	 * @return
	 */
	public static String formatPercent(double number) {
		NumberFormat percentFormat = NumberFormat.getPercentInstance();
		percentFormat.setMaximumFractionDigits(2);
		return percentFormat.format(number);
	}

	public static String getUUID() {
		String uuid = UUID.randomUUID().toString();
		return uuid.replaceAll("-", "");
	}

	public static boolean isLongValid(Long id) {
		if (id == null || id == 0) {
			return false;
		}
		return true;
	}

	public static boolean contains(String[] array, String dest) {
		for (String strValue : array) {
			if (strValue.equals(dest)) {
				return true;
			}
		}
		return false;
	}

	public static List<Long> split4LongList(String str) {
		return split4LongList(str, ",");
	}

	public static List<Long> split4LongList(String str, String spliter) {
		if (isEmail(str)) {
			return Collections.emptyList();
		}
		String[] ss = str.split(spliter);
		List<Long> list = new ArrayList<Long>();
		for (String s : ss) {
			list.add(Long.parseLong(s));
		}
		return list;
	}

	public static boolean isAllChinese(String str) {
		char start='\u4e00';
		char end = '\u9fa5';
		char[] arr = str.toCharArray();
		for(int i=0;i<arr.length;i++){
			if(arr[i] < start || arr[i]> end){
				return false;
			}
		}
		return true;
	}
	
//	public static String getFileExtension(String fileName){
//		if(StringUtils.isNotEmpty(fileName)){
//			String extention = Files.getFileExtension(fileName.toLowerCase());
//			return StringUtils.isEmpty(extention)?"doc":extention;
//		}
//		return "";
//	}
	
	public  static String readableFileSize(long size) {
	    if(size <= 0) return "0";
	    final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
	    int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
	    return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}
	
	public static String getFileExtension(String fileName){
		if(StringUtils.isNotEmpty(fileName)){
			String extention = Files.getFileExtension(fileName.toLowerCase());
			return StringUtils.isEmpty(extention)?"doc":extention;
		}
		return "";
	}
	
	
	public static int calPartCount(long fileLength,long partSize) {
	        int partCount = (int) (fileLength / partSize);
	        if (fileLength % partSize != 0){
	            partCount++;
	        }
	        return partCount;
	}
	
	public static boolean isValidPhone(String number,String siteCode){
		if(StringUtils.isNotEmpty(number)){
			String theCode = "";
			number =  number.trim();
			if(!"en".equals(siteCode)){
				if(number.length() == 11 && isInteger(number)){
					return true;
				}else{
					return false;
				}
			}
			//shift + from number
			if (number.indexOf("+")!=-1) {
				number = number.substring(1);
			}
			//check if there has ( or ) and ( behind than )
			if (number.indexOf(')') - number.indexOf('(') > 0) {
				theCode = number.substring(number.indexOf(')'), number.indexOf('(') + 1);
			} else {
				if (number.indexOf(' ') <= 0) return false;
				theCode = number.split(" ")[0];
			}
			return isValidPhone(theCode, siteCode);
		}
		return false;
	}
	
	public static String transTel(String tel,String siteCode){
		if(StringUtils.isNotEmpty(tel)){
			tel = tel.toString().trim();
			if(!"en".equals(siteCode)){
				return tel;
			}
			if (tel.indexOf("+")!=-1) {
			    return tel;
			} else if (tel.indexOf('@') > 0) {
			    String[] arr = tel.split("@");
			    return '+' + StringUtils.join(arr, " ");
			} else if (tel.indexOf(')') - tel.indexOf('(') > 1 && tel.indexOf(')') > 0 && tel.indexOf('(') >= 0) {
			    return '+' + tel.substring(tel.indexOf('(') + 1, tel.indexOf(')')) + ' ' + tel.substring(tel.indexOf(')') + 1, tel.length());
			}else if(tel.indexOf('-') > 0){
			    return '+' + tel.substring(0,tel.indexOf('-')) + ' ' + tel.substring(tel.indexOf('-')+1,tel.length());
			}else if(tel.indexOf(' ')>0){
			    return '+' + tel;
			}else {
			    //其他情况，返回原字符串
			    return tel;
			}
			
		}
		return "";
	}
	
}
