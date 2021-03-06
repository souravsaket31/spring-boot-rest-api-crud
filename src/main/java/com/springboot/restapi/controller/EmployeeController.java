package com.springboot.restapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.restapi.exception.ResourceNotFoundException;
import com.springboot.restapi.model.Employee;
import com.springboot.restapi.repository.EmployeeRepository;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

	@Autowired
	EmployeeRepository employeeRepository;

	// 1. get all
	@GetMapping("/employees")
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	// 2. create employee
	/* JSON Body
	 * 
	 {
    	"firstName" : "Mathew",
    	"lastName" : "Perry",
    	"email" : "mperry@gmail.com"    
	 }
	 * */
	@PostMapping("/employees")
	public Employee createEmployee(@RequestBody Employee employee) {
		return employeeRepository.save(employee);
	}

	// 3. get employee by ID

	@GetMapping("/employees/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") long empId)
			throws ResourceNotFoundException {
		Employee employee = employeeRepository.findById(empId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + empId));
		return ResponseEntity.ok().body(employee);
	}

	// update employee
	@PutMapping("/employees/{id}")
	public ResponseEntity<Employee> UpdateEmployee(@PathVariable(value = "id") long empId,
			@RequestBody Employee employeeDetails) throws ResourceNotFoundException {
		Employee employee = employeeRepository.findById(empId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + empId));

		employee.setFirstName(employeeDetails.getFirstName());
		employee.setEmail(employeeDetails.getEmail());
		employee.setLastName(employeeDetails.getLastName());

		final Employee updatedEmployee = employeeRepository.save(employee);

		return ResponseEntity.ok(updatedEmployee);
	}

	// delete employee by ID
	@DeleteMapping("/employees/{id}")
	public ResponseEntity<Employee> deletEmployeeById(@PathVariable(value = "id") long empId)
			throws ResourceNotFoundException {
		employeeRepository.findById(empId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + empId));
		employeeRepository.deleteById(empId);
		return ResponseEntity.ok().build();
	}

}
