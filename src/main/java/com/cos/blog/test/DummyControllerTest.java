package com.cos.blog.test;

import java.util.List;
import java.util.function.Supplier;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.*;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

//html 파일이 아니라 data를 리턴해주는 controller = RestController
@RestController
public class DummyControllerTest {

	//실행 시 UserRepository 타입으로 메모리에 띄워줌 
	@Autowired //의존성 주입(DI)
	private UserRepository userRepository;
	
	
	//save 함수는 id를 전달하지 않으면 insert를 하고
	//save 함수는 id를 전달하면 해당 id에 대한 데이터가 있으면 update를 진행함
	//save 함수는 id를 전달하면 해당 id에 대한 데이터가 없으면 insert를 진행함
	//email, password
	
	@DeleteMapping("/dummy/user/{id}")
	public String deleteUser(@PathVariable int id) {
		try {
			userRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			// TODO: handle exception
			return "삭제실패하였습니다. 해당 id는 DB에 없습니다.";
		}
				
		return "삭제되었습니다. id : " + id;
	}
	
	@Transactional //함수 종료시에 자동 commit이 됨. 함수 종료시 변경감지 하여 변경된 데이터 확인 후 수정 commit 진행 -> 더티체킹
	@PutMapping("/dummy/user/{id}")
	public User updateUser(@PathVariable int id,@RequestBody User requestUser) { //json 데이터를 요청 -> Java ObjectMessageConverter의 Jackson라이브러리가 변환해서 받아줌
		System.out.println("id : " + id);
		System.out.println("password : " + requestUser.getPassword());
		System.out.println("email : " + requestUser.getEmail());
		
		// save는 인서트에서만 사용한다.
		// 영속화가 됨.
		User user = userRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("수정에 실패하였습니다.");
		});
		
		user.setPassword(requestUser.getPassword());
		user.setEmail(requestUser.getEmail());
		
//		userRepository.save(user); 
		
		//더티체킹
		return user;
	}
	
	
	@GetMapping("/dummy/users")
	public List<User> list(){
		return userRepository.findAll();
	}
	
	// 한 페이당 2건에 데이터를 리턴받아 볼 예정
	@GetMapping("/dummy/user")
	public List<User> pageList(@PageableDefault(size=2, sort="id", direction=Sort.Direction.DESC) Pageable pageable){
		Page<User> pageUser = userRepository.findAll(pageable); 
		
				
		List<User> users = pageUser.getContent(); //getContent()는 List 값으로 반환한다.
		return users;
	}
	
	
	//{id}주소로 파마레터를 전달 받을 수 있음
	//http://localhost:8000/blog/dummy/user/3
	@GetMapping("/dummy/user/{id}")
	public User detail(@PathVariable int id) {
		//user/4을 찾으면 내가 DB에서 못 찾으면 user = null이 됨
		//return할 때 null이 되면 문제가 발생함
		//optional로 너의 User 객체를 가져올테니 null인지 아닌지 판단해서 return해
		//userRepository.findById(id).get() 는 절대 null이 없다는 가정 하에 사용
		//userRepository.findById(id).orElseGet(new Supplier<User>()) 없으면 객체 하나 만들어서 넣어줘
//		User user = userRepository.findById(id).orElseGet(new Supplier<User>() {
//			//Supplier 인터페이스를 이용해 익명의 객체 생성. 인터페이스는 new를 할 수 없기 때문에 익명 Class로 new 해야 함
//			@Override
//			public User get() {
//				// TODO Auto-generated method stub
//				return new User();
//			}
//		});
		
		// 권장되는 방법. 찾는게 없을 경우 예외처리
		User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
		
			@Override
			public IllegalArgumentException get() {
				// TODO Auto-generated method stub
				return new IllegalArgumentException("해당 유저는 없습니다. id : " + id);
			}
		});
		
		//요청 : 웹 브라우저
		//user 객체 = 자바 오브젝트
		//변환(웹브라우저가 이해할 수 있는 데이터) -> json 
		//스프링부트 = MessageConveter라는 애가 응답시에 자동 작동
		//만약에 자바 오브젝트를 리턴하게 되면 MessageConverter가 Jackson 라이브러리를 호출해서
		//user 오브젝트를 json으로 변환해서 브라우저에게 던져줌
		return user;
	}
	
	//http://localhost:8080/blog/dummy/join(요청)
	//http의 body에 username,password,email 데이터를 가지고 요청
	@PostMapping("/dummy/join")
//	public String join(String username, String password, String email) { // key=value 타입으로 데이터를 전달받음.(약속된 규칙)
	public String join(User user) {	//오브젝트로 데이터를 받을 수 있음
		System.out.println("username : " + user.getUsername());
		System.out.println("password : " + user.getPassword());
		System.out.println("email : " + user.getEmail());
		
		//default 값을 회원가입때 넣을 수 있도록 함
		user.setRole(RoleType.USER);
		
		//DB에 저장
		userRepository.save(user);
		return "회원가입이 완료되었습니다.";
	}
}
