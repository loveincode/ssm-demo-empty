package com.kedacom.smu.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.kedacom.smu.bean.Person;
import com.kedacom.smu.dao.PersonMapper;
import com.kedacom.smu.service.IPersonService;
import com.kedacom.smu.util.Page;
import com.kedacom.smu.util.ToolsUtil;

@Transactional
@Service("personService")
public class PersonServiceImpl implements IPersonService {

	@Autowired
	private PersonMapper personMapper;

	public List<Person> queryAll() {
		return personMapper.queryAll();
	}

	public void add(Person person) {

		// mysql
		personMapper.insert(person);
		try {
			// redis
			
			int i = 1 / 0;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	// 调用存储函数
	public void callAddPerson(Person person) {

		// mysql 存储过程
		personMapper.callAddPerson(person);
		try {

			//WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
			//RedisRepository redisRepository = (RedisRepository) wac.getBean("redisRepository");
			
			WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
			RedisTemplate rt = (RedisTemplate) wac.getBean("redisTemplate");
			
			List<Object> list1 = new ArrayList<Object>();
	    	list1.add("testWrite");
	    	list1.add("123");
	    	List<Object> redisRets1 = ToolsUtil.getBusinessDataFromRedis(rt, list1);
	    	System.err.println(redisRets1.toString());
			List<Object> list2 = new ArrayList<Object>();
	    	list2.add("testRead");
	    	List<Object> redisRets2 = ToolsUtil.getBusinessDataFromRedis(rt, list2);
	    	
			System.err.println(redisRets2.toString());
			int i = 1/0;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public void delete(Integer id) {
		personMapper.delete(id);
	}

	public void update(Person person) {
		personMapper.update(person);

		// int i = 1/0;
	}

	public Person findByName(String name) {
		return personMapper.findByName(name);
	}

	public Person findById(Integer id) {
		return personMapper.findById(id);
	}

	public Page<Person> listByPage(int pageNo, int pageSize) {
		Page<Person> page = new Page<Person>(pageNo, pageSize);

		int offset = page.getOffsets();
		RowBounds rowBound = new RowBounds(offset, pageSize);
		System.err.println(rowBound.getOffset() + " rowbound " + rowBound.getLimit());

		List<Person> users = personMapper.listByPage(rowBound);
		page.setRows(users);
		int total = personMapper.countAll();
		page.setTotal(total);
		if (offset >= page.getTotal()) {
			page.setPageNo(page.getTotalPages());
		}
		return page;
	}

	public List<Person> listByParams(Map<String, Object> params) {
		List<Person> users = personMapper.listByParams(params);
		return users;
	}

}
