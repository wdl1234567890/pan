package com.example.demo.utils;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.assertj.core.internal.Bytes;
import org.springframework.util.Base64Utils;

import com.example.demo.domain.Authority;
import com.example.demo.enums.StatusCode;
import com.example.demo.exception.PanException;


/**
 * 
 * @ClassName ToolUtils
 * @Description 工具类
 * @author fuling
 * @date 2020年9月19日 上午10:31:30
 */
public class ToolUtils {
	
	public static final String algorithm = "AES";
	
	public static final String transformation = "AES/CBC/NOPadding";
	
	public static final String key = "1234567812345678";
	
	/**
	 * 
	 * @Title distinctAuthority
	 * @Description 过滤具有重复部门Id的条目，若重复了，则以第一次出现的条目为准
	 * @param authoritys 需要过滤的权限集合
	 * @return 过滤完毕的权限集合
	 */
	public static List<Authority> distinctAuthority(List<Authority> authoritys){
		if(null == authoritys || authoritys.size() == 0)return authoritys;
		Set<Integer> deparmentIds = new HashSet<>();
		return authoritys.stream().filter(auth->{
			return deparmentIds.add(auth.getDepartmentId());
			
		}).collect(Collectors.toList());
	}
	
	
	/**
	 * 
	 * @Title Encryption
	 * @Description 
	 * @param @param rawKey
	 * @param @return 参数说明
	 * @return 返回加密参数
	 * @throws
	 */
	public static String Encryption(String rawKey) {
		try {
			
			// 获取Cipher
			Cipher cipher = Cipher.getInstance(transformation);
			// 生成密钥
			SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), algorithm);
			// 指定模式(加密)和密钥
			// 创建初始化向量
			IvParameterSpec iv = new IvParameterSpec(key.getBytes());
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

			byte[] bs = rawKey.getBytes();
			
			int temp = bs.length % 16;
			byte[] bss = bs;
			if(temp != 0) {
				int temp1 = (bs.length / 16 + 1) * 16;
				bss = new byte[temp1];
				Arrays.fill(bss, (byte)0);
				System.arraycopy(bs, 0, bss, 0, bs.length);
				
			}
			
			// 加密
			byte[] bytes = cipher.doFinal(bss);
			 
			return Base64Utils.encodeToString(bytes).replaceAll("/", ";");
		}catch(Exception ex) {
			throw new PanException(StatusCode.ENCODE_ERROR.code(), ex.getMessage());
		}
		
	}
	
	/**
	 * 
	 * @Title Decryption
	 * @Description 
	 * @param @param key
	 * @param @return 参数说明
	 * @return 返回该加密参数解密后对应的文件对象
	 * @throws
	 */
	public static String Decryption(String enKey) {
		
		try {
			// 获取Cipher
			Cipher cipher = Cipher.getInstance(transformation);
			// 生成密钥
			SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), algorithm);
			// 指定模式(解密)和密钥
			// 创建初始化向量
			IvParameterSpec iv = new IvParameterSpec(key.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
		
			// 解密
			enKey = enKey.replaceAll(";", "/");
			byte[] bytes = cipher.doFinal(Base64Utils.decodeFromString(enKey));
			
			return new String(bytes);
		
			
		}catch(Exception ex) {
			throw new PanException(StatusCode.DECODE_ERROR.code(), StatusCode.DECODE_ERROR.message());
		}
		
	}
}
