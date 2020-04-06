package me.hotsse.vueslack.test.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

	@GetMapping("/send")
	public String getTest() {
		return "GET 200 OK";
	}
	
	@PostMapping("/send")
	public String postTest() {
		return "POST 200 OK";
	}
	
	@PutMapping("/send")
	public String putTest() {
		return "PUT 200 OK";
	}
	
	@DeleteMapping("/send")
	public String deleteTest() {
		return "DELETE 200 OK";
	}	
}
