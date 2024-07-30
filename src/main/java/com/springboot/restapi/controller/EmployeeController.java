package com.springboot.restapi.controller;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.springboot.restapi.exception.ResourceNotFoundException;
import com.springboot.restapi.model.Employee;
import com.springboot.restapi.repository.EmployeeRepository;
import com.springboot.restapi.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	EmployeeService employeeService;

	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

	// 1. get all
	@GetMapping("/employees")
	public List<Employee> getAllEmployees() {
		logger.info("Fetching all employees from h2 db");
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
		logger.info("saved employee data in h2 db {}", employee.toString());
		return employeeRepository.save(employee);
	}

	// 3. get employee by ID

	@GetMapping("/employees/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") long empId)
			throws ResourceNotFoundException {
		Employee employee = employeeRepository.findById(empId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + empId));

		logger.info("fetching employee by id {}", empId);
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
		logger.info("updating employee by id {}", empId);

		return ResponseEntity.ok(updatedEmployee);
	}

	// delete employee by ID
	@DeleteMapping("/employees/{id}")
	public ResponseEntity<Employee> deleteEmployeeById(@PathVariable(value = "id") long empId)
			throws ResourceNotFoundException {
		employeeRepository.findById(empId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + empId));
		employeeRepository.deleteById(empId);
		logger.info("deleting employee by id {}", empId);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/load")
	public ResponseEntity<List<Employee>> staticLoadEmployees() {
		List<Employee> employees = employeeService.loadEmployees();
		logger.info("Storing static list of employees");
		return new ResponseEntity<>(employees, HttpStatus.CREATED);
	}

	@GetMapping("/templates/employees")
	public ResponseEntity<List<Employee>> getStaticListOfEmployees() {
		List<Employee> employees = employeeService.getStaticEmployees();
		logger.info("Fetching all static list of employees");
		return new ResponseEntity<>(employees, HttpStatus.OK);
	}

	@GetMapping("/count")
	public long getCountOfEmployees(){
		long count = employeeService.getStaticEmployees().stream().count();
		return count;
	}

}
