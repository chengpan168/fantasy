package com.shining3d.saber.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.shining3d.common.AppConstants;
import com.shining3d.common.dto.Result;
import com.shining3d.common.resource.IResourceManager;

@RestController
@RequestMapping("/")
public class IndexController {
	 	@Resource
		private IResourceManager resourceManager;
	 	
	 
	    @RequestMapping(path="/status",method = RequestMethod.GET)
		public @ResponseBody JSONObject status(){
	    	JSONObject json = new JSONObject();
	    	json.put("app", "metis");
	    	json.put(AppConstants.STATUS, "ok");
			return json;
	    }
	    
	    @RequestMapping(path="/refresh",method = RequestMethod.POST)
	   	public @ResponseBody JSONObject refresh(@RequestParam("resName") String resName){
	    	Result<Boolean> ret = new Result<Boolean>();
	    	boolean flag = false;
			try {
				flag = resourceManager.refresh(resName);
			} catch (Exception e) {
			}
	    	ret.setResult(flag);
	    	JSONObject json = new JSONObject();
	       	json.put("ret", ret);
	   		return json;
	   	}
	    
	    
}
