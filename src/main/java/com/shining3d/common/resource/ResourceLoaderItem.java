package com.shining3d.common.resource;


public abstract class ResourceLoaderItem {
	private String name;
	private String objClassName;
	private String keyName;
	private String parent;
	private String groups;// 逗号隔开
	private String cronText;// 定时刷新规则
	private String comments;
	private String dataSourceBean;

	private String applicationName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getObjClassName() {
		return objClassName;
	}

	public void setObjClassName(String objClassName) {
		this.objClassName = objClassName;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getGroups() {
		return groups;
	}

	public void setGroups(String groups) {
		this.groups = groups;
	}

	public String getCronText() {
		return cronText;
	}

	public void setCronText(String cronText) {
		this.cronText = cronText;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getDataSourceBean() {
		return dataSourceBean;
	}

	public void setDataSourceBean(String dataSourceBean) {
		this.dataSourceBean = dataSourceBean;
	}
}
