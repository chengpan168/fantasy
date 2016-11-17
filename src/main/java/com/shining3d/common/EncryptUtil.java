/*
 *  Copyright (c) 2014 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.shining3d.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import org.apache.commons.codec.digest.DigestUtils;

public class EncryptUtil
{
  private static final String UTF8 = "utf-8";

  public static String md5Digest(String src) throws NoSuchAlgorithmException, UnsupportedEncodingException
  {
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] b = md.digest(src.getBytes("utf-8"));
    return byte2HexStr(b);
  }

  public static String base64Encoder(String src) throws UnsupportedEncodingException
  {
    BASE64Encoder encoder = new BASE64Encoder();
    return encoder.encode(src.getBytes("utf-8"));
  }

  public static String base64Decoder(String dest)
    throws NoSuchAlgorithmException, IOException
  {
    BASE64Decoder decoder = new BASE64Decoder();
    return new String(decoder.decodeBuffer(dest), "utf-8");
  }

  private static String byte2HexStr(byte[] b)
  {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < b.length; ++i) {
      String s = Integer.toHexString(b[i] & 0xFF);
      if (s.length() == 1)
        sb.append("0");

      sb.append(s.toUpperCase());
    }
    return sb.toString();
  }
  

  
  public static String shaEncrypt(String strSrc, String encName) {
      MessageDigest md = null;
      String strDes = null;

      byte[] bt = strSrc.getBytes();
      try {
          if (encName == null || encName.equals("")) {
              encName = "SHA-256";
          }
          md = MessageDigest.getInstance(encName);
          md.update(bt);
          strDes = bytes2Hex(md.digest()); // to HexString
      } catch (NoSuchAlgorithmException e) {
          return null;
      }
      return strDes;
  }

  public static String bytes2Hex(byte[] bts) {
      String des = "";
      String tmp = null;
      for (int i = 0; i < bts.length; i++) {
          tmp = (Integer.toHexString(bts[i] & 0xFF));
          if (tmp.length() == 1) {
              des += "0";
          }
          des += tmp;
      }
      return des;
  }

  /**
   * 签名字符串
   * @param text 需要签名的字符串
   * @param key 密钥
   * @param input_charset 编码格式
   * @return 签名结果
   */
  public static String sign(String text, String key, String input_charset) {
  	text = text + key;
      return DigestUtils.md5Hex(getContentBytes(text, input_charset));
  }
  
  /**
   * 签名字符串
   * @param text 需要签名的字符串
   * @param sign 签名结果
   * @param key 密钥
   * @param input_charset 编码格式
   * @return 签名结果
   */
  public static boolean verify(String text, String sign, String key, String input_charset) {
  	text = text + key;
  	String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
  	if(mysign.equals(sign)) {
  		return true;
  	}
  	else {
  		return false;
  	}
  }

  /**
   * @param content
   * @param charset
   * @return
   * @throws SignatureException
   * @throws UnsupportedEncodingException 
   */
  private static byte[] getContentBytes(String content, String charset) {
      if (charset == null || "".equals(charset)) {
          return content.getBytes();
      }
      try {
          return content.getBytes(charset);
      } catch (UnsupportedEncodingException e) {
          throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
      }
  }
}