package com.example.demo.entity;

import java.io.Serializable;

/**
 * @author shiwen.wy
 * @date 2023/12/3 21:24
 */
public class StudentDO implements Serializable {
	private static final long serialVersionUID =
			-2334705325988480016L;

	private long id;

	private String name;

	private int age;

	private int score;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "StudentDO{" + "id=" + id + ", name='" + name + '\'' + ", age="
					   + age + ", score=" + score + '}';
	}
}
