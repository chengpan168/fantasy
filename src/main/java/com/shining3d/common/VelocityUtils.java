package com.shining3d.common;

import java.io.StringWriter;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class VelocityUtils {
	
	protected static Logger logger = LoggerFactory.getLogger(VelocityUtils.class);
	
	public static String parseString(String content, Map params){
		Template template = Velocity.getTemplate(content);
		StringWriter sw = new StringWriter();
		VelocityContext context = new VelocityContext(params);
		template.merge(context, sw);
		return sw.toString();
	}
	
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//      String  str = "尊敬的用户 ${realName} 您好 :<br/><br/>感谢您注册成为U贷会员，请点击以下链接,验证您的邮箱。<br/><a href='${basePath}user/active?token=${token}' target='_blank'>${basePath}user/active?token=${token}</a><br/><br/>祝您生活愉快<br/>U贷 运营组<br/>";
//      Map params = new HashMap();
//      params.put("basePath", "lllllllll");
//      params.put("token", "lllllllll");
//      System.out.println(parseString(str,params));
//	}

}
