package com.shining3d.common.resource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.shining3d.common.SpringContextUtil;


/**
 * 
* @Description: 
* @version: v1.0.0
* @author: guojl
* @date: Oct 29, 2014 10:24:31 PM
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* Oct 29, 2014     guojl          v1.0.0
 */
public class ResourceManagerDBImpl implements IResourceManager {
	
	public static final String DEFAULT_SCHEDULER_GROUP_NAME = "resourceManager";
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String loaderImpl;
	public void setLoaderImpl(String loaderImpl) {
		this.loaderImpl = loaderImpl;
	}
	
	private Object loaderArgs;
	public void setLoaderArgs(Object args){
		this.loaderArgs = args;
	}
	
	private String initSql;
	public void setInitSql(String initSql) {
		this.initSql = initSql;
	}
	
	private int  pageSize;
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	private String cronText;
	public void setCronText(String cronText) {
		this.cronText = cronText;
	}
	
	private String applicationName;
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	
	private String appDataSourceBean;
	public void setAppDataSourceBean(String appDataSourceBean) {
		this.appDataSourceBean = appDataSourceBean;
	}

	private Map<String, IResourceLoader> loaders = new HashMap<String, IResourceLoader>();
	private Map<String, Method> groupMethodsMap = new HashMap<String, Method>();

	@Override
	public void init() throws Exception {
		Assert.notNull(appDataSourceBean, "appDataSourceBean not empty");
		Assert.notNull(initSql);
		if(pageSize <= 0){
			pageSize = DBResourceLoaderItem.DEFUAULT_LOAD_PAGE_SIZE;
		}
		
		DBResourceLoaderItem resourceInfo = new DBResourceLoaderItem();
		resourceInfo.setName(RESOURCE_LOADER_ITEMS);
		resourceInfo.setKeyName("name");
		resourceInfo.setDataSourceBean(appDataSourceBean);
		resourceInfo.setApplicationName(applicationName);
		resourceInfo.setLoadSql(initSql);
		resourceInfo.setDataSource((DataSource) SpringContextUtil.getBean(resourceInfo.getDataSourceBean()));
		resourceInfo.setPageSize(pageSize);
		resourceInfo.setObjClassName(DBResourceLoaderItem.class.getName());
		resourceInfo.setCronText(StringUtils.isEmpty(this.cronText)?"0 0 0 * * ?":this.cronText);
		IResourceLoader resourceLoader = doReload(resourceInfo);
		if(null != resourceLoader){
			//加载全部的资源项s_loader_resource
			Set<String> loaderItemNameSet = resourceLoader.getResKeySet();
			for(String loaderItemName: loaderItemNameSet){
				DBResourceLoaderItem item = (DBResourceLoaderItem)resourceLoader.getItemByKey(loaderItemName);

				if (item.getDataSourceBean().equals("minasDataSource")) continue;

				item.setApplicationName(applicationName);
				item.setDataSource((DataSource) SpringContextUtil.getBean(item.getDataSourceBean()));
				if(null == doReload(item)){
					throw new RuntimeException(item.getName()+" load failed!");
				}
			}
		}
	}
	
	
	private IResourceLoader doReload(DBResourceLoaderItem item) throws Exception {
		IResourceLoader resourceLoader;
		if(null == item || StringUtils.isEmpty(item.getName())){
			return null;
		}
		if(loaders.containsKey(item.getName())){
			resourceLoader = loaders.get(item.getName());
		}else{
			try {
				resourceLoader = (IResourceLoader) Class.forName(loaderImpl).newInstance();
				resourceLoader.setLoaderArgs(this.loaderArgs);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("loaderImpl is invalid,please set full package path");
			}
		}
		resourceLoader.setResourceLoaderItem(item);
		resourceLoader.load();
		loaders.put(item.getName(), resourceLoader);
		
		String jobName = "RESITEM_" + item.getName();
		
//		JobKey jobKey = new JobKey(jobName, DEFAULT_SCHEDULER_GROUP_NAME);
//		if(!this.scheduler.checkExists(jobKey)){
//			JobDetail jobDetail = JobBuilder.newJob(ResourceManagerRunningJob.class)
//					.usingJobData("itemName", item.getName())
//					.withIdentity(jobName, DEFAULT_SCHEDULER_GROUP_NAME).build();
//
//			// Trigger the job to run now, and then repeat every 40 seconds
//			CronTrigger trigger = TriggerBuilder.newTrigger()
//		        .withIdentity(jobName, DEFAULT_SCHEDULER_GROUP_NAME)
//		        .withSchedule(CronScheduleBuilder.cronSchedule(item.getCronText()))
//		        .build();
//			scheduler.scheduleJob(jobDetail, trigger);
//		}
		return resourceLoader;
	}

	@Override
	public void destroy() throws Exception{
//		this.scheduler.shutdown(false);
	}

