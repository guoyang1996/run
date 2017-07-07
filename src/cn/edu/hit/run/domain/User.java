package cn.edu.hit.run.domain;

import cn.edu.hit.run.service.StepDetector;

public class User {
	private String gender ;
	private int goal;
	private String name;
	private String ObjectId;
	private int sensitivity;
	private int step_length;
	
	private int today_step;
	
	public User() {
		// TODO Auto-generated constructor stub
	}
	public String getGender() {
		return gender;
	}
	public int getGoal() {
		return goal;
	}
	
	public String getName() {
		return name;
	}
	public String getObjectId() {
		return ObjectId;
	}
	public int getSensitivity() {
		return sensitivity;
	}
	public int getStep_length() {
		return step_length;
	}
	public int getToday_step() {
		return today_step;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public void setGoal(int goal) {
		this.goal = goal;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setObjectId(String string) {
		ObjectId = string;
	}
	public void setSensitivity(int sensitivity) {
		this.sensitivity = sensitivity;
	}
	public void setStep_length(int step_length) {
		this.step_length = step_length;
	}
	public void setToday_step(int today_step) {
		this.today_step = today_step;
	}
}
