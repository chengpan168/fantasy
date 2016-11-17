package com.shining3d.common.resource;

import javax.sql.DataSource;

/**
 * 资源加载条目
 * @author jianli.guojl
 *
 */
public class DBResourceLoaderItem extends ResourceLoaderItem{
	public static final int DEFUAULT_LOAD_PAGE_SIZE = 5000;
	
	private String loadSql;
	
	private int pageSize = DEFUAULT_LOAD_PAGE_SIZE;
	
	private DataSource dataSource;
	
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	public String getLoadSql() {
		return loadSql;
	}
	public void setLoadSql(String loadSql) {
		this.loadSql = loadSql;
	}
}
