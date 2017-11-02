package com.kedacom.smu.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.DefaultScriptExecutor;
import org.springframework.data.redis.core.script.ScriptExecutor;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kedacom.shiro.demo.session.RedisRepository;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class ToolsUtil {
	private static Logger log = Logger.getLogger(ToolsUtil.class);

	/**
	 * 判断字符串是否为空，为空则返回true，否则返回false
	 * 
	 * @param str
	 *            要判断的字符串
	 * @return boolean 为空则返回true，否则返回false
	 */
	public static boolean isNull(String str) {
		if (str == null || str.length() == 0)
			return true;
		else
			return false;
	}

	/**
	 * 连接多个对象，返回字符串
	 * 
	 * @param objects
	 * @return
	 */
	public static String concat(Object... objects) {
		if (objects == null) {
			return null;
		}

		StringBuffer sb = new StringBuffer();
		for (Object obj : objects) {
			sb.append(obj);
		}
		return sb.toString();
	}

	/**
	 * 得到gmt时间的前十位，去除毫秒，主要用于给流媒体服务器传时间参数
	 * 
	 * @param gmtTime
	 *            gmt时间
	 * @return 去除毫秒的 gmt时间
	 */
	public static String substringTime(long gmtTime) {
		String dateString = String.valueOf(gmtTime);
		return dateString.substring(0, 10);
	}

	/**
	 * 得到全球唯一UUID
	 * 
	 * @return 全球唯一UUID
	 */
	public static String getUUID() {
		String s = UUID.randomUUID().toString();
		// 去掉"-"符号
		return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
	}

	public static String getDataBaseUUID() {
		return getUUID();
	}

	public static String replaceLast(String text, String regex, String replacement) {
		return text.replaceFirst("(?s)" + regex + "(?!.*?" + regex + ")", replacement);
	}

	public static String getCurDate() {
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();

		return s.format(d);
	}

	public static long getCurDateLong() {
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date d = new Date();
		return d.getTime();
	}

	/*
	 * 用Base64编码
	 */
	public static String encodeBASE64(byte[] bytes) throws Exception {
		if (bytes == null)
			return null;
		BASE64Encoder baE64 = new BASE64Encoder();
		String result = null;
		result = baE64.encode(bytes);
		return result;
	}

	/*
	 * 用Base64解码
	 */
	public static byte[] decodeBASE64(String str) throws Exception {
		byte dBytes[] = null;
		BASE64Decoder baD64 = new BASE64Decoder();
		try {
			dBytes = baD64.decodeBufferToByteBuffer(str).array();
		} catch (IOException e) {
			throw new Exception("BASE64Decoder 转码失败 : " + e);
		}
		return dBytes;
	}

	/**
	 * 压缩文件
	 * 
	 * @param fileName
	 *            压缩文件名
	 * @return 压缩后的文件名
	 * @throws IOException
	 *             文件IO异常
	 */
	public static String createTarFile(String fileName) throws IOException {

		String desFile = fileName + ".zip";
		File file = new File(fileName);
		if (!file.exists()) {
			throw new IOException(fileName + " 文件不存在.");
		}
		FileInputStream fis = new FileInputStream(fileName);
		BufferedInputStream bis = new BufferedInputStream(fis);
		FileOutputStream fos = new FileOutputStream(desFile);
		GZIPOutputStream gos = new GZIPOutputStream(fos);
		BufferedOutputStream bos = new BufferedOutputStream(gos);
		try {
			byte[] buffer = new byte[1024];
			int i = 0;
			while ((i = bis.read(buffer)) != -1) {
				bos.write(buffer, 0, i);
			}
			bos.flush();

		} catch (IOException ioe) {
			log.error("createTarFile IOException", ioe);
			throw ioe;
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();

		}
		return desFile;
	}

	/**
	 * 解压文件
	 * 
	 * @param fileName
	 *            文件名
	 * @return 解压后的文件名
	 * @throws IOException
	 */
	public static String getTarFile(String fileName) throws IOException {

		String desFile = fileName.substring(0, fileName.length() - 4);
		File file = new File(fileName);
		if (!file.exists()) {
			throw new IOException(fileName + " 文件不存在.");
		}
		FileInputStream fis = new FileInputStream(fileName);
		GZIPInputStream gis = new GZIPInputStream(fis);
		BufferedInputStream bis = new BufferedInputStream(gis);
		FileOutputStream fos = new FileOutputStream(desFile);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		try {
			byte[] buffer = new byte[1024];
			int i = 0;
			while ((i = bis.read(buffer)) != -1) {
				bos.write(buffer, 0, i);
			}
			bos.flush();
		} catch (IOException ioe) {
			log.error("createTarFile getTarFile", ioe);
			throw ioe;
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();

		}
		return desFile;
	}

	// 文件复制
	public static void FileCopy(File fin, File fout) throws Exception {
		FileInputStream in = new FileInputStream(fin);
		FileOutputStream out = new FileOutputStream(fout);
		byte[] buffer = new byte[1024];
		int length = -1;
		while ((length = in.read(buffer)) != -1) {
			out.write(buffer, 0, length);
			out.flush();
		}
		out.flush();
		in.close();
		out.close();

	}

	public static String getMethodName() {
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		StackTraceElement e = stacktrace[2];
		String methodName = e.getMethodName();
		return methodName;
	}

	public static List<Object> getBusinessDataFromRedis(RedisRepository redisRepository, List<Object> list,
			Object... objs) {
		RedisTemplate rt = redisRepository.getJedisManager().getRedisTemplate();
		return getBusinessDataFromRedis(rt, list, objs);
	}

	public static List<Object> getBusinessDataFromRedis(RedisTemplate rt, List<Object> list, Object... objs) {
		DefaultRedisScript<Object> script = new DefaultRedisScript<Object>();
		script.setLocation(new FileSystemResource("/opt/kdm/system/script/getFromRedis.lua"));
		script.setResultType(Object.class);
		System.out.println("getSha1 " + script.getSha1());

		GenericToStringSerializer<Object> ser = new GenericToStringSerializer<Object>(Object.class);
		ScriptExecutor<Object> se = new DefaultScriptExecutor<Object>(rt);
		List<Object> redisRets = (List<Object>) se.execute(script, ser, ser, list, objs);
		return redisRets;
	}

	public static void dumpList(String string, List<Object> list) {
		int i = 0;
		for (Object item : list) {
			if (item instanceof List) {
				dumpList(string + i, (List) item);
			} else {
				System.out.println(String.format("%s%d %s", string, i, item));
			}
			++i;
		}
	}

	public static long getSysUptime() {
		try {
			return Double.valueOf(new Scanner(new FileInputStream("/proc/uptime")).next()).longValue();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public static String bean2Json(Object obj) {
		Gson gson = new GsonBuilder().setPrettyPrinting()
				.excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT)
				.excludeFieldsWithModifiers(java.lang.reflect.Modifier.FINAL, java.lang.reflect.Modifier.STATIC)
				.disableHtmlEscaping().create();
		return gson.toJson(obj);
	}

	public static void main(String[] args) {
		System.out.println(getDataBaseUUID());
	}

}
