package com.tipre.dashboard.dto;

import java.util.List;

import com.tipre.dashboard.model.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsersResponse {

	private Integer count;
	private List<User> users;
}
