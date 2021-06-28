package com.cos.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cos.blog.service.BoardService;


@Controller
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	@GetMapping({"", "/"})
	public String index(Model model, @PageableDefault(size=3, sort="id", direction=Sort.Direction.DESC) Pageable pageable) { //컨트롤로에서 세션 접근 방법
		// /WEB-INF/views/index.jsp
		
		model.addAttribute("boards", boardService.글목록(pageable));
		return "index"; //리턴할때 viewResolver 작동함. 해당 index 페이지로 model의 정보를 들고 이동함.
	}
	
	@GetMapping("/board/{id}")
	public String finById(@PathVariable int id, Model model) {
		System.out.println("게시판 id : " + id);
		model.addAttribute("board", boardService.글상세보기(id));
		return "board/detail";
	}
	
	
	// model은 해당 데이터를 view까지 들고 이동함
	@GetMapping("/board/{id}/updateForm")
	public String updateForm(@PathVariable int id, Model model) {
		model.addAttribute("board", boardService.글상세보기(id));
		return "board/updateForm";
	}
	
	@GetMapping("/board/saveForm")
	public String saveForm() { //컨트롤로에서 세션 접근 방법
		return "/board/saveForm";
	}
}
