package com.cos.blog.controller.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.dto.ResponseDto;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;
//응답왔을 경우 처리함
@RestController
public class UserApiController {
	
	//db에 저장을 위한 연결
	@Autowired
	private UserService userService;	
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	//세션을 위함
	@Autowired
	private AuthenticationManager authenticattionManger;
	
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
	
	
	@PutMapping("/user")
	public ResponseDto<Integer> update(@RequestBody User user){
		
		userService.회원수정(user);
		//여기서 트랜잭션이 종료되기 때문에 db의 값은 변경 됨
		//하지만 세션값은 변경되지 않은 상태이기 때문에 우리가 직접 세션값을 변경해줄 것임.
		//세션등록 후 로그인 처리
		Authentication authentication = authenticattionManger.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1); //데이터 전달 될 경우 ResponseDto로 전달
	}
}
