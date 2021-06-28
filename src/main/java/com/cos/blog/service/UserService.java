package com.cos.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

//스프링이 컴포넌트 스캔을 통해서 Bean에 등록을 해줌. IOC 해줌
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Transactional
	public User 회원찾기(String username) {

		User user = userRepository.findByUsername(username).orElseGet(() -> {
			return new User();
		});
		return user;
	}

	@Transactional
	public void 회원가입(User user) {
		// 회원이 입력한 password
		String rawPassword = user.getPassword();
		// hash화 한 password
		String encPassowrd = encoder.encode(rawPassword);
		user.setPassword(encPassowrd);
		user.setRole(RoleType.USER);
		userRepository.save(user);
	}

	@Transactional
	public void 회원수정(User user) {
		// 수정시에는 영속성 컨텍스트 User 오브젝트를 영속화시키고, 영속화된 User 오브젝트를 수정
		// select를 해서 User 오브젝트를 DB로부터 가져오는 이유는 영속화를 하기 위해서임.
		// 영속화된 오브젝트를 변경하면 자동을 DB에 update 문을 날려줌
		User persistance = userRepository.findById(user.getId()).orElseThrow(() -> {
			return new IllegalArgumentException("회원찾기 실패");
		});

		//aouth로 로그인한 사용자들은 비밀번호 수정을 할 수 없음
		if (persistance.getOauth() == null || persistance.getOauth().equals("")) {
			// 회원정보 업데이트
			String rawPassword = user.getPassword();
			String encPassword = encoder.encode(rawPassword);
			persistance.setPassword(encPassword);
		}

		persistance.setEmail(user.getEmail());
		
		// 회원수정 함수 종료시 = 서비스 종료 = 트랜잭션 종료 = commit이 자동으로 됨.
		// 영속화된 persistance 객체의 변화가 감지되면 더티체킹이 되어 update문을 날림
	}

}
