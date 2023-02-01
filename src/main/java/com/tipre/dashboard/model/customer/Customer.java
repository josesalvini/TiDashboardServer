package com.tipre.dashboard.model.customer;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clientes")
public class Customer implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull 
	@Column(length = 30)
	@Size(min = 3, max = 30)
	private String firstname;
	@NotNull 
	@Column(length = 30)
	@Size(min = 3, max = 30)
	private String lastname;
	
	@NotNull 
	@Column(length = 11)
	@Size(min = 3, max = 11)
	private String documento;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private Type type;

	public Customer(String firstname, String lastname, String documento,Type type ){
		this.firstname = firstname;
		this.lastname = lastname;
		this.documento = documento;
		this.type = type;
	}
	
	
	private static final long serialVersionUID = -7255922475816577670L;
}
