package com.cos.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cos.blog.model.User;

// DAO 역할
// 자동으로 bean으로 등록이 된다.
@Repository //생략가능
public interface UserRepository extends JpaRepository<User,Integer>{

	
}
