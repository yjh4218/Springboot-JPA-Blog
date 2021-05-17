package com.cos.blog.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // getter, setter 모두 포함
//@AllArgsConstructor // 생성자 생성
@NoArgsConstructor // 빈생성자
public class Member {
	private int id;
	private String username;
	private String password;
	private String email;
	
	@Builder //builder를 통해서 생성자를 통해 객체 생성 시 내가 수정하고 싶은 데이터만 수정하여 데이터 입력 가능하며, 순서에 상관없이 데이터 입력가능
	public Member(int id, String username, String password, String email) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
	}	

	

}
