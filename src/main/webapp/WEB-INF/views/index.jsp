<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@include file="layout/header.jsp"%>

<div class="container">
	<!-- BoardControlller로부터 받은 Page의 데이터 값을 받아서 저장. content는 Page에서 정리해주는 데이터 값. -->
	<c:forEach var="board" items="${boards.content}">
		<div class="card m-2">
			<div class="card-body">
				<h4 class="card-title">${board.title}</h4>
				<a href="/board/${board.id}" class="btn btn-primary">상세보기</a>
			</div>
		</div>
	</c:forEach>
	<!-- Paging화 진행.  boards.number은 Page로부터 받은 데이터 중 현재 값을 말함-->
	<ul class="pagination justify-content-center">
		<!--  justify-content-center 가운데 정렬 justify-content-end 끝 정렬, justify-content-starter 앞 정렬-->
		<!-- jstl 조건문. choose는 참일때, otherwise는 거짓일때 -->
		<c:choose>
			<c:when test="${boards.first}">
				<li class="page-item disabled"><a class="page-link" href="?page=${boards.number-1}">Previous</a></li>	
			</c:when>
					
			<c:otherwise>
				<li class="page-item"><a class="page-link" href="?page=${boards.number-1}">Previous</a></li>
			</c:otherwise>
		</c:choose>
		
		<c:choose>
			<c:when test="${boards.last}">
				<li class="page-item disabled"><a class="page-link" href="?page=${boards.number+1}">Next</a></li>	
			</c:when>
					
			<c:otherwise>
				<li class="page-item"><a class="page-link" href="?page=${boards.number+1}">Next</a></li>
			</c:otherwise>
		</c:choose>
		
		
	</ul>

</div>


<%@include file="layout/footer.jsp"%>
