package com.cos.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blog.dto.ReplySaveRequestDto;
import com.cos.blog.model.Board;
import com.cos.blog.model.Reply;
import com.cos.blog.model.User;
import com.cos.blog.repository.BoardRepository;
import com.cos.blog.repository.ReplyRepository;
import com.cos.blog.repository.UserRepository;

//스프링이 컴포넌트 스캔을 통해서 Bean에 등록을 해줌. IOC 해줌
@Service
public class BoardService {

	@Autowired
	private BoardRepository boardRepository;

	@Autowired
	private ReplyRepository replyRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	
	@Transactional
	public void 글쓰기(Board board, User user) {
		board.setCount(0);
		board.setUser(user);
		System.out.println("User data 입니다." + user);
		System.out.println("board data 입니다." + board);
		boardRepository.save(board);
	}
	
	@Transactional(readOnly = true)
	public Page<Board> 글목록(Pageable pageable) {
		return boardRepository.findAll(pageable);
	}
	
	@Transactional(readOnly = true)
	public Board 글상세보기(int id) {
		return boardRepository.findById(id)
				.orElseThrow(()->{
					return new IllegalArgumentException("글 상세보기 실패 : 아이디를 찾을 수 없습니다.");
				});
	}
	
	@Transactional
	public void 글삭제하기(int id) {
		boardRepository.deleteById(id);
	}
	
	@Transactional
	public void 글수정하기(int id, Board requestBoard) {
		System.out.println("boardservice 들어옴!");
		Board board = boardRepository.findById(id)
				.orElseThrow(()->{
					return new IllegalArgumentException("글 찾기 실패 : 아이디를 찾을 수 없습니다.");
				}); //영속화 완료
		board.setTitle(requestBoard.getTitle());
		board.setContent(requestBoard.getContent());
		// 해당 함수 종료시에(service가 종료될 때) 트랜잭션이 종료됨. 이때 더티체킹 진행함->자동 업데이트가 됨. db flush(commit)  
	}
	
	//DTO 사용하지 않을 경우
//	@Transactional
//	public void 댓글쓰기(User user, int boardId, Reply requestReply) {
//		
//		Board board = boardRepository.findById(boardId)
//				.orElseThrow(()->{
//					return new IllegalArgumentException("댓글 쓰기 실패. 게시글 id를 찾을 수 없습니다.");
//				});
//		
//		requestReply.setUser(user);
//		requestReply.setBoard(board);
//		
//		replyRepository.save(requestReply);
//	}
	
	//DTO 사용할 경우
	@Transactional
	public void 댓글쓰기(ReplySaveRequestDto replySaveRequestDto) {
//		User user = userRepository.findById(replySaveRequestDto.getUserId())
//				.orElseThrow(()->{
//					return new IllegalArgumentException("댓글 쓰기 실패. 유저 id를 찾을 수 없습니다.");
//				}); // 영속화 완료
//		
//		Board board = boardRepository.findById(replySaveRequestDto.getBoardId())
//				.orElseThrow(()->{
//					return new IllegalArgumentException("댓글 쓰기 실패. 게시글 id를 찾을 수 없습니다.");
//				}); // 영속화 완료
		
//		Reply relpy = Reply.builder()
//				.user(user)
//				.board(board)
//				.content(replySaveRequestDto.getContent())
//				.build();
		
//		replyRepository.save(relpy);
		
		// native query 사용해서 데이터 넣기. 영속화를 하지 않아도 된다.
		replyRepository.mSave(replySaveRequestDto.getUserId(), replySaveRequestDto.getBoardId(), replySaveRequestDto.getContent());
		
	}
	
	@Transactional
	public void 댓글삭제(int replyId) {
		replyRepository.deleteById(replyId);
	}
	
}
