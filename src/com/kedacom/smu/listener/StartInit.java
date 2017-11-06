package com.kedacom.smu.listener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.kedacom.smu.thread.TestThread;
import com.kedacom.smu.util.ScheduledExecutorBox;

public class StartInit implements ServletContextListener {
	private static Logger logger = Logger.getLogger(StartInit.class);

	// 容器关闭执行方法
	public void contextDestroyed(final ServletContextEvent event) {
		logger.info("system stoped");
		System.out.println("system stoped");
	}

	// 容器启动执行方法
	@SuppressWarnings("rawtypes")
	public void contextInitialized(final ServletContextEvent event) {
		logger.info("system started");
		System.out.println("system started");

		ScheduledExecutorService TestThreadService = Executors.newScheduledThreadPool(1);
		ScheduledExecutorBox TestThreadBox = new ScheduledExecutorBox<TestThread>(TestThread.class);

		// 设定执行线程计划,初始0s延迟,每次任务完成后延迟10s再执行一次任务
		TestThreadService.scheduleWithFixedDelay(TestThreadBox, 0, 10, TimeUnit.SECONDS);
		TestThreadBox = null;

		logger.info("system init end");
		System.out.println("system init end");
	}

}
