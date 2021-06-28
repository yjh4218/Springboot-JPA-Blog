package com.cos.blog.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.model.KakaoProfile;
import com.cos.blog.model.OAuthToken;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

// 인증이 안된 사용자들이 출입할 수 있는 경로룰 /auth 허용
//그냥 주소가 / 이면 index.jsp 허용
//static 이하에 있는 js파일 /css, /image 하용

@Controller
public class UserController {
	
	
	@Value("${cos.key}")
	private String cosKey;

	// 세션을 위함
	@Autowired
	private AuthenticationManager authenticattionManger;

	@Autowired
	private UserService userService;

	@GetMapping("/auth/joinForm")
	public String joinForm() {

		return "user/joinForm";
	}

	@GetMapping("/auth/loginForm")
	public String loginForm() {

		return "user/loginForm";
	}

	@GetMapping("/user/updateForm")
	public String updateForm(@AuthenticationPrincipal PrincipalDetail principal) {
		return "user/updateForm";
	}

	@GetMapping("/auth/kakao/callback")
	public String kakaoCallback(String code) {// @ResponseBody를 붙이면 data를 리턴해주는 컨트롤러 함수. 응답을 받음

		// POST 방식으로 key=value 데이터를 요청(카카오쪽으로)
		// Retrofit2 -> 안드로이드에서 많이 사용함
		// OkHttp
		RestTemplate rt = new RestTemplate();

		// HttpHeader 오브젝트 생성
		HttpHeaders headers = new HttpHeaders();
		// content-type을 담는 것은 내가 전달할 데이터가 key-value 형태라는 것을 알려주는 것임
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		// 카카오 토큰을 던지기 위해서 데이터 담음
		// HttpBody 생성
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("grant_type", "authorization_code");
		param.add("client_id", "dbcd58874436edf442ce94def0e2f09e");
		param.add("redirect_uri", "http://localhost:8000/auth/kakao/callback");
		param.add("code", code);

		// HTTPHeader와 HttpBody를 하나의 오브젝트로 담기 -> 하나로 담는 이유는 rt.exchange가 HttpEntity로 받게
		// 되어 있음
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(param, headers);

		// 토큰 발급 주소 담음
		// Http 요청하기 - Post 방식으로 - 그리고 response 변수의 응답 받음
		ResponseEntity<String> response = rt.exchange("https://kauth.kakao.com/oauth/token", // 토큰 받을 주소
				HttpMethod.POST, // 토큰 전송 방식
				kakaoTokenRequest, // header와 body 데이터
				String.class // 응답 받을 데이터
		);

		// 카카오 접근 토큰 받기
		// Gson, Json Simple, ObjectMapper
		ObjectMapper objectMapper = new ObjectMapper();
		OAuthToken oauthToken = null;
		try {
			oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		System.out.println("카카오 엑세스 토큰 : "+oauthToken.getAccess_token());

		// 사용자 프로필 조회
		RestTemplate rt2 = new RestTemplate();

		// HttpHeader 오브젝트 생성
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer " + oauthToken.getAccess_token());
		// content-type을 담는 것은 내가 전달할 데이터가 key-value 형태라는 것을 알려주는 것임
		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		// HTTPHeader와 HttpBody를 하나의 오브젝트로 담기 -> 하나로 담는 이유는 rt.exchange가 HttpEntity로 받게
		// 되어 있음
		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 = new HttpEntity<>(headers2);

		// 토큰 발급 주소 담음
		// Http 요청하기 - Post 방식으로 - 그리고 response 변수의 응답 받음
		ResponseEntity<String> response2 = rt2.exchange("https://kapi.kakao.com/v2/user/me", // 토큰 받을 주소
				HttpMethod.POST, // 토큰 전송 방식
				kakaoProfileRequest2, // header와 body 데이터
				String.class // 응답 받을 데이터
		);

//		System.out.println("카카오 정보 조회 : "+response2.getBody());

		ObjectMapper objectMapper2 = new ObjectMapper();
		KakaoProfile kakaoProfile = null;
		try {
			kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// User 오브젝트 : username, password, email
		System.out.println("카카오 아이디(번호) : " + kakaoProfile.getId());
		System.out.println("카카오 email : " + kakaoProfile.getKakao_account().getEmail());

		System.out.println("블로그서버 유저네임 : " + kakaoProfile.getKakao_account().getEmail() + "_" + kakaoProfile.getId());
		System.out.println("블로그서버 이메일 : " + kakaoProfile.getKakao_account().getEmail());

		// 임시패스워드 -> 중복되지 않는 어떤 특정 값을 만들어내는 알고리즘
//		UUID uuidPassword = UUID.randomUUID();
//
//		System.out.println("블로그서버 패스워드 : " + uuidPassword);

		User kakaoUser = User.builder()
				.username(kakaoProfile.getKakao_account()
				.getEmail() + "_" + kakaoProfile.getId())
				.password(cosKey)
				.oauth("kakao")
				.email(kakaoProfile.getKakao_account().getEmail()).build();

		// 가입자인지, 비가입자인지 확인 후 처리 필요
		User originUser = userService.회원찾기(kakaoUser.getUsername());

		if (originUser.getUsername() == null) {
			System.out.println("기존회원이 아니기에 회원가입 진행합니다.");
			userService.회원가입(kakaoUser);
		}

		System.out.println("자동 로그인 진행함");
		// 로그인 처리
		Authentication authentication = authenticattionManger.authenticate(
				new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), cosKey));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		System.out.println("자동 로그인 완료");
		return "redirect:/";
	}
}
