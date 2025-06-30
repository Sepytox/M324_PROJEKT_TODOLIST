package com.example.demo;

public class Task {
	
	private String taskdescription;

	public Task() {
    }

	public Task(String taskdescription) {
		setTaskdescription(taskdescription);
	}

	public String getTaskdescription() {
		return taskdescription;
	}

	public void setTaskdescription(String taskdescription) {
		if (taskdescription != null && taskdescription.length() > 200) {
			this.taskdescription = taskdescription.substring(0, 200);
		} else {
			this.taskdescription = taskdescription;
		}
	}

}