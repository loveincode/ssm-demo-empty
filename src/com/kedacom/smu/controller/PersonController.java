package com.kedacom.smu.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kedacom.smu.bean.Person;
import com.kedacom.smu.bean.ResultVO;
import com.kedacom.smu.service.IPersonService;
import com.kedacom.smu.util.Page;
import com.kedacom.smu.util.ToolsUtil;

@Controller
@RequestMapping("/person")
public class PersonController {

	private static Logger log = Logger.getLogger(PersonController.class);

	@Autowired
	private IPersonService personService;

	/**
	 * 4.1 person列表
	 * 
	 * @param pageNo
	 * @param pageNum
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(method = { RequestMethod.GET }, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getAll(@RequestParam(value = "pageNo", required = true) Integer pageNo,
			@RequestParam(value = "pageSize", required = true) Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) {
		ResultVO resultVO = new ResultVO();
		System.out.println(pageNo + " " + pageSize);
		Page<Person> pages = personService.listByPage(pageNo, pageSize);
		resultVO.setData(pages);
		return resultVO.toString();
	}

	// 查询 /id get方式
	// http://localhost:8080/ssm/person/1
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody String show(@PathVariable Integer id, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("ENTER " + ToolsUtil.getMethodName());
		ResultVO resultVO = new ResultVO();
		Person person = new Person();
		person = personService.findById(id);
		if (person != null) {
			resultVO.setSuccess(true);
			resultVO.setData(person);
			resultVO.setMessage("查询成功");
		} else {
			resultVO.setMessage("查询失败，没找到id=" + id + "的Person");
		}
		return resultVO.toString();
	}

	// 新增 POST方式
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody String add(@ModelAttribute("person") Person person, HttpServletRequest request,
			HttpServletResponse response) {
		ResultVO resultVO = new ResultVO();
		System.out.println(person.toString());
		if (person.getName() != null) {
			personService.add(person);
			resultVO.setSuccess(true);
			resultVO.setMessage("插入成功");
		} else {
			resultVO.setMessage("name为空，插入失败");
		}
		return resultVO.toString();
	}

	// 更新 /id PUT
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public String update(@PathVariable Integer id, @ModelAttribute("person") Person person, HttpServletRequest request,
			HttpServletResponse response) throws InterruptedException {
		ResultVO resultVO = new ResultVO();
		Person oldperson = personService.findById(id);
		if (oldperson != null) {
			if (person.getName() != null) {
				person.setId(oldperson.getId());
				personService.update(person);
				resultVO.setMessage("更新成功");
			} else {
				resultVO.setMessage("更新失败，name为空");
			}
		} else {
			resultVO.setMessage("更新失败，不存在 id = " + id + "的Person");
		}
		return resultVO.toString();
	}

	// 删除 /id DELETE方式
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public String delete(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response) {
		ResultVO resultVO = new ResultVO();
		Person person = new Person();
		person = personService.findById(id);
		if (person != null) {
			resultVO.setSuccess(true);
			personService.delete(id);
			resultVO.setMessage("删除成功");
		} else {
			resultVO.setMessage("删除失败，不存在id = " + id + "的Person");
		}
		return resultVO.toString();
	}

}
