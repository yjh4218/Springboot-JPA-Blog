package com.cos.blog.controller.api;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.dto.ResponseDto;
import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;
//응답왔을 경우 처리함
@RestController
public class UserApiController {
	
	//db에 저장을 위한 연결
	@Autowired
	private UserService userService;	
	
	//요청받은 데이터
	//user.js 에서 데이터 요청이 올 경우 user.js의 user 정보를 전달 받음. 그 후 ResponseDto에 결과 값을 
	@PostMapping("/auth/joinProc")
	public ResponseDto<Integer> save(@RequestBody User user) {//username, password, email
		System.out.print("UserApiController에서 save 호출함");
		
		
		//실제로 DB에 insert하고 결과 값을 전달받음
		userService.회원가입(user);
		
		//결과값을 DTO에 저장 후 user.js에 반환
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1); //데이터 전달 될 경우 ResponseDto로 전달
	}
	
}
