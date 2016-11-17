package com.shining3d.common;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

/**
 * Util Class for Jdbc Operation.
 * @author Coraldane coraldane@gmail.com
 * @version 1.0
 * @date Sep 26, 2011 5:18:36 PM
 */
public class JdbcUtils {
	
	public static final String DB_TYPE_ORACLE = "Oracle";
	public static final String DB_TYPE_DB2 = "DB2";
	public static final String DB_TYPE_MYSQL = "MySQL";
	
	/**
	 * 获取ResultSet中的数据
	 * @param rs
	 * @param index
	 * @return
	 * @throws SQLException
	 * @author coraldane
	 */
	public static Object getResultSetValue(ResultSet rs, int index)
			throws SQLException {
		Object obj = rs.getObject(index);
		if (obj instanceof Blob) {
			obj = rs.getBytes(index);
		} else if (obj instanceof Clob) {
			obj = rs.getString(index);
		} else if ((obj != null) && (obj.getClass().getName().startsWith("oracle.sql.TIMESTAMP"))) {
			obj = rs.getTimestamp(index);
		} else if ((obj != null) && (obj.getClass().getName().startsWith("oracle.sql.DATE"))) {
			String metaDataClassName = rs.getMetaData().getColumnClassName(index);
			if (("java.sql.Timestamp".equals(metaDataClassName)) || ("oracle.sql.TIMESTAMP".equals(metaDataClassName))) {
				obj = rs.getTimestamp(index);
			} else{
				obj = rs.getDate(index);
			}
		} else if ((obj != null) && (obj instanceof Date)) {
			String metaDataClassName = rs.getMetaData().getColumnClassName(index);
			if("java.sql.Timestamp".equals(metaDataClassName)){
				obj = rs.getTimestamp(index);
			}
		}

		return obj;
	}
	
	/**
	 * 根据记录集元数据获得查询结果的列名或别名
	 * @param resultSetMetaData
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 * @author coraldane
	 */
	public static String lookupColumnName(ResultSetMetaData resultSetMetaData, int columnIndex) throws SQLException {
		String name = resultSetMetaData.getColumnLabel(columnIndex);
		if ((name == null) || (name.length() < 1)){
			name = resultSetMetaData.getColumnName(columnIndex);
		}
		return name;
	}
	
	/**
	 * 根据DataSource获得数据库类型
	 * @param dataSource
	 * @return Oracle/DB2/MySQL
	 * @author coraldane
	 */
	public static String getDatabaseType(DataSource dataSource){
		Connection conn = null;
		String retValue = null;
		try {
			conn = dataSource.getConnection();
			retValue = getDatabaseType(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(null != conn){
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return retValue;
	}
	
	/**
	 * 根据Connection获得数据库类型
	 * @param connection
	 * @return Oracle/DB2/MySQL
	 * @author coraldane
	 */
	public static String getDatabaseType(Connection connection) {
		try {
			String productName = connection.getMetaData().getDatabaseProductName();
			if(productName.contains("DB2")){
				return "DB2";
			}else{
				return productName;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取Connection中的数据库用户名
	 * @param connection
	 * @return
	 * @author coraldane
	 */
	public static String getUserName(Connection connection) {
		try {
			return connection.getMetaData().getUserName();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void closeStatement(Statement stmt){
		try {
			if(null != stmt){
				stmt.close();
				stmt = null;
			}
		} catch (SQLException e) {
			
		}
	}
	
	public static void closeResultSet(ResultSet resultSet){
		try {
			if(null != resultSet){
				resultSet.close();
				resultSet = null;
			}
		} catch (SQLException e) {
			
		}
	}
	
}