package com.shining3d.common.resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import redis.clients.jedis.ShardedJedis;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.shining3d.common.BeanUtils;
import com.shining3d.common.JedisService;

/**
 * 从MySQL加载到Redis
* @Description: 
* @version: v1.0.0
* @author: tianlg
* @date: Oct 29, 2014 9:48:53 PM
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* Oct 29, 2014     tianlg          v1.0.0
 */
public class RedisDBResourceLoaderImpl<T extends DBResourceLoaderItem> extends MysqlDBResourceLoaderImpl<T> {

	private JedisService jedisService;
	
	public RedisDBResourceLoaderImpl(){
		
	}
	
	@Override
	public void setLoaderArgs(Object args){
		this.jedisService = (JedisService)args;
	}
	
	private String getResourceKeyName(){
		return this.getResourceLoaderItem().getApplicationName() + "_" + this.getResourceLoaderItem().getName();
	}
	
	@Override
	public Set<String> getResKeySet() {
		ShardedJedis jedis = this.jedisService.getJedis();
		try {
			return jedis.hkeys(this.getResourceKeyName());
		} catch (Exception e) {
			logger.error("hgetAll error for key:{}.", this.getResourceKeyName(), e);
		} finally{
			this.jedisService.returnRes(jedis);
		}
		return null;
	}

	@Override
	public List<?> getGroupItems(String groupName, String groupValue) {
		List<Object> retList = Lists.newArrayList();
		ShardedJedis jedis = this.jedisService.getJedis();
		String key = this.getResourceKeyName() + "_" + groupName;
		try {
			String text = jedis.hget(key, groupValue);
			if(StringUtils.isNotEmpty(text)){
				List<ObjectContainer> containerList = JSON.parseArray(text, ObjectContainer.class);
				for(ObjectContainer container: containerList){
					retList.add(container.getTarget());
				}
			}
		} catch (Exception e) {
			logger.error("hset error for key, {}.", key, e);
		} finally{
			this.jedisService.returnRes(jedis);
		}
		return retList;
	}

	@Override
	public List<String> getGroupNameValues(String groupName) {
		List<String> retList = new ArrayList<String>();
		ShardedJedis jedis = this.jedisService.getJedis();
		String key = this.getResourceKeyName() + "_" + groupName;
		try {
			Map<String, String> textMap = jedis.hgetAll(key);
			if(null != textMap){
				for(Iterator<String> iter = textMap.keySet().iterator(); iter.hasNext();){
					retList.add(iter.next());
				}
			}
		} catch (Exception e) {
			logger.error("hget error for key, {}.", key, e);
		} finally{
			this.jedisService.returnRes(jedis);
		}
		return retList;
	}

	@Override
	public Object getItemByKey(String key) {
		ShardedJedis jedis = this.jedisService.getJedis();
		try {
			String text = jedis.hget(this.getResourceKeyName(), key);
			if(StringUtils.isNotEmpty(text)){
				ObjectContainer container = JSON.parseObject(text, ObjectContainer.class);
				if(null != container){
					return container.getTarget();
				}
			}
		} catch (Exception e) {
			logger.error("hget error for key:{}.", key, e);
		} finally{
			this.jedisService.returnRes(jedis);
		}
		return null;
	}
	
	@Override
	public void writeItem(String key, Object value){
		ShardedJedis jedis = this.jedisService.getJedis();
		try {
			ObjectContainer container = new ObjectContainer();
			container.setObjectClass(value.getClass().getName());
			container.setTarget(value);
			jedis.hset(this.getResourceKeyName(), key, JSON.toJSONString(container));
		} catch (Exception e) {
			logger.error("hset error for key:" + key, e);
		} finally{
			this.jedisService.returnRes(jedis);
		}
	}
	
	@Override
	public void deleteItem(String key){
		ShardedJedis jedis = this.jedisService.getJedis();
		try {
			jedis.hdel(this.getResourceKeyName(), key);
		} catch (Exception e) {
			logger.error("hset error for key:"+key+".", e);
		} finally{
			this.jedisService.returnRes(jedis);
		}
	}
	
	@Override
	public int getSize() {
		ShardedJedis jedis = this.jedisService.getJedis();
		try {
			return jedis.hlen(this.getResourceKeyName()).intValue();
		} catch (Exception e) {
			logger.error("hgetAll error for key:{}.", this.getResourceKeyName(), e);
		} finally{
			this.jedisService.returnRes(jedis);
		}
		return 0;
	}
	
	protected void deleteResourceCache(){
		ShardedJedis jedis = this.jedisService.getJedis();
		try {
			jedis.del(this.getResourceKeyName());
			for(String groupName: this.groupNames){
				jedis.del(this.getResourceKeyName() + "_" + groupName);
			}
		} catch (Exception e) {
			logger.error("del error for key:{}.", this.getResourceKeyName(), e);
			e.printStackTrace();
		} finally{
			this.jedisService.returnRes(jedis);
		}
	}
	
	@Override
	public boolean load(){
		//TODO don't clean items before ResourceManager load into redis
		this.deleteResourceCache();
		return super.load();
	}
	
	@Override
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
					this.writeItem(String.valueOf(rowMap.get(keyName)), item);
					
					if (CollectionUtils.isNotEmpty(groupNames)) {
						try {
							// 将新item加入组
							for (int j = 0; j < groupNames.size(); j++) {
								groupName = groupNames.get(j);
								if(StringUtils.isEmpty(groupName)){
									continue;
								}
								Object indexObj = rowMap.get(groupName);
								if (indexObj != null) {
									// 如果索引字段为空，不索引该记录
									String groupValue = indexObj.toString();
									this.lputData(groupName, groupValue, item);
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
	
	private void lputData(String groupName, String groupValue, Object item){
		ShardedJedis jedis = this.jedisService.getJedis();
		String key = this.getResourceKeyName() + "_" + groupName;
		try {
			ObjectContainer container = new ObjectContainer();
			container.setObjectClass(item.getClass().getName());
			container.setTarget(item);
			
			List<ObjectContainer> dataList = Lists.newArrayList();
			String text = jedis.hget(key, groupValue);
			if(StringUtils.isNotEmpty(text)){
				List<ObjectContainer> containerList = JSON.parseArray(text, ObjectContainer.class);
				dataList.addAll(containerList);
			}
			dataList.add(container);
			jedis.hset(key, groupValue, JSON.toJSONString(dataList));
		} catch (Exception e) {
			logger.error("hset error for key "+key+",groupValue:"+groupValue+".", e);
		} finally{
			this.jedisService.returnRes(jedis);
		}
	}
	
}

class ObjectContainer implements Serializable{
	private static final long serialVersionUID = -5281850666786432739L;
	private String objectClass;
	private Object target;
	public String getObjectClass() {
		return objectClass;
	}
	public void setObjectClass(String objectClass) {
		this.objectClass = objectClass;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object getTarget() throws Exception{
		Class clazz = Class.forName(this.getObjectClass());
		return JSON.parseObject(JSON.toJSONString(this.target), clazz);
	}
	public void setTarget(Object target) {
		this.target = target;
	}
}