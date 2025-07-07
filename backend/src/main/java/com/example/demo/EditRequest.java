package com.example.demo;

public class EditRequest {
	
	private int id;
	private String newDescription;
	
	public EditRequest() {
	}
	
	public EditRequest(int id, String newDescription) {
		this.id = id;
		this.newDescription = newDescription;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getNewDescription() {
		return newDescription;
	}
	
	public void setNewDescription(String newDescription) {
		this.newDescription = newDescription;
	}
}
