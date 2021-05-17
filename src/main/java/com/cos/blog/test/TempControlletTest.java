package com.cos.blog.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//데이터 리턴이 아닌 파일 리턴. RestContoller 는 문자 그대로 반환. Controller는 파일을 찾아 리턴
@Controller
public class TempControlletTest {

	//http://localhost:8000/blog/temp/home
	@GetMapping("/temp/home")
	public String tempHome() {
		System.out.println("tempHome()");
		// 파일리턴 기본경로 : src/main/resources/static
		// 리턴명을 : /home.html 을 해야함
		// 풀경로 : src/main/resoutces/static/home.html
		return "/home.html";
	}
	
	@GetMapping("/temp/img")
	public String tempImg() {
		return "/a.png";
	}
	
	@GetMapping("/temp/jsp")
	public String tempJsp() {
//		prefix: /WEB-INF/views/
//	    suffix: .jsp
		//풀경로 : /WEB-INF/views/test.jsp
		return "test";
	}
}
