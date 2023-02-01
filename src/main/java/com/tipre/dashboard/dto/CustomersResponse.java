package com.tipre.dashboard.dto;

import java.util.List;

import com.tipre.dashboard.model.customer.Customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomersResponse {
	
	private Integer count;
	private List<Customer> customers;
	
}
