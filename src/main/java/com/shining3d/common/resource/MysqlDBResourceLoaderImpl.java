package com.shining3d.common.resource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.shining3d.common.BeanUtils;
import com.shining3d.common.JdbcUtils;

/**
 * 从MySQL加载到内存
* @Description: 
* @version: v1.0.0
* @author: guojl
* @date: Oct 29, 2014 9:48:53 PM
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* Oct 29, 2014     guojl          v1.0.0
 */
public class MysqlDBResourceLoaderImpl<T extends DBResourceLoaderItem> implements IResourceLoader {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	protected T dbResourceLoaderItem;
	
	private Map<String,Map<String,List<Object>>> groupDatas = new ConcurrentHashMap<String,Map<String,List<Object>>>();
	
	protected List<String> groupNames = new ArrayList<String>();
	
	private static List<String> TIME_STAMP_CLASS = new ArrayList<String>();
	
	private Map<String, Object> dataMap = Maps.newConcurrentMap();
	
	static{
		TIME_STAMP_CLASS.add("java.util.Date");
		TIME_STAMP_CLASS.add("java.sql.Date");
		TIME_STAMP_CLASS.add("java.sql.Time");
		TIME_STAMP_CLASS.add("java.sql.TimeStamp");
	}
	
	public MysqlDBResourceLoaderImpl(){
		
	}
	
	@Override
	public void setLoaderArgs(Object args){
		
	}
	
	@Override
	public Set<String> getResKeySet() {
		return this.dataMap.keySet();
	}

	@Override
	public List<?> getGroupItems(String groupName, String groupValue) {
		if (groupDatas.containsKey(groupName)) {
			Map<String, List<Object>> group = groupDatas.get(groupName);
			if (group.containsKey(groupValue)) {
				return group.get(groupValue);
			}
		}
		return new ArrayList<Object>(0);
	}

	@Override
	public List<String> getGroupNameValues(String groupName) {
		List<String> result = new ArrayList<String>();
		if (groupDatas.containsKey(groupName)) {
			for (String key : ((Map<String, List<Object>>) groupDatas.get(groupName)).keySet()) {
				result.add(key);
			}
		}
		return result;
	}

	@Override
	public Object getItemByKey(String key) {
		return dataMap.get(key);
	}
	
	@Override
	public void writeItem(String key, Object value){
		this.dataMap.put(key, value);
	}
	
	@Override
	public void deleteItem(String key){
		this.dataMap.remove(key);
	}

	@Override
	public int getSize() {
		return this.dataMap.size();
	}
	
	@Override
	public boolean load(){
		this.dataMap.clear();
		this.groupDatas.clear();
		int start = 0;
		boolean result = true;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet resultSet = null;
		try{
			conn = dbResourceLoaderItem.getDataSource().getConnection();
			boolean isContinue = true;
			boolean isFirst = true;
			while(isContinue){
				pst = conn.prepareStatement(getPageSql());
				pst.setInt(1, start);
				pst.setInt(2, dbResourceLoaderItem.getPageSize());
				resultSet = pst.executeQuery();
				if(isFirst){
					isFirst = false;
				}
				List<Map<String, ?>> list = this.queryForList(resultSet);
				boolean[] fetchResult = fetchFromResultSet(list);
				isContinue = fetchResult[0];
				result = !fetchResult[1];
				start += dbResourceLoaderItem.getPageSize();
			}
			
		}catch(Exception e){
			logger.error("load resource cache ["+this.dbResourceLoaderItem.getName()+"] error, loader sql is ["+this.dbResourceLoaderItem.getLoadSql()+"].",  e);
			result = false;
		}finally {
			try {
				if(null != resultSet){
					resultSet.close();
					resultSet = null;
				}
				if(null != pst){
					pst.close();
					pst = null;
				}
				if(null != conn){
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				logger.error("", e);
			}
		}
		return result;
	}
	
	public List<Map<String, ?>> queryForList(ResultSet resultSet) throws Exception{
		List<Map<String, ?>> retList = Lists.newArrayList();
		ResultSetMetaData rsmd = resultSet.getMetaData();
		int columnCount = rsmd.getColumnCount();
		List<String> columnNameList = new ArrayList<String>();
		for (int i = 1; i <= columnCount; ++i) {
			columnNameList.add(JdbcUtils.lookupColumnName(rsmd, i));
		}
		
		while (resultSet.next()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int index = 1; index <= columnCount; ++index) {
				map.put(columnNameList.get(index - 1), JdbcUtils.getResultSetValue(resultSet, index));
			}
			retList.add(map);
		}
		return retList;
	}

	protected boolean[] fetchFromResultSet(List<Map<String, ?>> list) {
		boolean bContinue = false;
		boolean hasError = false;
		String groupName = null;
		
		if (CollectionUtils.isNotEmpty(list)) {
			try {
				String keyName = dbResourceLoaderItem.getKeyName();
				for (Map<String, ?> rowMap: list) {
					bContinue = true;
					Object item = BeanUtils.loadObject(Class.forName(dbResourceLoaderItem.getObjClassName()), rowMap);
					dataMap.put(String.valueOf(rowMap.get(keyName)), item);
					if (CollectionUtils.isNotEmpty(groupNames)) {
						try {
							// 将新item加入组
							for (int j = 0; j < groupNames.size(); j++) {
								groupName = groupNames.get(j);
								if(StringUtils.isEmpty(groupName)){
									continue;
								}
								Map<String, List<Object>> group; // 组
								if (groupDatas.containsKey(groupName)) {
									group = groupDatas.get(groupName);
								} else {
									group = new HashMap<String, List<Object>>();
									groupDatas.put(groupName, group);
								}
								Object indexObj = rowMap.get(groupName);
								if (indexObj != null) {
									// 如果索引字段为空，不索引该记录
									String groupValue = indexObj.toString();
									List<Object> family; // 相同值的集合
									if (group.containsKey(groupValue)) {
										family = group.get(groupValue);
									} else {
										family = new ArrayList<Object>();
										group.put(groupValue, family);
									}
									family.add(item);
								}
							}
						} catch (Exception e) {
							throw e;
						}
					}
				}
			} catch (Exception e) {
				logger.error("Error:", e);
				hasError = true;
				bContinue = false;
			}
		}
		return new boolean[] { bContinue, hasError };
	}
	
	protected String getPageSql(){
		String sql = dbResourceLoaderItem.getLoadSql();
		return "select * from (" +sql +") a limit ?,?";
	}


	@SuppressWarnings("unchecked")
	@Override
	public void setResourceLoaderItem(ResourceLoaderItem item) {
		this.dbResourceLoaderItem = (T)item;
		if(dbResourceLoaderItem.getGroups()!=null){
			String[] groupNameArray = dbResourceLoaderItem.getGroups().split(GROUP_SPLIT_STRING);
			for(String groupName:groupNameArray){
				if(!groupNames.contains(groupName)){
					groupNames.add(groupName);
				}
			}
		}
		
		if(StringUtils.isNotEmpty(dbResourceLoaderItem.getParent())){
			String parent = dbResourceLoaderItem.getParent();
			if(!groupNames.contains(parent)){
				groupNames.add(parent);
			}
		}
	}


	@Override
	public ResourceLoaderItem getResourceLoaderItem() {
		return this.dbResourceLoaderItem;
	}
}
