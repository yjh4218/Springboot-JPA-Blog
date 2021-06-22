<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@include file="../layout/header.jsp"%>

<div class="container">

	<button class="btn btn-secondary" onclick="history.back()">돌아가기</button>	
	<button id="btn-update" class="btn btn-warning">수정</button>
		<!-- 글작성자가 맞을 경우에만 글 삭제가 될 수 있도록 수정. principal은 PrincipalDetail의 user의 id를 지칭함 -->
		<c:if test="${board.user.id == principal.user.id}">
	<button id="btn-delete" class="btn btn-danger">삭제</button>
	</c:if>
	<br/><br/>
	<div>
		글 번호 : <span id="id"><i>${board.id} </i></span>
		작성자 : <span id="id"><i>${board.user.username} </i></span>
	</div>
	<br/>
	<div class="form-group">
		<h3>${board.title}</h3>
	</div>
	<hr />
	<div class="form-group">
		<div>${board.content}</div>
	</div>

</div>
<script src="/js/board.js"></script>
<%@include file="../layout/footer.jsp"%>
