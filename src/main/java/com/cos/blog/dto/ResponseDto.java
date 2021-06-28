package com.cos.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//응답에 대한 데이터 저장
//계층간의 데이터 교환을 위한 데이터 객체. 데이터 저장소
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDto<T> {

	int status;
	T data;
}
