package com.kedacom.smu.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kedacom.smu.bean.Person;
import com.kedacom.smu.dao.PersonMapper;
import com.kedacom.smu.service.IPersonService;
import com.kedacom.smu.util.Page;

@Transactional
@Service("personService")
public class PersonServiceImpl implements IPersonService {

	@Autowired
	private PersonMapper personMapper;

	public List<Person> queryAll() {
		return personMapper.queryAll();
	}

	public void add(Person person) {
		personMapper.insert(person);
	}

	public void delete(Integer id) {
		personMapper.delete(id);
	}

	public void update(Person person) throws InterruptedException {
		personMapper.update(person);
		
		System.out.println("sleep 8 s");
		Thread.sleep(8000);
		
		//int i = 1/0;
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
