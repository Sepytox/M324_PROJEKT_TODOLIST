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
			return "error";
		}
		
		try {
			Task task = mapper.readValue(taskdescription, Task.class);
			
			if (task.getTaskdescription() == null || task.getTaskdescription().trim().isEmpty()) {
				logger.warning("Task with empty description");
				return "error";
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
			return "error";
		}
		return "success";
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/delete")
	public String delTask(@RequestBody String taskdescription) {
		System.out.println("API EP '/delete': '" + taskdescription + "'");
		
		if (taskdescription == null || taskdescription.trim().isEmpty()) {
			logger.warning("Empty task description for delete");
			return "error";
		}
		
		try {
			Task task = mapper.readValue(taskdescription, Task.class);
			
			if (task.getTaskdescription() == null || task.getTaskdescription().trim().isEmpty()) {
				logger.warning("Task with empty description for delete");
				return "error";
			}
			
			Iterator<Task> it = tasks.iterator();
			while (it.hasNext()) {
				Task t = it.next();
				if (t.getTaskdescription().equals(task.getTaskdescription())) {
					System.out.println("...deleting task: '" + task.getTaskdescription() + "'");
					it.remove();
					return "success";
				}
			}
			System.out.println(">>>task: '" + task.getTaskdescription() + "' not found!");
		} catch (JsonProcessingException e) {
			logger.warning("Failed to parse JSON for delete: " + e.getMessage());
			return "error";
		}
		return "not_found";
	}

}
