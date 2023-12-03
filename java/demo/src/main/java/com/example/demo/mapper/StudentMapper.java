package com.example.demo.mapper;

import com.example.demo.entity.StudentDO;

/**
 * @author shiwen.wy
 * @date 2023/12/3 21:38
 */
public interface StudentMapper {

	StudentDO getById(long id);
}