	@Override
	public boolean refresh(String resName) throws Exception {
		if(StringUtils.isEmpty(resName)){
			throw new RuntimeException("resName can't be empty.");
		}
		logger.info("Start to refresh resourceManager for key {}.", resName);
		//对新增加的缓存项，先初始化resourceLoader
		if (!this.loaders.containsKey(resName)) {
			DBResourceLoaderItem loadItem = (DBResourceLoaderItem) (loaders.get(RESOURCE_LOADER_ITEMS)).getResourceLoaderItem();
			loadItem.setDataSource((DataSource) SpringContextUtil.getBean(loadItem.getDataSourceBean()));
			loadItem.setApplicationName(this.applicationName);
			if(null != doReload(loadItem)){
				IResourceLoader resourceLoader = this.loaders.get(RESOURCE_LOADER_ITEMS);
				if(null != resourceLoader){
					Set<String> loaderItemNameSet = resourceLoader.getResKeySet();
					for(String loaderItemName: loaderItemNameSet){
						DBResourceLoaderItem item = (DBResourceLoaderItem)resourceLoader.getItemByKey(loaderItemName);
						if(resName.equals(item.getName())){//加载新增的资源项
							item.setDataSource((DataSource) SpringContextUtil.getBean(item.getDataSourceBean()));
							item.setApplicationName(applicationName);
							if(null == doReload(item)){
								throw new RuntimeException(item.getName()+" load failed!");
							}
						}
					}
				}
			}
		}
		if (loaders.containsKey(resName)) {
			IResourceLoader loader = loaders.get(resName);
			DBResourceLoaderItem clotInfoDo = (DBResourceLoaderItem)loader.getResourceLoaderItem();
	        if (null != clotInfoDo) {
	        	clotInfoDo.setDataSource((DataSource) SpringContextUtil.getBean(clotInfoDo.getDataSourceBean()));
	        	clotInfoDo.setApplicationName(applicationName);
	            if (null != doReload(clotInfoDo)) {
	               return true;
	            }
	        } else {
	            if (loaders.containsKey(resName)) {
	            	loaders.remove(resName);
	            }
	        }
		} else {
		    throw new RuntimeException("refresh failed! "+ resName +" not init");
		}
		return false;
	}
	
	@Override
	public Set<String> getResNameSet(){
		return this.loaders.keySet();
	}

	@Override
	public Set<String> getResKeySet(String resName) {
		if (loaders.containsKey(resName)) {
			return loaders.get(resName).getResKeySet();
	    }
		return null;
	}

	@Override
	public Object[] getResItems(String resName, String[] keys) {
		if (keys != null && keys.length > 0) {
	            Object[] objs = new Object[keys.length];
	            for (int i = 0; i < keys.length; i++) {
	                objs[i] = getResItem(resName, keys[i]);
	            }
	            return objs;
	    }
		return null;
	}

	@Override
	public List<String> getResGroupKeyList(String resName, String groupName) {
		if (loaders.containsKey(resName)) {
            return loaders.get(resName).getGroupNameValues(groupName);
        }
        return new ArrayList<String>(0);
	}

	@Override
	public List<?> getResListByGroup(String resName, String groupName,
			String groupValue) {
		if (loaders.containsKey(resName)) {
	        return loaders.get(resName).getGroupItems(groupName, groupValue);
	    }
		return null;
	}

	@Override
	public List<?> getTreeNodes(String resName, String groupName, String rootKey) {
		List<Object> result = new ArrayList<Object>();
        if (loaders.containsKey(resName)) {
            List<?> children = loaders.get(resName).getGroupItems(groupName, rootKey);
            if (children != null && children.size() > 0) {
                for (Object item : children) {
                    result.add(item);
                    Method method = getKeyMethod(item.getClass(), resName);
                    try {
                        Object rtn = method.invoke(item, (Object[]) null);
                        String rootValue = rtn == null ? (String) null : String.valueOf(rtn);
                        result.addAll(getTreeNodes(resName, groupName, rootValue));
                    } catch (IllegalArgumentException e) {
                    } catch (IllegalAccessException e) {
                    } catch (InvocationTargetException e) {
                    }
                }
            }
        }
        return result;
	}
	
	
	private Method getKeyMethod(Class<?> clazz, String resName) {
        String key = resName;
        if (groupMethodsMap.containsKey(key)) {
            return groupMethodsMap.get(key);
        } else {
            Method method;
            String dbField = ((IResourceLoader) loaders.get(resName)).getResourceLoaderItem().getKeyName();
            try {
                method = clazz.getMethod(dbFieldName2PropName(dbField), (Class[]) null);
                if (method != null) {
                    groupMethodsMap.put(key, method);
                    return method;
                }
            } catch (SecurityException e) {
            } catch (NoSuchMethodException e) {
            }
        }
        return null;
    }
	
	
	 private String dbFieldName2PropName(String dbFieldName) {
	        StringBuffer buff = new StringBuffer(dbFieldName.toLowerCase());
	        StringBuffer tb = new StringBuffer();
	        tb.append("get");
	        boolean toUpperCase = true;
	        for (int i = 0; i < dbFieldName.length(); i++) {
	            char aChar = buff.charAt(i);
	            if ('_' == aChar) {
	                toUpperCase = true;
	            } else {
	                tb.append(toUpperCase ? java.lang.Character.toUpperCase(aChar) : aChar);
	                toUpperCase = false;
	            }
	        }
	        return tb.toString();
	}

	@Override
	public Object getResItem(String resName, String key) {
		if (loaders.containsKey(resName)) {
            return loaders.get(resName).getItemByKey(key);
        }
		return null;
	}
	
	@Override
	public void writeResItem(String resName, String key, Object value) {
		if(!loaders.containsKey(resName)){
			try {
				IResourceLoader resourceLoader = (IResourceLoader)Class.forName(loaderImpl).newInstance();
				ResourceLoaderItem resourceLoaderItem = new DBResourceLoaderItem();
				resourceLoaderItem.setName(resName);
				resourceLoader.setResourceLoaderItem(resourceLoaderItem);
				resourceLoader.setLoaderArgs(this.loaderArgs);
				resourceLoader.writeItem(key, value);
				loaders.put(resName, resourceLoader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (loaders.containsKey(resName)) {
            loaders.get(resName).writeItem(key, value);
        }
	}
	
	@Override
	public void deleteResItem(String resName, String key){
		if (loaders.containsKey(resName)) {
            loaders.get(resName).deleteItem(key);
        }
	}
}
