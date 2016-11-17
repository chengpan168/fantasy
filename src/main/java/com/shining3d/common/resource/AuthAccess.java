package com.shining3d.common.resource;

import java.sql.Timestamp;

import com.shining3d.common.DateUtils;

/**
 * auth授权访问
 * @author guojianli
 *
 */
public class AuthAccess {
	
	public static String ACCESS_ID="accessId";
	public static String ACCESS_SECRET="accessSecret";
	public static String ACCESS_OPEN_ID="accessOpenId";

	private Long id;
	private Timestamp gmtCreate = DateUtils.now();
	private Timestamp gmtModified = DateUtils.now();
	
    private String accessId;
	
	private String accessSecret;
	
	private String accessDomain;
	
	private String useEnv;
	
	private String namespace;
	
	private Integer status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Timestamp gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Timestamp getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(Timestamp gmtModified) {
		this.gmtModified = gmtModified;
	}

	public String getAccessId() {
		return accessId;
	}

	public void setAccessId(String accessId) {
		this.accessId = accessId;
	}

	public String getAccessSecret() {
		return accessSecret;
	}

	public void setAccessSecret(String accessSecret) {
		this.accessSecret = accessSecret;
	}

	public String getAccessDomain() {
		return accessDomain;
	}

	public void setAccessDomain(String accessDomain) {
		this.accessDomain = accessDomain;
	}

	public String getUseEnv() {
		return useEnv;
	}

	public void setUseEnv(String useEnv) {
		this.useEnv = useEnv;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
