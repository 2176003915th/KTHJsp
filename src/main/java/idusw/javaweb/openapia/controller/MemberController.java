package idusw.javaweb.openapia.controller;

import idusw.javaweb.openapia.model.MemberDTO;
import idusw.javaweb.openapia.model.ProjectDTO;
import idusw.javaweb.openapia.util.ConnectionManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "memberController", urlPatterns = {"/members/post", "/members/post-form", "/members/list", "/members/update",
        "/members/delete", "/members/get-list", "/members/login-form", "/members/login" ,
        "/members/logout", "/members/get-one" ,"/members/desc", "/members/search" ,"/members/listdelete"})
public class MemberController extends HttpServlet {
    ConnectionManager connectionManager = ConnectionManager.getInstance();
    MemberDTO memberDTO = null;
    List<MemberDTO> memberDTOList = null;
    int cnt = 0;

    private MemberDTO setRsToDTO(ResultSet rs) throws SQLException { //값을 보여주거나 조회할때 이용
        memberDTO = new MemberDTO();
        memberDTO.setMid(rs.getLong("mid"));
        memberDTO.setFullName(rs.getString("full_name"));
        memberDTO.setEmail(rs.getString("email"));
        memberDTO.setPw(rs.getString("pw"));
        memberDTO.setZipcode(rs.getString("zipcode"));;
        memberDTO.setRegTimeStamp(rs.getString("start_timestamp"));
        return memberDTO;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        process(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        process(request, response);
    }

    public void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int index = request.getRequestURI().lastIndexOf("/");//URI의 마지막 슬래시 위치를 반환
        String command = request.getRequestURI().substring(index + 1);//index 뒤의 문자열부터 command에 저장

        HttpSession session = request.getSession(); // HttpSession 유형의 객체가 존재하는 경우 가져오고, 존재하지않는 경우 생성함,

        String sql = null;
        String tableName = "mb_a202012007";
        //요청의 type에 따른 작업 분리
        if(command.equals("post")) {
            memberDTO = new MemberDTO();
            memberDTO.setFullName(request.getParameter("full-name"));
            memberDTO.setEmail(request.getParameter("email"));
            memberDTO.setZipcode(request.getParameter("zipcode"));
            String pw1 = request.getParameter("pw1");
            String pw2 = request.getParameter("pw2");

            if (pw1.equals(pw2)) {
                    memberDTO.setPw(pw1);
            } else {
                System.out.println("비밀번호 불일치");
            }

            sql = " insert into " + tableName + "(full_name, email, pw , zipcode)" + " values(?,?,?,?) ";

            try ( Connection conn = connectionManager.getConnection(); //db값을 수정 입력 이면 2줄
                  PreparedStatement pstmt = conn.prepareStatement(sql);) {
                    pstmt.setString(1, memberDTO.getFullName());
                    pstmt.setString(2, memberDTO.getEmail());
                    pstmt.setString(3, memberDTO.getPw());
                    pstmt.setString(4, memberDTO.getZipcode());
                    cnt = pstmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if(cnt >= 1) {
                    request.setAttribute("dto", memberDTO);
                    request.getRequestDispatcher("/members/success.jsp").forward(request, response);
                } else {
                    request.setAttribute("message", "회원가입 실패");
                    request.getRequestDispatcher("/errors/fail.jsp").forward(request, response); //회원가입 실패 view 호출
                }
            }
        }
        else if(command.equals("get-list")){
            memberDTOList = new ArrayList<>();
            sql = "select * from " + tableName;
            try ( Connection conn = connectionManager.getConnection(); //값 그냥 조회는 이 3줄
                  Statement stmt = conn.createStatement();// 문장 질의 처리 변환
                  ResultSet rs = stmt.executeQuery(sql);) {

                while(rs.next()){
                    MemberDTO member = setRsToDTO(rs);
                    memberDTOList.add(member);
                }
            } catch(SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if (memberDTOList != null) {
                    request.setAttribute("dtoList", memberDTOList);
                    request.getRequestDispatcher("../members/list.jsp").forward(request, response);
                } else {
                    request.setAttribute("message", "멤버 조회 실패");
                    request.getRequestDispatcher("/errors/fail.jsp").forward(request, response); //회원가입 실패 view 호출
                }
            }
        }
        else if(command.equals("post-form")) {
            request.getRequestDispatcher("../members/register.jsp").forward(request, response);
        }
        else if(command.equals("login-form")) {
            request.getRequestDispatcher("../members/login.jsp").forward(request, response);
        }
        else if(command.equals("login")){
            String email = request.getParameter("email");
            String pw = request.getParameter("pw1");

            sql = " select * from " + tableName + " where email = (?)" + " and pw = (?)";
            try (Connection conn = connectionManager.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql);) {
                pstmt.setString(1, email);
                pstmt.setString(2, pw);
                ResultSet rs = pstmt.executeQuery();
                if(rs.next()){
                    memberDTO = new MemberDTO();
                    memberDTO = setRsToDTO(rs);
                }
            } catch(SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if(memberDTO != null) {
//                    boolean isAdmin = false;
//                    if (memberDTO.getFullName() != null) {
//                        isAdmin = memberDTO.getFullName().equals("admin");
//                    }
//                    session.setAttribute("ooAdmin", isAdmin);
                    //request.setAttribute("dto", m);
                    session.setAttribute("dto", memberDTO);
                    request.getRequestDispatcher("../main/index.jsp").forward(request, response);
                } else {
                    request.setAttribute("message", "로그인 실패");
                    request.getRequestDispatcher("/errors/fail.jsp").forward(request, response);
                }
            }
        }
        else if(command.equals("get-one")){
            memberDTO = new MemberDTO();
            memberDTO.setMid(Long.valueOf(request.getParameter("mid")));
            sql = " select * from " + tableName + " where mid =" + memberDTO.getMid();

            try (Connection conn = connectionManager.getConnection(); //값 그냥 조회는 이 3줄
                 Statement stmt = conn.createStatement();// 문장 질의 처리 변환
                 ResultSet rs = stmt.executeQuery(sql);){
                if(rs.next()){
                    memberDTO = setRsToDTO(rs);
                }
            } catch(SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if(memberDTO != null) {
                    //request.setAttribute("dto", m);
                    session.setAttribute("dto", memberDTO);
                    request.getRequestDispatcher("../members/detail.jsp").forward(request, response);
                } else {
                    request.setAttribute("dto", "로그인 실패");
                    request.getRequestDispatcher("../errors/fail.jsp").forward(request, response);//로그인 실패
                }
            }
        }
        else if(command.equals("update")){
            memberDTO = new MemberDTO();
            memberDTO.setMid(Long.valueOf(request.getParameter("mid"))); // String -> Long 값으로 변환하여 가져오기
            memberDTO.setFullName(request.getParameter("full-name"));
            memberDTO.setEmail(request.getParameter("email"));
            memberDTO.setPw(request.getParameter("pw"));
            memberDTO.setZipcode(request.getParameter("zipcode"));
            sql = " update " + tableName + " set full_name = (?), pw = (?), zipcode = (?)" + " where mid = (?)";
            try (Connection conn = connectionManager.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql);){
                pstmt.setString(1, memberDTO.getFullName()); // placeholder에 매칭하여 대체됨
                pstmt.setString(2, memberDTO.getPw());
                pstmt.setString(3, memberDTO.getZipcode());
                pstmt.setLong(4, memberDTO.getMid()); // Auto Boxing / Unboxing : long -> Long, Long -> long
                cnt = pstmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if(cnt > 0) {
                    request.getRequestDispatcher("../members/get-one?mid=" + memberDTO.getMid()).forward(request, response);
                } else {
                    request.getRequestDispatcher("../errors/fail.jsp").forward(request, response);
                }
            }
        }
        else if(command.equals("delete")){
            memberDTO = new MemberDTO();
            memberDTO.setMid(Long.valueOf(request.getParameter("mid")));
             // String -> Long 값으로 변환하여 가져오기
            sql = " delete from " + tableName + " where mid = (?)";
            try (Connection conn = connectionManager.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql);) {
                pstmt.setLong(1, memberDTO.getMid()); // Auto Boxing / Unboxing : long -> Long, Long -> long
                cnt = pstmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if(cnt > 0) {
                    session.invalidate();
                    request.getRequestDispatcher("../main/index.jsp").forward(request, response);
                   // response.sendRedirect("https://www.naver.com"); 상관없이 이동만 시킴
                } else {
                    request.getRequestDispatcher("../errors/fail.jsp").forward(request, response);
                }
            }
        }
        else if(command.equals("listdelete")){
            memberDTO = new MemberDTO();
            memberDTO.setMid(Long.valueOf(request.getParameter("mid")));
            // String -> Long 값으로 변환하여 가져오기
            sql = " delete from " + tableName + " where mid = (?)";
            try (Connection conn = connectionManager.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql);) {
                pstmt.setLong(1, memberDTO.getMid()); // Auto Boxing / Unboxing : long -> Long, Long -> long
                cnt = pstmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if(cnt > 0) {
                    response.sendRedirect("../members/get-list");
                    // response.sendRedirect("https://www.naver.com"); 상관없이 이동만 시킴
                } else {
                    request.getRequestDispatcher("../errors/fail.jsp").forward(request, response);
                }
            }
        }

        else if(command.equals("logout")){
            session.invalidate();
            response.sendRedirect("../main/index.jsp"); //request 객체 전달이 필요없는 경우
        }
        else if(command.equals("desc")) {
            memberDTOList = new ArrayList<>();
            sql = "select * from " + tableName + " ORDER BY full_name DESC";
            try(Connection conn = connectionManager.getConnection(); //값 그냥 조회는 이 3줄
                Statement stmt = conn.createStatement();// 문장 질의 처리 변환
                ResultSet rs = stmt.executeQuery(sql);) {
                while(rs.next()) {
                    MemberDTO member = setRsToDTO(rs);
                    memberDTOList.add(member);
                }
            } catch(SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if(memberDTOList != null) {
                    request.setAttribute("dtoList", memberDTOList);
                    request.getRequestDispatcher("../members/list.jsp").forward(request, response);
                } else {
                    request.setAttribute("message", "프로젝트 조회실패");
                    request.getRequestDispatcher("../errors/fail.jsp").forward(request, response);
                }
            }
        }
        else if(command.equals("search")) {
            String fieldName = "full_name";
            String by = request.getParameter("by");
            String keyword = request.getParameter("keyword");
            if(by.equals("email"))
                fieldName = "email";

            sql = "select * from " + tableName + " where " + fieldName + " like '%" + keyword + "%'";
            try(Connection conn = connectionManager.getConnection(); //값 그냥 조회는 이 3줄
                Statement stmt = conn.createStatement();// 문장 질의 처리 변환
                ResultSet rs = stmt.executeQuery(sql);) {
                memberDTOList = new ArrayList<>();
                while(rs.next()) {
                    MemberDTO member = setRsToDTO(rs);
                    memberDTOList.add(member);
                }
            } catch(SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if(memberDTOList != null) {
                    request.setAttribute("by", by);
                    request.setAttribute("keyword", keyword);
                    request.setAttribute("dtoList", memberDTOList);
                    request.getRequestDispatcher("../members/list.jsp").forward(request, response);
                } else {
                    request.setAttribute("message", "프로젝트 조회실패");
                    request.getRequestDispatcher("../errors/fail.jsp").forward(request, response);
                }
            }
        }
    }
}