package idusw.javaweb.openapia.controller;

import idusw.javaweb.openapia.model.ProjectDTO;
import idusw.javaweb.openapia.util.ConnectionManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import javax.swing.*;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "projectController", urlPatterns = {"/projects/add-form","/projects/add","/projects/list",
        "/projects/detail", "/projects/update" , "/projects/update-form" , "/projects/delete", "/projects/search",
        "/projects/asc", "/projects/desc", "/projects/asc1"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2,
maxFileSize = 1024 * 1024 * 30,
maxRequestSize = 1024 * 1024 * 50)
public class ProjectController extends HttpServlet {

    ConnectionManager connectionManager = ConnectionManager.getInstance();
    ProjectDTO projectDTO = null;
    List<ProjectDTO> projectDTOList = null;
    int cnt = 0;



    private ProjectDTO setRsToDTO(ResultSet rs) throws SQLException { //값을 보여주거나 조회할때 이용
        projectDTO = new ProjectDTO();
        projectDTO.setPid(rs.getLong("pid"));
        projectDTO.setProjectName(rs.getString("project_name"));
        projectDTO.setProjectDescription(rs.getString("project_description"));
        projectDTO.setStatus(rs.getString("status"));
        projectDTO.setProjectLeader(rs.getString("project_leader"));
        projectDTO.setProjectImage(rs.getString("project_image"));
        projectDTO.setRegTimeStamp(rs.getString("start_timestamp"));
        projectDTO.setRevTimeStamp(rs.getString("rev_timestamp"));
        return projectDTO;
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
        String tableName = "prj_a202012007";
        //요청의 type에 따른 작업 분리
        if(command.equals("add-form")) {
            request.getRequestDispatcher("../projects/add.jsp").forward(request, response);
        }
        else if(command.equals("add")) {
            fileUpload(request, response);
            projectDTO = new ProjectDTO();
            projectDTO.setProjectName(request.getParameter("project-name"));
            projectDTO.setStatus(request.getParameter("status"));
            projectDTO.setProjectDescription(request.getParameter("project-description"));
            projectDTO.setClientCompany(request.getParameter("client-company"));
            projectDTO.setProjectLeader(request.getParameter("project-leader"));
            projectDTO.setProjectImage((String) request.getAttribute("project-image"));

            sql = "insert into " + tableName +
                    "(project_name, project_description, status, project_leader, project_image) " +
                    "values (?,?,?,?,?) ";

            try ( Connection conn = connectionManager.getConnection(); //db값을 수정 입력 이면 2줄
                  PreparedStatement pstmt = conn.prepareStatement(sql);) {
                //문장 (질의 처리 ) 객체 반환, 객체에 저장된 값을 이용하여 질의문 생성
                pstmt.setString(1, projectDTO.getProjectName());
                pstmt.setString(2, projectDTO.getProjectDescription());
                pstmt.setString(3, projectDTO.getStatus());
                pstmt.setString(4, projectDTO.getProjectLeader());
                pstmt.setString(5, projectDTO.getProjectImage());
                cnt = pstmt.executeUpdate();
        }catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if (cnt >= 1) {
                    response.sendRedirect("../projects/list");
                } else {
                    request.setAttribute("message", "프로젝트 등록 실패");
                    request.getRequestDispatcher("/errors/fail.jsp").forward(request, response); //회원가입 실패 view 호출
                }
            }
        }
        else if(command.equals("list")) {
            sql = "select * from " + tableName;
            try ( Connection conn = connectionManager.getConnection(); //값 그냥 조회는 이 3줄
                  Statement stmt = conn.createStatement();// 문장 질의 처리 변환
                  ResultSet rs = stmt.executeQuery(sql);) {
                projectDTOList = new ArrayList<>();
                while(rs.next()){
                    ProjectDTO project = setRsToDTO(rs);
                    projectDTOList.add(project);
                }
            } catch(SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if(projectDTOList != null) {
                    request.setAttribute("dtoList", projectDTOList);
                    request.getRequestDispatcher("../projects/list.jsp").forward(request, response);
                } else {
                    request.setAttribute("message", "프로젝트 조회실패");
                    request.getRequestDispatcher("../errors/fail.jsp").forward(request, response);
                }
            }
        }
        else if(command.equals("detail")) {
            projectDTO = new ProjectDTO();
            projectDTO.setPid(Long.valueOf(request.getParameter("pid")));
            sql = "select * from " + tableName + " where pid=" + projectDTO.getPid();
            try (Connection conn = connectionManager.getConnection();
                 Statement stmt = conn.createStatement();// 문장 질의 처리 변환
                 ResultSet rs = stmt.executeQuery(sql);) {
                if(rs.next()){
                    projectDTO = setRsToDTO(rs);
                }
            } catch(SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if(projectDTO != null) {
                    request.setAttribute("pDto", projectDTO);
                    request.getRequestDispatcher("../projects/detail.jsp").forward(request, response);
                } else {
                    request.setAttribute("message", "프로젝트 조회실패");
                    request.getRequestDispatcher("../errors/fail.jsp").forward(request, response);
                }
            }
        }
        else if(command.equals("update-form")) {
            projectDTO = new ProjectDTO();
            projectDTO.setPid(Long.valueOf(request.getParameter("pid")));
            sql = "select * from " + tableName + " where pid=" + projectDTO.getPid();

            try (Connection conn = connectionManager.getConnection();
                 Statement stmt = conn.createStatement();// 문장 질의 처리 변환
                 ResultSet rs = stmt.executeQuery(sql);) {  // 자신에게 권한이 있는 대상 테이블
                if(rs.next()){
                    projectDTO = setRsToDTO(rs);
                }
            } catch(SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if(projectDTO != null) {
                    request.setAttribute("pDto", projectDTO);
                    request.getRequestDispatcher("../projects/edit.jsp").forward(request, response);
                } else {
                    request.setAttribute("message", "프로젝트 업데이트 조회실패");
                    request.getRequestDispatcher("../errors/fail.jsp").forward(request, response);
                }
            }
        }
        else if(command.equals("update")) {
            fileUpload(request, response);
            projectDTO = new ProjectDTO();
            projectDTO.setPid(Long.valueOf(request.getParameter("pid"))); // String -> Long 값으로 변환하여 가져오기
            projectDTO.setProjectName(request.getParameter("project-name"));
            projectDTO.setStatus(request.getParameter("status"));
            projectDTO.setProjectDescription(request.getParameter("project-description"));
            projectDTO.setProjectLeader(request.getParameter("project-leader"));
            projectDTO.setProjectImage((String) request.getAttribute("project-image"));
            sql = "update " + tableName + " set project_name = ?, status = ?, project_description = ?, project_leader = ?, " +
                    "project_image = ?,  rev_timestamp = NOW() where pid = ?";
            try (Connection conn = connectionManager.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql);) {
                pstmt.setString(1, projectDTO.getProjectName()); // placeholder에 매칭하여 대체됨
                pstmt.setString(2, projectDTO.getStatus());
                pstmt.setString(3, projectDTO.getProjectDescription());
                pstmt.setString(4, projectDTO.getProjectLeader()); // Auto Boxing / Unboxing : long -> Long, Long -> long
                pstmt.setString(5, projectDTO.getProjectImage());
                pstmt.setLong(6, projectDTO.getPid());
                cnt = pstmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if(cnt > 0) {
                    response.sendRedirect("../projects/list");
                } else {
                    request.setAttribute("message", "프로젝트 업데이트 실패");
                    request.getRequestDispatcher("../errors/fail.jsp").forward(request, response);
                }
            }
        }
        else if(command.equals("delete")) {
            projectDTO = new ProjectDTO();
            projectDTO.setPid(Long.valueOf(request.getParameter("pid")));
            sql ="delete from " + tableName + " where pid= ?";
            try (Connection conn = connectionManager.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql);) {
                pstmt.setLong(1, projectDTO.getPid());
                cnt = pstmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if(cnt > 0) {
                    response.sendRedirect("../projects/list");
                    // response.sendRedirect("https://www.naver.com"); 상관없이 이동만 시킴
                } else {
                    request.getRequestDispatcher("../errors/fail.jsp").forward(request, response);
                }
            }
        }
        else if(command.equals("search")) {
            String fieldName = "project_name";
            String by = request.getParameter("by");
            String keyword = request.getParameter("keyword");
            if(by.equals("leader"))
                fieldName = "project_leader";

            sql = "select * from " + tableName + " where " + fieldName + " like '%" + keyword + "%'";
            try(Connection conn = connectionManager.getConnection(); //값 그냥 조회는 이 3줄
                Statement stmt = conn.createStatement();// 문장 질의 처리 변환
                ResultSet rs = stmt.executeQuery(sql);) {
                projectDTOList = new ArrayList<>();
                while(rs.next()) {
                    ProjectDTO project = setRsToDTO(rs);
                    projectDTOList.add(project);
                }
            } catch(SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if(projectDTOList != null) {
                    request.setAttribute("by", by);
                    request.setAttribute("keyword", keyword);
                    request.setAttribute("dtoList", projectDTOList);
                    request.getRequestDispatcher("../projects/list.jsp").forward(request, response);
                } else {
                    request.setAttribute("message", "프로젝트 조회실패");
                    request.getRequestDispatcher("../errors/fail.jsp").forward(request, response);
                }
            }
        }
        else if(command.equals("asc")) {
            String fieldName = "project_name";
            String by = request.getParameter("by");
            String keyword = request.getParameter("keyword");
            if(by.equals("leader"))
                fieldName = "project_leader";

            sql = "select * from " + tableName + " where " + fieldName + " like '%" + keyword + "%'" + " ORDER BY project_name ASC";
            try(Connection conn = connectionManager.getConnection(); //값 그냥 조회는 이 3줄
                Statement stmt = conn.createStatement();// 문장 질의 처리 변환
                ResultSet rs = stmt.executeQuery(sql);) {
                projectDTOList = new ArrayList<>();
                while(rs.next()) {
                    ProjectDTO project = setRsToDTO(rs);
                    projectDTOList.add(project);
                }
            } catch(SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if(projectDTOList != null) {
                    request.setAttribute("dtoList", projectDTOList);
                    request.setAttribute("by", by);
                    request.setAttribute("keyword", keyword);
                    request.getRequestDispatcher("../projects/list.jsp").forward(request, response);
                } else {
                    request.setAttribute("message", "프로젝트 조회실패");
                    request.getRequestDispatcher("../errors/fail.jsp").forward(request, response);
                }
            }
        }
        else if(command.equals("desc")) {
            String fieldName = "project_name";
            String by = request.getParameter("by");
            String keyword = request.getParameter("keyword");
            if(by.equals("leader"))
                fieldName = "project_leader";

            sql = "select * from " + tableName + " where " + fieldName + " like '%" + keyword + "%'" + " ORDER BY project_name DESC";
            try(Connection conn = connectionManager.getConnection(); //값 그냥 조회는 이 3줄
                Statement stmt = conn.createStatement();// 문장 질의 처리 변환
                ResultSet rs = stmt.executeQuery(sql);) {
                projectDTOList = new ArrayList<>();
                while(rs.next()) {
                    ProjectDTO project = setRsToDTO(rs);
                    projectDTOList.add(project);
                }
            } catch(SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if(projectDTOList != null) {
                    request.setAttribute("dtoList", projectDTOList);
                    request.setAttribute("by", by);
                    request.setAttribute("keyword", keyword);
                    request.getRequestDispatcher("../projects/list.jsp").forward(request, response);
                } else {
                    request.setAttribute("message", "프로젝트 조회실패");
                    request.getRequestDispatcher("../errors/fail.jsp").forward(request, response);
                }
            }
        }
        else if(command.equals("asc1")) {
            sql = "select * from " + tableName + " ORDER BY project_leader ASC";
            try(Connection conn = connectionManager.getConnection(); //값 그냥 조회는 이 3줄
                Statement stmt = conn.createStatement();// 문장 질의 처리 변환
                ResultSet rs = stmt.executeQuery(sql);) {
                projectDTOList = new ArrayList<>();
                while(rs.next()) {
                    ProjectDTO project = setRsToDTO(rs);
                    projectDTOList.add(project);
                }
            } catch(SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if(projectDTOList != null) {
                    request.setAttribute("dtoList", projectDTOList);
                    request.getRequestDispatcher("../projects/list.jsp").forward(request, response);
                } else {
                    request.setAttribute("message", "프로젝트 조회실패");
                    request.getRequestDispatcher("../errors/fail.jsp").forward(request, response);
                }
            }
        }



    }

    private static final String SAVE_DIR = "files";
    private String partName = null;
    private String partValue = null;
    public void fileUpload(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String appPath = request.getServletContext().getRealPath("");
        String savePath = appPath + File.separator + SAVE_DIR;
        File fileSaveDir = new File(savePath);
        if( !fileSaveDir.exists() ) {
            fileSaveDir.mkdir();
        }
        Collection<Part> parts = request.getParts();
        for (Part part : parts) {
            partName = part.getName();
            if(part.getContentType() != null) {
                partValue = getFileName(part);
                if(partValue != null && !partValue.isEmpty()) {
                    part.write(savePath + File.separator + partValue);
                }
            }

            else {
                partValue = request.getParameter(partName);
            }
            request.setAttribute(partName, partValue);
        }
    }

    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length()-1);
            }
        }
        return "";
    }
}

