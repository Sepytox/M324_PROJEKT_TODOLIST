package com.example.demo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@SpringBootApplication
public class DemoApplication {

	private static final Logger logger = Logger.getLogger(DemoApplication.class.getName());
	private final ObjectMapper mapper = new ObjectMapper();
	private static final String ERROR_RESPONSE = "error";
	private static final String SUCCESS_RESPONSE = "success";
	
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	private List<Task> tasks = new ArrayList<>();

	public List<Task> getTasks() {
		return tasks;
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/")
	public List<Task> getTasksEndpoint() {

		System.out.println("API EP '/' returns task-list of size " + tasks.size() + ".");
		if (tasks.size() > 0) {
			int i = 1;
			for (Task task : tasks) {
				System.out.println("-task " + (i++) + ":" + task.getTaskdescription());
			}
		}
		return tasks;
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/tasks")
	public String addTask(@RequestBody String taskdescription) {
		System.out.println("API EP '/tasks': '" + taskdescription + "'");
		
		if (taskdescription == null || taskdescription.trim().isEmpty()) {
			logger.warning("Empty task description received");
			return ERROR_RESPONSE;
		}
		
		try {
			Task task = mapper.readValue(taskdescription, Task.class);
			
			if (task.getTaskdescription() == null || task.getTaskdescription().trim().isEmpty()) {
				logger.warning("Task with empty description");
				return ERROR_RESPONSE;
			}
			
			for (Task t : tasks) {
				if (t.getTaskdescription().equals(task.getTaskdescription())) {
					System.out.println(">>>task: '" + task.getTaskdescription() + "' already exists!");
					return "duplicate";
				}
			}
			System.out.println("...adding task: '" + task.getTaskdescription() + "'");
			tasks.add(task);
		} catch (JsonProcessingException e) {
			logger.warning("Failed to parse JSON: " + e.getMessage());
			return ERROR_RESPONSE;
		}
		return SUCCESS_RESPONSE;
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/delete")
	public String delTask(@RequestBody String taskdescription) {
		System.out.println("API EP '/delete': '" + taskdescription + "'");
		
		if (taskdescription == null || taskdescription.trim().isEmpty()) {
			logger.warning("Empty task description for delete");
			return ERROR_RESPONSE;
		}
		
		try {
			Task task = mapper.readValue(taskdescription, Task.class);
			
			if (task.getTaskdescription() == null || task.getTaskdescription().trim().isEmpty()) {
				logger.warning("Task with empty description for delete");
				return ERROR_RESPONSE;
			}
			
			Iterator<Task> it = tasks.iterator();
			while (it.hasNext()) {
				Task t = it.next();
				if (t.getTaskdescription().equals(task.getTaskdescription())) {
					System.out.println("...deleting task: '" + task.getTaskdescription() + "'");
					it.remove();
					return SUCCESS_RESPONSE;
				}
			}
			System.out.println(">>>task: '" + task.getTaskdescription() + "' not found!");
		} catch (JsonProcessingException e) {
			logger.warning("Failed to parse JSON for delete: " + e.getMessage());
			return ERROR_RESPONSE;
		}
		return "not_found";
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/edit")
	public String editTask(@RequestBody String requestBody) {
		System.out.println("API EP '/edit': '" + requestBody + "'");
		
		if (requestBody == null || requestBody.trim().isEmpty()) {
			logger.warning("Empty request body for edit");
			return ERROR_RESPONSE;
		}
		
		try {
			EditRequest editRequest = mapper.readValue(requestBody, EditRequest.class);
			
			if (editRequest.getId() <= 0 || editRequest.getNewDescription() == null || 
				editRequest.getNewDescription().trim().isEmpty()) {
				logger.warning("Invalid edit request");
				return ERROR_RESPONSE;
			}
			
			for (Task task : tasks) {
				if (task.getId() == editRequest.getId()) {
					String oldDesc = task.getTaskdescription();
					task.setTaskdescription(editRequest.getNewDescription());
					System.out.println("...editing task " + editRequest.getId() + ": '" + oldDesc + "' -> '" + editRequest.getNewDescription() + "'");
					return "success";
				}
			}
			System.out.println(">>>task with id " + editRequest.getId() + " not found!");
		} catch (JsonProcessingException e) {
			logger.warning("Failed to parse JSON for edit: " + e.getMessage());
			return ERROR_RESPONSE;
		}
		return "not_found";
	}

}
