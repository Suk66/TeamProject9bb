package com.mycompany.teamproject9.controller;

import com.mycompany.teamproject9.dto.Board;
import com.mycompany.teamproject9.dto.User;
import com.mycompany.teamproject9.repository.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardMapper boardMapper;

    // 📌 게시글 목록
    @GetMapping
    public String boardList(Model model) {
        List<Board> boardList = boardMapper.findAll();

        // 📌 [디버깅] 가져온 게시글 목록 확인
        for (Board board : boardList) {
            System.out.println("📌 [디버깅] 게시글 ID: " + board.getBoardId() + ", 제목: " + board.getTitle());
        }

        model.addAttribute("boards", boardList);
        return "board/board-list";
    }

    // 📌 게시글 작성 페이지 (GET 요청)
    @GetMapping("/write")
    public String showWriteForm(Model model, HttpSession session) {
        model.addAttribute("board", new Board()); // 빈 Board 객체 전달

        // ✅ 세션에서 User 객체 가져오기
        Object userObj = session.getAttribute("user");

        if (userObj == null) {
            System.out.println("📌 [디버깅] 세션에 사용자 정보 없음! (user == null)");
        } else if (userObj instanceof User) {
            User loggedInUser = (User) userObj;
            model.addAttribute("loggedInUser", loggedInUser);
            System.out.println("📌 [디버깅] 모델에 추가된 사용자 이름: " + loggedInUser.getName());
        } else {
            System.out.println("❌ [디버깅] 세션에 저장된 데이터 타입이 예상과 다름: " + userObj.getClass().getName());
        }

        return "board/board-write";  // 게시글 작성 페이지
    }




    // 📌 게시글 작성 요청 처리 (POST)
    @PostMapping("/write")
    public String insert(@ModelAttribute Board board, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser != null) {
            board.setWriter(loggedInUser.getEmail());  // ✅ DB에는 email 저장
        } else if (board.getWriter() == null || board.getWriter().isEmpty()) {
            return "redirect:/board/write?error=missing_writer";
        }

        System.out.println("📌 [디버깅] 새 게시글 작성! 제목: " + board.getTitle() + ", 작성자(이메일): " + board.getWriter());

        boardMapper.insert(board);
        return "redirect:/board";
    }




    // 📌 게시글 상세 보기
    @GetMapping("/{id}")
    public String boardDetail(@PathVariable("id") int boardId, Model model) {
        System.out.println("📌 [디버깅] 요청된 게시글 ID: " + boardId);

        Board board = boardMapper.findById(boardId);

        if (board == null) {
            System.out.println("❌ [오류] 게시글을 찾을 수 없음! ID: " + boardId);
            return "redirect:/board";
        }

        System.out.println("📌 [디버깅] 조회된 게시글 ID: " + board.getBoardId());

        model.addAttribute("board", board);
        return "board/board-detail";
    }

    // 📌 게시글 수정 페이지 (GET)
    @GetMapping("/edit/{id}")
    public String editBoard(
            @PathVariable("id") int boardId,
            HttpSession session,
            Model model) {

        Board board = boardMapper.findById(boardId);
        if (board == null) {
            return "redirect:/board"; // 게시글이 없으면 목록으로 이동
        }

        // ✅ 로그인한 사용자 정보 가져오기
        Object userObj = session.getAttribute("user");
        if (userObj instanceof User) {
            User loggedInUser = (User) userObj;

            System.out.println("📌 [디버깅] 로그인한 사용자 이메일: " + loggedInUser.getEmail());
            System.out.println("📌 [디버깅] 게시글 작성자 이메일: " + board.getWriter());

            // 🚀 **로그인한 사용자가 작성자일 경우 → 바로 수정 페이지로 이동**
            if (board.getWriter().equals(loggedInUser.getEmail())) {
                System.out.println("✅ [디버깅] 사용자 이메일이 일치! 바로 수정 페이지로 이동");
                model.addAttribute("board", board);
                return "board/board-edit";
            }
        }

        // ❌ **작성자가 아니거나 비로그인 상태 → 비밀번호 입력 페이지로 이동**
        System.out.println("❌ [디버깅] 비밀번호 입력 필요! 비밀번호 확인 페이지로 이동");
        return "redirect:/board/check-password/" + boardId;
    }


    // 📌 게시글 수정 요청 처리 (POST)
    @PostMapping("/edit/{id}")
    public String updateBoard(
            @PathVariable("id") int boardId,
            @ModelAttribute Board board) {
        board.setBoardId(boardId);  // ✅ URL에서 받은 boardId 설정

        int updatedRows = boardMapper.update(board);
        if (updatedRows > 0) {
            System.out.println("📌 [디버깅] 게시글 수정 완료! ID: " + boardId);
        } else {
            System.out.println("❌ [디버깅] 게시글 수정 실패! ID: " + boardId);
        }

        return "redirect:/board/" + boardId;  // ✅ 수정 완료 후 상세 페이지로 이동
    }


    @PostMapping("/delete/{id}")
    @ResponseBody  // ✅ JSON 응답을 반환하도록 변경
    public Map<String, Object> deleteBoard(@PathVariable("id") int boardId, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        System.out.println("📌 [디버깅] 삭제 요청 ID: " + boardId);

        Object userObj = session.getAttribute("user");
        if (!(userObj instanceof User)) {
            System.out.println("❌ [오류] 로그인한 사용자가 아님");
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return response;
        }

        User loggedInUser = (User) userObj;
        Board board = boardMapper.findById(boardId);

        if (board == null) {
            System.out.println("❌ [오류] 삭제하려는 게시글이 존재하지 않음!");
            response.put("success", false);
            response.put("message", "삭제하려는 게시글이 존재하지 않습니다.");
            return response;
        }

        if (!board.getWriter().equals(loggedInUser.getEmail())) {
            System.out.println("❌ [오류] 다른 사용자의 게시글을 삭제할 수 없음!");
            response.put("success", false);
            response.put("message", "다른 사용자의 게시글은 삭제할 수 없습니다.");
            return response;
        }

        int result = boardMapper.delete(boardId);
        if (result > 0) {
            System.out.println("✅ [디버깅] 게시글 삭제 완료! ID: " + boardId);
            response.put("success", true);
            response.put("message", "게시글이 삭제되었습니다.");
        } else {
            System.out.println("❌ [오류] 게시글 삭제 실패!");
            response.put("success", false);
            response.put("message", "게시글 삭제 실패! 다시 시도하세요.");
        }
        return response;
    }




    // 📌 비밀번호 입력 화면으로 이동
    @GetMapping("/check-password/{id}")
    public String showPasswordCheckPage(@PathVariable("id") int boardId, Model model) {
        model.addAttribute("boardId", boardId);
        return "board/password-check";  // 비밀번호 입력 화면
    }

    // 📌 비밀번호 확인 후 수정 페이지 이동
    @PostMapping("/check-password")
    public String checkPassword(
            @RequestParam("boardId") int boardId,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {

        // 게시글 정보 조회
        Board board = boardMapper.findById(boardId);
        if (board == null) {
            return "redirect:/board?error=not_found";  // 게시글이 없으면 목록으로 이동
        }

        User loggedInUser = (User) session.getAttribute("user");

        // ✅ 로그인한 사용자가 본인의 게시글을 수정하는 경우 → 비밀번호 없이 수정 가능
        if (loggedInUser != null && loggedInUser.getEmail().equals(board.getWriter())) {
            model.addAttribute("board", board);
            return "board/board-edit"; // 바로 수정 페이지로 이동
        }

        // ✅ 비로그인 사용자는 비밀번호 확인 후 수정 가능
        if (!board.getPassword().equals(password)) {
            return "redirect:/board/check-password/" + boardId + "?error=invalid_password";  // 비밀번호 틀림
        }

        // ✅ 비밀번호가 맞으면 수정 페이지로 이동
        model.addAttribute("board", board);
        return "board/board-edit";
    }

    @PostMapping("/check-password-before-delete")
    @ResponseBody
    public ResponseEntity<?> checkPasswordBeforeDelete(@RequestBody Map<String, String> data) {
        int boardId = Integer.parseInt(data.get("boardId"));
        String password = data.get("password");

        // 게시글 조회
        Board board = boardMapper.findById(boardId);
        if (board == null) {
            return ResponseEntity.status(404).body("게시글이 존재하지 않습니다.");
        }

        // 비밀번호 확인
        if (!board.getPassword().equals(password)) {
            return ResponseEntity.status(400).body("비밀번호가 틀렸습니다.");
        }

        // 비밀번호가 맞다면 삭제
        int result = boardMapper.delete(boardId);
        if (result > 0) {
            return ResponseEntity.ok(new HashMap<String, Object>() {{
                put("success", true);
                put("message", "게시글이 삭제되었습니다.");
            }});
        } else {
            return ResponseEntity.status(500).body("게시글 삭제 실패!");
        }
    }

    @PostMapping("/check-password-for-login-delete")
    @ResponseBody
    public ResponseEntity<?> checkPasswordForLoginDelete(@RequestBody Map<String, String> data, HttpSession session) {
        String password = data.get("password");

        // 로그인한 사용자 정보 가져오기
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            return ResponseEntity.status(400).body("로그인 필요");
        }

        // 비밀번호 확인
        if (!loggedInUser.getPassword().equals(password)) {
            return ResponseEntity.status(400).body("비밀번호가 틀렸습니다.");
        }

        // 게시글 삭제
        int boardId = Integer.parseInt(data.get("boardId"));
        Board board = boardMapper.findById(boardId);
        if (board == null) {
            return ResponseEntity.status(404).body("게시글이 존재하지 않습니다.");
        }

        int result = boardMapper.delete(boardId);
        if (result > 0) {
            return ResponseEntity.ok(new HashMap<String, Object>() {{
                put("success", true);
                put("message", "게시글이 삭제되었습니다.");
            }});
        } else {
            return ResponseEntity.status(500).body("게시글 삭제 실패!");
        }
    }

}