package com.shining3d.common;

/**
 * 用于将JavaBean转换成数据库模型的辅助类
* @Description: 
* @version: v1.0.0
* @author: tianlg
* @date: Jan 21, 2015 5:12:42 PM
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* Jan 21, 2015      tianlg          v1.0.0
 */
@SuppressWarnings("rawtypes")
public class FieldParameter{
	private String name;
	private String columnName;
	private Class filedType;
	private Object value;
	public Class getFiledType() {
		return filedType;
	}
	public void setFiledType(Class filedType) {
		this.filedType = filedType;
	}
	public String getName() {
		return name; 
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	public static String convertFieldNameToColumnName(String fieldName){
		StringBuffer buffer = new StringBuffer();
		for(int index=0; index<fieldName.length(); index ++){
			char ch = fieldName.charAt(index);
			if(Character.isUpperCase(ch)){
				buffer.append("_").append(Character.toLowerCase(ch));
			}else{
				buffer.append(ch);
			}
		}
		return buffer.toString();
	}
	
	public static String convertColumnNameToFieldName(String columnName){
		StringBuffer buffer = new StringBuffer();
		String text = columnName.toLowerCase();
		
		boolean bUpper = false;
		for(int index=0; index< text.length(); index ++){
			char ch = text.charAt(index);
			if('_' == ch){
				bUpper = true;
			}else{
				buffer.append(bUpper?Character.toUpperCase(ch):ch);
				bUpper = false;
			}
		}
		return buffer.toString();
	}
}
