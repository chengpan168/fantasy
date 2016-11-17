package com.shining3d.saber.controller.interceptor;

import java.lang.reflect.Method;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shining3d.saber.controller.anotation.AuthAccessRequired;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;
import com.shining3d.common.AppConstants;
import com.shining3d.common.dto.Result;
import com.shining3d.common.resource.AuthAccess;

public class AuthAccessRequiredInterceptor extends HandlerInterceptorAdapter{

//	@Resource
//	private IResourceManager resourceManager;
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub
	    if(request.getMethod().toLowerCase().equals("options")){
	    	return super.preHandle(request, response, handler);
		}
		if(isAccessRequire(handler)){//需要验证accesstoken
			JSONObject json = new JSONObject();
			Result<Boolean> ret = new Result<Boolean>();
			Properties props = System.getProperties();
			String propsRunMode = props.getProperty(AppConstants.RUN_MODE, AppConstants.DEFAULT_RUN_MODE);
			String accessId = request.getHeader(AuthAccess.ACCESS_ID);
			if(StringUtils.isEmpty(accessId)){
				accessId = request.getParameter(AuthAccess.ACCESS_ID);
			}
			String accessSecret = request.getHeader(AuthAccess.ACCESS_SECRET);
			if(StringUtils.isEmpty(accessSecret)){
				accessSecret = request.getParameter(AuthAccess.ACCESS_SECRET);
			}
//			String accessOpenId = request.getParameter(AuthAccess.ACCESS_OPEN_ID);
//			if(StringUtils.isNotEmpty(accessId)&&StringUtils.isNotEmpty(accessSecret)){
//				Object obj = resourceManager.getResItem(ResourceContant.METIS_AUTH_ITEMS, accessId);
//			    if(obj!=null){
//			    	AuthAccess authAccess = (AuthAccess)obj;
//			    	if(propsRunMode.toLowerCase().equals(authAccess.getUseEnv().toLowerCase())
//			    			&&accessId.equals(authAccess.getAccessId())
//			    			&&accessSecret.equals(authAccess.getAccessSecret())){
//			    		return super.preHandle(request, response, handler);
//			    	}
//			    }
//			}
			ret.setErrorInfo("405", "auth fail,forbidden request");
			json.put("ret", ret);
			response.getWriter().write(json.toJSONString());
			return false;
//			else if(StringUtils.isNotEmpty(accessOpenId)){
//				String date = JSON.parseObject(jedisService.hget(resourceManager.getAppName()+"_"+ResourceConstants.PANGU_AUTH_ACCESS, accessOpenId),String.class);
//				if(StringUtils.isNotEmpty(date)){
//					Date rdate = new Date(Long.parseLong(date));
//					if(StringUtils.isNotEmpty(date)&&rdate.after(DateUtils.current())){
//						return super.preHandle(request, response, handler); 
//					}
//				}else{
//					return super.preHandle(request, response, handler);
//				}
//				
//			}
		}
	    
	   
		return super.preHandle(request, response, handler);
	}
	
	public boolean isAccessRequire(Object handler){
		 boolean flag = false;
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
		    AuthAccessRequired annotation = method.getDeclaringClass().getAnnotation(AuthAccessRequired.class);
		    if(annotation!=null){
		    	flag = true;
		    }else{
		 	     annotation = method.getAnnotation(AuthAccessRequired.class);
		 	     if(annotation!=null){
		 	    	flag = true;
		 	     }
		    }
		}
		
	    return flag;
	}

}
