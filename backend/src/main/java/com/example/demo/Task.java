package com.example.demo;

public class Task {
	
	private static int idCounter = 1;
	private int id;
	private String taskdescription;

	public Task() {
		this.id = idCounter++;
    }

	public Task(String taskdescription) {
		this.id = idCounter++;
		setTaskdescription(taskdescription);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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