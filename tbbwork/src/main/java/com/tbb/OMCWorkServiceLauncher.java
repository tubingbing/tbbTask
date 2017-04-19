package com.tbb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 服务加载器 
 * User: tbb
 * Date: 2015-03-10 
 * Time: 10:50
 */
public class OMCWorkServiceLauncher {
	private static final Logger LOGGER = LoggerFactory.getLogger(OMCWorkServiceLauncher.class);

	public static void main(String[] args) {
		new ClassPathXmlApplicationContext(new String[] { "spring-config.xml" });
		LOGGER.info("定时推送服务已启动");
		// 启动本地服务，然后hold住本地服务
		synchronized (OMCWorkServiceLauncher.class) {
			while (true) {
				try {
					OMCWorkServiceLauncher.class.wait();
				} catch (InterruptedException e) {
					LOGGER.error("后台服务异常终止:" + e.getMessage(), e);
				}
			}
		}
	}
}
