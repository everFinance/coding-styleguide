package com.example.demo.controller;

import com.example.demo.model.Student;
import com.example.demo.service.StudentService;
import com.example.demo.vo.StudentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author shiwen.wy
 * @date 2023/12/3 21:33
 */
@Controller
public class StudentController {

	@Autowired
	private StudentService studentService;
	@RequestMapping(value = "/student/{id}", method = RequestMethod.GET)
	@ResponseBody
	public StudentVO queryStudent(@PathVariable("id") String id) {
		Student student = studentService.getStudent(Long.parseLong(id));
		return convertToVO(student);
	}

	private StudentVO convertToVO(Student student) {
		if (student == null) {
			return null;
		}
		StudentVO vo = new StudentVO();
		vo.setId(student.getId());
		vo.setName(student.getName());
		vo.setAge(student.getAge());
		vo.setScore(student.getScore());
		return vo;
	}
}
