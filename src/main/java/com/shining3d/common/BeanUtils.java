package com.shining3d.common;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.support.ArgumentConvertingMethodInvoker;
import org.springframework.util.Assert;
import org.springframework.util.MethodInvoker;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

/**
 * 
* @Description: 
* @version: v1.0.0
* @author: tianlg
* @date: Jan 21, 2015 5:12:30 PM
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* Jan 21, 2015      tianlg          v1.0.0
 */
public class BeanUtils extends org.springframework.beans.BeanUtils {
	
	private static Logger logger = LoggerFactory.getLogger(BeanUtils.class);
	
	public static Map<String, String> convertObject2Map(Object target){
		Map<String, String> retMap = Maps.newHashMap();
		
		JSONObject json = JSON.parseObject(JSON.toJSONString(target));
		for(Iterator<String> iter = json.keySet().iterator(); iter.hasNext();){
			String key = iter.next();
			retMap.put(key, json.getString(key));
		}
		return retMap;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public static Object loadObject(Class clazz, Map<String, ?> rowMap) throws Exception{
		Object obj = clazz.newInstance();
		List<FieldParameter> paramList = getFieldParameters(obj, false);
		if(rowMap.isEmpty()){
			return obj;
		}
		StringBuffer methodName = new StringBuffer();
		MethodInvoker invoker = new ArgumentConvertingMethodInvoker();
		invoker.setTargetObject(obj);
		for(int index=0; index < paramList.size(); index ++){
			FieldParameter param = paramList.get(index);
			String fieldName = param.getName();
			String columnName = param.getColumnName();
			if(!rowMap.containsKey(columnName)){
				continue;
			}
			Object value = rowMap.get(columnName);
			if(null == value){
				continue;
			}
			methodName.setLength(0);
			methodName.append("set").append(fieldName.substring(0,1).toUpperCase())
				.append(fieldName.substring(1));
			invoker.setTargetMethod(methodName.toString());
			try {
				invoker.setArguments(new Object[]{value});
				invoker.prepare();
				invoker.invoke();
			} catch (Exception e) {
				logger.error("Error when setting parameter value [" + value + "] for " + clazz.getName()
						+ "[" + fieldName + "]", e);
			}
		}
		return obj;
	}
	
	@SuppressWarnings({ "unchecked"})
	public <T> List<T> loadList(Class<T> clazz, List<Map<String, Object>> dataList) throws Exception{
		List<T> retList = new ArrayList<T>();
		for(int index=0,len=dataList.size(); index<len; index++){
			Map<String, Object> rowMap = dataList.get(index);
			retList.add((T)loadObject(clazz, rowMap));
		}
		return retList;
	}
	
	@SuppressWarnings("rawtypes")
	public static List<FieldParameter> getFieldParameters(Object obj, boolean obtainValue){
		Assert.notNull(obj, "Can't get field values from a null object.");
		List<String> paramNameList = new ArrayList<String>();
		List<FieldParameter> retList = new ArrayList<FieldParameter>();
		Class clazz = obj.getClass();
		List<Field> fields = new ArrayList<Field>();
		getReflectField(clazz, fields);
		
		StringBuffer methodName = new StringBuffer();
		MethodInvoker invoker = new MethodInvoker();
		invoker.setTargetObject(obj);
		for(int index=0,len=fields.size(); index < len; index ++){
			Field field = fields.get(index);
			int mod = field.getModifiers();
			if(!Modifier.isStatic(mod) && (Modifier.isPrivate(mod) || Modifier.isProtected(mod))){
				FieldParameter param = new FieldParameter();
				String fieldName = field.getName();
				param.setName(fieldName);
				param.setColumnName(FieldParameter.convertFieldNameToColumnName(fieldName));
				param.setFiledType(field.getType());
				Object value = null;
				try {
					if(obtainValue){
						methodName.setLength(0);
						methodName.append("get").append(fieldName.substring(0,1).toUpperCase())
							.append(fieldName.substring(1));
						invoker.setTargetMethod(methodName.toString());
						invoker.setArguments(new Object[]{});
						value = invoker.invoke();
					}
				} catch (Exception e) {
					logger.error("Error when fetch parameter [" + fieldName + "] from [" + clazz.getName() + "].");
				} finally{
					param.setValue(value);
					if(!paramNameList.contains(param.getName())){
						paramNameList.add(param.getName());
						retList.add(param);
					}
				}
			}
		}
		return retList;
	}
	
	@SuppressWarnings("rawtypes")
	private static void getReflectField(Class clazz, List<Field> fieldList){
		if(clazz == Object.class){
			return;
		}
		Field[] fields = clazz.getDeclaredFields();
		for(int index=0; index < fields.length; index ++){
			fieldList.add(fields[index]);
		}
		getReflectField(clazz.getSuperclass(), fieldList);
	}
	
	/**
	 * Copy the property values of the given source bean into the given target bean.
	 * <p>Note: The source and target classes do not have to match or even be derived
	 * from each other, as long as the properties match. Any bean properties that the
	 * source bean exposes but the target bean does not will silently be ignored.
	 * @param source the source bean
	 * @param target the target bean
	 * @param ignoreProperties array of property names to ignore
	 * @throws BeansException if the copying failed
	 * @see BeanWrapper
	 */
	public static void copyProperties(Object source, Object target, String[] ignoreProperties)
			throws BeansException {

		Assert.notNull(source, "Source must not be null");
		Assert.notNull(target, "Target must not be null");

		Class<?> actualEditable = target.getClass();
		PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
		List<String> ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties) : null;
		
		for (PropertyDescriptor targetPd : targetPds) {
			if (targetPd.getWriteMethod() != null &&
					(ignoreProperties == null || (!ignoreList.contains(targetPd.getName())))) {
				PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
				if (sourcePd != null && sourcePd.getReadMethod() != null) {
					try {
						Method readMethod = sourcePd.getReadMethod();
						if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
							readMethod.setAccessible(true);
						}
						Object value = readMethod.invoke(source);
						Method writeMethod = targetPd.getWriteMethod();
						if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
							writeMethod.setAccessible(true);
						}
						writeMethod.invoke(target, value);
					}
					catch (Throwable ex) {
						throw new FatalBeanException("Could not copy properties from source to target", ex);
					}
				}
			}
		}
	}
}
