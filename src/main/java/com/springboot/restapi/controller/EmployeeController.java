package com.springboot.restapi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;


import com.springboot.restapi.exception.ResourceNotFoundException;
import com.springboot.restapi.model.Employee;
import com.springboot.restapi.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

	static List<Employee> empList = new ArrayList<>();
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
	public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "id") long empId,
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
	public ResponseEntity<Employee> deleteEmployeeById(@PathVariable(value = "id") long empId)
			throws ResourceNotFoundException {
		employeeRepository.findById(empId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + empId));
		employeeRepository.deleteById(empId);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/load")
	public ResponseEntity<List<Employee>> loadAllEmployees() {
		IntStream.range(0, 100).forEach(i -> {
			empList.add(new Employee(i, "FirstName-" + i, "LastName-" + i, "email" + i + "@example.com"));
		});

	//	employeeRepository.saveAll(empList);
		return new ResponseEntity<>(empList, HttpStatus.CREATED);
	}
	@GetMapping("/getAll")
	public ResponseEntity<List<Employee>> getPreLoadedEmployees() {
		//List<Employee> all = employeeRepository.findAll();

		return new ResponseEntity<>(empList, HttpStatus.OK);
	}


}
