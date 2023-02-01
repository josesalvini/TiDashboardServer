package com.tipre.dashboard.model.fileinfo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "files")
public class FileDB {
	// @Id
	// @GeneratedValue(generator = "uuid")
	// @GenericGenerator(name = "uuid", strategy = "uuid2")
	// private String id;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String type;

	@Lob
	private byte[] data;
	
	private long user;

	public FileDB(String name, String type, byte[] data, long user) {
		this.name = name;
		this.type = type;
		this.data = data;
		this.user = user;
	}
}
