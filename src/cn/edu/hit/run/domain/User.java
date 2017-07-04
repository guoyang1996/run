package cn.edu.hit.run.domain;

import cn.edu.hit.run.service.StepDetector;

public class User {
	public User() {
		// TODO Auto-generated constructor stub
	}
	private int goal;
	private String name;
	private String gender ;
	private int step_length;
	private String ObjectId;
	
	public String getObjectId() {
		return ObjectId;
	}
	public void setObjectId(String string) {
		ObjectId = string;
	}
	private int sensitivity;
	private int today_step;
	
	public int getSensitivity() {
		return sensitivity;
	}
	public void setSensitivity(int sensitivity) {
		this.sensitivity = sensitivity;
	}
	public int getToday_step() {
		return today_step;
	}
	public void setToday_step(int today_step) {
		this.today_step = today_step;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getStep_length() {
		return step_length;
	}
	public void setStep_length(int step_length) {
		this.step_length = step_length;
	}
	public int getGoal() {
		return goal;
	}
	public void setGoal(int goal) {
		this.goal = goal;
	}
}
