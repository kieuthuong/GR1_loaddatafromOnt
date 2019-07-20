package com.example.demo.resource;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entities.Person;
import com.example.demo.service.PersonServiceImpl;

@Controller
public class PersonController {
	
	@Autowired
	private PersonServiceImpl personService;

	@GetMapping("/persons")
	public String getAll(Model model) {
		ArrayList<Person> listPerson = personService.findAll();
		model.addAttribute("persons", listPerson);
		return "listPerson";
	}
	@GetMapping("/write")
	public String writeToOnt() {
		personService.writeDataToOnt();
		return "success";
	}
}
