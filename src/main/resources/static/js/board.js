let index = {
	init:function(){
		$("#btn-save").on("click", ()=>{ 
			this.save();
		});
		$("#btn-delete").on("click", ()=>{ 
			this.deleteById();
		});
		$("#btn-update").on("click", ()=>{ 
			this.update();
		});
		$("#btn-reply-save").on("click", ()=>{ 
			this.replySave();
		});
	},
	
	save:function(){
		let data={ //유저가 입력한 데이터 저장
			title:$("#title").val(),
			content:$("#content").val(),
		};
		
		$.ajax({
			//게시글 작성
			type:"POST",
			url:"/api/board", //UserApiController로 던짐(PostMapping)
			data: JSON.stringify(data), //http body data.
			contentType:"application/json; charset=utf-8", //body data가 어떤 타입인지(MIME)
			dataType:"json" //요청을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열(String Buffer)인데, 만약 생긴게 json이라면 javascript 오브젝트로 변경함
		}).done(function(resp){
			alert("글쓰기가 완료되었습니다.");
			location.href ="/";
		}).fail(function(error){
			alert(JSON.stringify(error));
		}); 
	},
	
	deleteById:function(){
		let id = $("#id").text(); // detail.jsp의 id값을 찾아옴

		$.ajax({
			//게시글 삭제
			type:"DELETE",
			url:"/api/board/" + id, //삭제할 게시글 던짐
			dataType:"json" //요청을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열(String Buffer)인데, 만약 생긴게 json이라면 javascript 오브젝트로 변경함
		}).done(function(resp){
			alert("삭제가 완료되었습니다.");
			location.href ="/";
		}).fail(function(error){
			alert(JSON.stringify(error));
		}); 
	},
	
	update:function(){
		let data={ //유저가 입력한 데이터 저장
			title:$("#title").val(),
			content:$("#content").val(),
		};
		
		let id=$("#id").val();
		console.log("여기 들어옴!!");
		
		$.ajax({
			//게시글 수정 진행
			type:"PUT",
			url:"/api/board/" + id, //UserApiController로 던짐(PostMapping)
			data: JSON.stringify(data), //http body data.
			contentType:"application/json; charset=utf-8", //body data가 어떤 타입인지(MIME)
			dataType:"json" //요청을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열(String Buffer)인데, 만약 생긴게 json이라면 javascript 오브젝트로 변경함
		}).done(function(resp){
			alert("글 수정이 완료되었습니다.");
			location.href ="/";
		}).fail(function(error){
			alert(JSON.stringify(error));
		}); 
	},
	
	replySave:function(){
		let data={ //유저가 입력한 데이터 저장
			userId:$("#userId").val(),
			boardId: $("#boardId").val(),
			content:$("#reply-content").val()
		};
		
		console.log(data);
		
		$.ajax({
			//게시글 작성
			type:"POST",
			url:"/api/board/" + data.boardId +"/reply", //UserApiController로 던짐(PostMapping)
			data: JSON.stringify(data), //http body data.
			contentType:"application/json; charset=utf-8", //body data가 어떤 타입인지(MIME)
			dataType:"json" //요청을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열(String Buffer)인데, 만약 생긴게 json이라면 javascript 오브젝트로 변경함
		}).done(function(resp){
			alert("댓글 작성이 완료되었습니다.");
			location.href ="/board/" + data.boardId;
		}).fail(function(error){
			alert(JSON.stringify(error));
		}); 
	},
	
	replyDelete:function(boardId, replyId){
		console.log(boardId, replyId);
		
		$.ajax({
			//게시글 작성
			type:"DELETE",
			url:"/api/board/" + boardId+"/reply/" + replyId, //UserApiController로 던짐(PostMapping)
			dataType:"json" //요청을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열(String Buffer)인데, 만약 생긴게 json이라면 javascript 오브젝트로 변경함
		}).done(function(resp){
			alert("댓글 삭제 성공");
			location.href ="/board/" + boardId;
		}).fail(function(error){
			alert(JSON.stringify(error));
		}); 
	},
}

index.init();