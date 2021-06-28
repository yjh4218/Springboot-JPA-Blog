package com.cos.blog.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
	private int id;
	
	@Column(nullable = false, length = 100)
	private String title;
	
	@Lob // 대용량 데이터
	private String content; //섬머노트 라이브러리 <heml> 태그가 섞여서 디자인 됨.
	
//	@ColumnDefault("0")
	private int count; //조회수
	
	//fetchType.Eager 는 select하면 무조건 가져옴
	@ManyToOne(fetch = FetchType.EAGER) // board = Many, user = one. 한 명의 유저는 여러개의 보드를 사용 가능. board = one, user = one. 한 명의 유저는 한 개의 보드를 사용할 수 있다. 
	@JoinColumn(name="userId")
	private User user; //DB는 오브젝트를 저장할 수 없다. FK, 자바는 오브젝트를 저장할 수 있다.
	// 원래 FK 값인 int userID 를 저장해야하나, JPA에서는 객체를 사용가능 

	//db에 반영 안됨. select하기 위해 존재
	//fetchType.Lazy 는 select하면 필요시 가져옴
	@OneToMany(mappedBy = "board",fetch = FetchType.EAGER, cascade = CascadeType.REMOVE) // 하나의 게시글은 여러개의 답변을 갖고 올 수 있음. mappedBy 는 연관관계의 주인이 아니다.(DB에 컬럼을 만들지 마세요). 조인의 데이터 저장.  
	@JsonIgnoreProperties({"board"})
	@OrderBy("id desc")
	//FK를 가지게 될 경우 원자성이 깨짐(1원칙 위배)
	private List<Reply> replys;
	
	@CreationTimestamp
	private Timestamp createDate;
	
}
