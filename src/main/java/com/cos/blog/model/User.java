package com.cos.blog.model;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// ORM -> JAVA(다른 언어 포함) Object -> 테이블로 매핑해주는 기술
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // Builder 패턴
//ORM -> JAVA(다른언어) Object -> 테이블로 매핑해주는 기술
@Entity //User 클래스가 MySql 에 테이블이 생성이 된다.
//@DynamicInsert //insert 할 때 null인 데이터는 제외하고 넣는다.
public class User {

	@Id // primarykey
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 프로젝트에서 연결된 DB의 넘버링 전략을 따라간다.
	private int id; // 오라클은 시퀀스, mysql은 auto_increment
	
	@Column(nullable = false, length = 100, unique = true)
	private String username; //아이디
	
	@Column(nullable = false, length = 100) //해쉬로 비밀번호 암호화 예정
	private String password; //비밀번호
	
	@Column(nullable = false, length = 50)
	private String email;
		

	// DB
//	@ColumnDefault("'user'")
	@Enumerated(EnumType.STRING)
	private RoleType role;// Enum을 쓰는게 좋다.(Enum을 사용할 경우 도메인 생성 가능. 도메인은 프로그램에서 범위 설정하는 것) 
	// admin, user, manager 등의 권한 부여 (String이기 때문에 실수 할 수 있음)
	// RoleType 으로 ADMIN, USER 만 사용할 수 있도록 강제
	
	
	private String oauth; //kakao, google 등으로 로그인 한 사람인지 확인
	
	@CreationTimestamp //시간이 자동입력(현재시간)
	private Timestamp createDate;
	
}
