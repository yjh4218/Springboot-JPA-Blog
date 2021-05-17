package com.cos.blog.test;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

// @Controller 사용자가 요청 -> 응답(Html 파일)

//@RestController 사용자가 요청 -> 응답(Data)

@RestController
public class HttpControllerTest {

	private static final String TAG = "HttpControllerTest";
	
	//포트 변경 
	@GetMapping("/http/lombok")
	public String lombokTest() {
//		Member m = new Member(2, "jibho", "123124", "yjh0950@naver.com"); // @AllArgsConstructor 생성자 있는 상태로 생성
		Member m = Member.builder().username("ss123").password("465464").email("124321@1n2n1").build();
		System.out.println(TAG + " getter : " + m.getUsername());
		m.setUsername("5000");
		System.out.println(TAG + " setter : " + m.getUsername());
		
		return "lombokTest 완료";
//		Member m1 = new Member(2, TAG, TAG, TAG); // @AllArgsConstructor 생성자 있는 상태로 생성
//		Member m2=new Member(); //@NoArgsConstructor 빈생성자로 생성
	}
	
	//인터넷 브라우저 요청은 무조건 get만 가능하다.
	// http://localhost:8080/http/get(select)
	@GetMapping("/http/get")
	public String getTest(Member m) {//MessageConverter (스프링부트) 기능이 자동으로 설정
		return "get 요청 : " + m.getId() + ", username : " + m.getUsername() + ", " + m.getPassword() + ", " + m.getEmail();
	}
	
	// http://localhost:8080/http/post(insert)
	@PostMapping("/http/post") //mine 타입이 text/plain, application/json
	public String postTest(@RequestBody Member m) {//MessageConverter (스프링부트) 기능이 자동으로 설정
		return "post 요청 : " + m.getId() + ", username : " + m.getUsername() + ", " + m.getPassword() + ", " + m.getEmail();
	}
	
	// http://localhost:8080/http/put(update)
	@PutMapping("/http/put") //@@RequestBody 는 html에서 요청 받은 body 내용을 MessageConverter가 할당해줌 
	public String putTest(@RequestBody Member m) {
		return "put 요청 : " + m.getId() + ", username : " + m.getUsername() + ", " + m.getPassword() + ", " + m.getEmail();
	}
	
	// http://localhost:8080/http/delete(delete)
	@DeleteMapping("/http/delete")
	public String deleteTest() {
		return "delete 요청";
	}
}
