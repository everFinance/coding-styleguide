package com.example.demo.model;

import java.io.Serializable;

/**
 * @author shiwen.wy
 * @date 2023/12/3 21:46
 */
public class Student implements Serializable {
	private static final long serialVersionUID = -3272421320600950226L;

	private long id;

	private String name;

	private int age;

	private int score;

	public boolean hasPassed() {
		if (score >=60) {
			return true;
		}
		return false;
	}

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
}
