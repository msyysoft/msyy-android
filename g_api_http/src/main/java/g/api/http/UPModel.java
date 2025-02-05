/*
 *
 * Copyright (C) 2015 The Android API Project
 */
package g.api.http;

/**
 * @类的描述 [网络模型类，主要用于保存HOST和切换HOST]
 * @网路切换备注1 [判断网络类型流程：首先访问内网3秒->通就是内网 ，执行切换:不通认为是外网，默认外网，不用切换]
 * @网路切换备注2 [判断网络应放在入口Activity的开始，最好弹出loading告知用户如“正在判断网络类型...”，并在3s后告知用户结果，如吐司“当前网络类型为内网”]
 * @网路切换备注3 [千万不要忘记对返回数据内网址的切换，详见<b>switchData<b>方法]
 */
public abstract class UPModel {
	/**
	 * 外网类型标识
	 */
	final public static int NET_TYPE_OUTSIDE = 1000;
	/**
	 * 内网类型标识
	 */
	final public static int NET_TYPE_INSIDE = 1001;

	protected UPModel() {
		OUTSIDE_HTTP_URL = onSetOutSideHttpUrl();
		INSIDE_HTTP_URL = onSetInSideHttpUrl();
		//优先外网
		setNetType(NET_TYPE_OUTSIDE);
	}

	private String OUTSIDE_HTTP_URL = "http://192.168.1.100";//需要子类传过来
	private String INSIDE_HTTP_URL = "http://192.168.1.100";//需要子类传过来
	private int NOW_USE_NET_TYPE;
	private String NOW_USE_HTTP_URL;

	/**
	 * 切换内外网
	 *
	 * @param NET_TYPE 网络类型：外网NET_TYPE_OUTSIDE，内网NET_TYPE_INSIDE
	 * @return 切换结果：成功(ture)失败(false)
	 */
	public boolean setNetType(int NET_TYPE) {
		boolean succ = false;
		switch (NET_TYPE) {
			case NET_TYPE_OUTSIDE: {
				NOW_USE_NET_TYPE = NET_TYPE_OUTSIDE;
				NOW_USE_HTTP_URL = OUTSIDE_HTTP_URL;
				succ = true;
				break;
			}
			case NET_TYPE_INSIDE: {
				NOW_USE_NET_TYPE = NET_TYPE_INSIDE;
				NOW_USE_HTTP_URL = INSIDE_HTTP_URL;
				succ = true;
				break;
			}
		}
		return succ;
	}

	;

	/**
	 * 获取当前使用的网络类型
	 *
	 * @return 外网(true)内网(false)
	 */
	public boolean getNetType() {
		if (NOW_USE_NET_TYPE == NET_TYPE_OUTSIDE) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取当前使用的HTTP请求的URL
	 *
	 * @return HTTP请求的URL
	 */
	public String getNowUseHttpUrl() {
		return NOW_USE_HTTP_URL;
	}

	/**
	 * 获取外网使用的HTTP请求的URL
	 *
	 * @return HTTP请求的URL
	 */
	public String getOutsideHttpUrl() {
		return OUTSIDE_HTTP_URL;
	}

	/**
	 * 获取内网使用的HTTP请求的URL
	 *
	 * @return HTTP请求的URL
	 */
	public String getInsideHttpUrl() {
		return INSIDE_HTTP_URL;
	}

	/**
	 * 切换网络返回数据中的地址，一般为JSON数据中的图片地址（版本更新，卫星雷达，图片下载，PDF文档只要返回数据中包含地址都要做数据切换）
	 *
	 * @param data 切换地址前的数据
	 * @return 切换地址后的数据
	 */
	public String switchData(String data) {
		if (data != null) {
			if (getNetType()) {
				data = data.replace(INSIDE_HTTP_URL, OUTSIDE_HTTP_URL);
			} else {
				data = data.replace(OUTSIDE_HTTP_URL, INSIDE_HTTP_URL);
			}
		}
		return data;
	}

	/**
	 * 子类必须传递外网HTTP_URL
	 */
	abstract protected String onSetOutSideHttpUrl();

	/**
	 * 子类必须传递内网HTTP_URL
	 */
	abstract protected String onSetInSideHttpUrl();
}
