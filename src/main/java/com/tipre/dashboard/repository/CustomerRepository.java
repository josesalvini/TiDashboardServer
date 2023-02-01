package com.tipre.dashboard.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tipre.dashboard.model.customer.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	Optional<Customer> findByDocumento(String documento);

	Boolean existsByDocumento(String documento);

}
