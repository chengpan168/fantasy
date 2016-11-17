package com.shining3d.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import com.google.common.base.Splitter;

/**
 * Jedis连接池管理类
 * @Date: 2014年2月20日 下午10:00:00<br>
 * @Copyright (c) 2014 Vobile <br> * 
 * @since 1.0
 * @author coral
 */
@Service
public class JedisService {
	
	public static final String KEY_SPLITTER = "^";
	
	private static ShardedJedisPool pool;
	
	private String servers;
	private String passwd;
	
	public void init() {
		JedisPoolConfig config = new JedisPoolConfig();
		//config.setMaxWait(1000L);
		config.setMaxIdle(10);
		config.setMaxTotal(5);
		config.setMaxWaitMillis(1000l);
		config.setTestOnBorrow(false);
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		try {
			if(StringUtils.isNotBlank(this.servers)){
				List<String> hostList = Splitter.on(",").splitToList(this.servers);
				for(String hostValue: hostList){
					int index = hostValue.indexOf(":");
					String host = hostValue.substring(0, index);
					int port = Integer.parseInt(hostValue.substring(index +1));
					JedisShardInfo jsi = new JedisShardInfo(host, port);
					jsi.setPassword(this.passwd);
					shards.add(jsi);
				}
			}
			
			pool = new ShardedJedisPool(config, shards);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ShardedJedis getJedis(){
		return pool.getResource();
	}
	
	public void returnRes(ShardedJedis jedis){
		pool.returnResource(jedis);
	}
	
	public void destroy(){
		pool.destroy();
	}
	
	public long llen(String key){
		ShardedJedis jedis = this.getJedis();
		if(!jedis.exists(key)){
			return 0;
		}
		long len = jedis.llen(key);
		this.returnRes(jedis);
		return len;
	}
	
	public String hget(String key, String field){
		ShardedJedis jedis = this.getJedis();
		String retValue = jedis.hget(key, field);
		this.returnRes(jedis);
		return retValue;
	}
	
	public Long hset(String key, String field, String value){
		
		ShardedJedis jedis = this.getJedis();
		Long retValue = jedis.hset(key, field, value);
		this.returnRes(jedis);
		return retValue;
	}
	
	public Long delset(String key, String field){
		ShardedJedis jedis = this.getJedis();
		Long retValue = jedis.hdel(key, field);
		this.returnRes(jedis);
		return retValue;
	}
	
    public Long ttl(String key){
		ShardedJedis jedis = this.getJedis();
		Long ret = jedis.ttl(key);
		this.returnRes(jedis);
		return ret;
	}
    
    public Long expire(String key,int seconds){
		ShardedJedis jedis = this.getJedis();
		Long ret =  jedis.expire(key, seconds);
		this.returnRes(jedis);
		return ret;
	}

	public String getServers() {
		return servers;
	}

	public void setServers(String servers) {
		this.servers = servers;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	
}
