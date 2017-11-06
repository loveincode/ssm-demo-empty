package com.kedacom.smu.thread;

import java.util.Date;

import org.apache.log4j.Logger;

import com.kedacom.smu.bean.Person;
import com.kedacom.smu.service.IPersonService;

public class TestThread implements Runnable {

	private static Logger logger = Logger.getLogger(TestThread.class);

	private static IPersonService personService;

	@Override
	public void run() {
		logger.info("test thread 执行" + new Date().toString());
		Person person = new Person();
		person.setName("thread add");
		person.setAge(30);

		personService.callAddPerson(person);
	}

	public static IPersonService getPersonService() {
		return personService;
	}

	public static void setPersonService(IPersonService personService) {
		TestThread.personService = personService;
	}

}
