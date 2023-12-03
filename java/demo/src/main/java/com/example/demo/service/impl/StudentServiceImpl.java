package com.example.demo.service.impl;

import com.example.demo.entity.StudentDO;
import com.example.demo.mapper.StudentMapper;
import com.example.demo.model.Student;
import com.example.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shiwen.wy
 * @date 2023/12/3 21:49
 */
@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentMapper studentMapper;

	@Override
	public Student getStudent(long id) {
		StudentDO studentDO = studentMapper.getById(id);
		return convertToDomain(studentDO);
	}

	private Student convertToDomain(StudentDO studentDO) {
		if (studentDO == null) {
			return null;
		}
		Student student = new Student();
		student.setAge(studentDO.getAge());
		student.setId(studentDO.getId());
		student.setName(studentDO.getName());
		student.setScore(studentDO.getScore());
		return student;
	}
}
