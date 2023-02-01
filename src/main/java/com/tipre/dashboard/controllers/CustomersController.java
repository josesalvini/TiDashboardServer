package com.tipre.dashboard.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tipre.dashboard.dto.CustomersResponse;
import com.tipre.dashboard.model.customer.Customer;
import com.tipre.dashboard.repository.CustomerRepository;

@RestController
@RequestMapping("/api/v1/customers")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CustomersController {

	@Autowired
	CustomerRepository customerRepository;

	@GetMapping
	public ResponseEntity<CustomersResponse> getCustomers() {
		try {
			List<Customer> customers = customerRepository.findAll();

			if (customers.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(  
					CustomersResponse
					.builder()
					.count(customers.size())
					.customers(customers)
					.build() , HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(path = "{id}")
	public ResponseEntity<Customer> getCustomer(@Valid @PathVariable("id") Long id) {
		Optional<Customer> customer = customerRepository.findById(id);

		if (customer.isPresent()) {
			return new ResponseEntity<>(customer.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping
	public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) {
		try {
			Customer _customer = new Customer(customer.getFirstname(), customer.getLastname(), customer.getDocumento(),
					customer.getType());

			customerRepository.save(_customer);

			return new ResponseEntity<>(_customer, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping(path = "{id}")
	public ResponseEntity<HttpStatus> deleteCustomer(@Valid @PathVariable("id") Long id) {
		try {
			customerRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(path = "{id}")
	public ResponseEntity<Customer> updateCustomer(@Valid @PathVariable("id") Long id, @Valid @RequestBody Customer customer) {
		Optional<Customer> customerData = customerRepository.findById(id);

		if (customerData.isPresent()) {
			Customer _customer = customerData.get();
			
			_customer.setFirstname(customer.getFirstname());
			_customer.setLastname(customer.getLastname());
			_customer.setDocumento(customer.getDocumento());
			_customer.setType(customer.getType());
			return new ResponseEntity<>(customerRepository.save(_customer), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
