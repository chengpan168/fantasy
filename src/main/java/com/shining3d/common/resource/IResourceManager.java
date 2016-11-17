package com.shining3d.common.resource;

import java.util.List;
import java.util.Set;

public interface IResourceManager {

	public static final String RESOURCE_LOADER_ITEMS = "RESOURCE_LOADER_ITEMS";

	void init() throws Exception;
	
	void destroy() throws Exception;
	
	Set<String> getResNameSet();

	/**
	 * 刷新指定名的resName
	 * @param resName
	 * @return 
	 */
	boolean refresh(String resName) throws Exception;

	/**
	 * 返回资源名为resName的资源列表.
	 * @param resName
	 * @return
	 */
	Set<String> getResKeySet(String resName);

	/**
	 * 返回资源名为resName, 多个key值对应的资源对象
	 * @param resName
	 * @param keys
	 * @return
	 */
	Object[] getResItems(java.lang.String resName, java.lang.String[] keys);

	/**
	 * 返回资源名为resName，group_name字段可能的所有取值
	 * @param resName
	 * @param groupName
	 * @return
	 */
	List<String> getResGroupKeyList(String resName, String groupName);

	/**
	 * 返回资源名为resName, 分组名为groupName,分组值为groupValue的资源列表.
	 * @param resName
	 * @param groupName
	 * @param groupValue
	 * @return
	 */
	List<?> getResListByGroup(String resName, String groupName,
			String groupValue);

	/**
	 * 返回树结构资源，节点rootKey(包括)下的所有子节点
	 * @param resName
	 * @param groupName 存放父节点值的字段名
	 * @param rootKey
	 * @return
	 */
	List<?> getTreeNodes(String resName, String groupName, String rootKey);

	/**
	 * 取得一个资源对象.
	 * @param resName
	 * @param key
	 * @return
	 */
	Object getResItem(String resName, String key);
	
	/**
	 * 将一个对象写到缓存
	 * @param resName
	 * @param key
	 * @param value
	 */
	void writeResItem(String resName, String key, Object value);
	
	/**
	 * 从缓存中删除一个对象，不适用于分组
	 * @param resName
	 * @param key
	 */
	void deleteResItem(String resName, String key);

}
