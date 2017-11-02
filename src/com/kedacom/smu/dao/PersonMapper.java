package com.kedacom.smu.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.kedacom.smu.bean.Person;

public interface PersonMapper {

	/**
	 * 查询所有
	 * 
	 * @return
	 */
	List<Person> queryAll();

	int countAll();

	void insert(Person person);

	void delete(Integer id);

	void update(Person person);

	Person findByName(String name);

	Person findById(Integer id);

	List<Person> listByPage(RowBounds rowBound);

	List<Person> listByParams(Map<String, Object> params);

}
