package com.shining3d.common.resource;

import java.util.List;
import java.util.Set;

public interface IResourceLoader {

	static final String GROUP_SPLIT_STRING = ",";
	
	void setLoaderArgs(Object args);
	
	Set<String> getResKeySet();
	
	List<?> getGroupItems(String groupName,String groupValue);
	
	List<String> getGroupNameValues(String groupName);
	
	Object getItemByKey(String key);
	
	int getSize();
	
	boolean load();
	
	void setResourceLoaderItem(ResourceLoaderItem item);
	
	ResourceLoaderItem getResourceLoaderItem();
	
	void writeItem(String key, Object value);
	
	void deleteItem(String key);
	
}
