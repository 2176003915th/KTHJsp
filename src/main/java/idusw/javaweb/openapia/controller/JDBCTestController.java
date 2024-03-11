package idusw.javaweb.openapia.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name="jdbcTestController", urlPatterns = {"/jdbc/test", "/members/get"})  //urlPattern은 무조건 처음 /사용
public class JDBCTestController extends HttpServlet {

    // http://localhost:8080/<application-context>/jdbc/test?input=<입력한 내용>
    public void process(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.setAttribute("input", req.getParameter("input"));
        req.getRequestDispatcher("../examples/blank.jsp").forward(req, resp);
    }
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        //super.doGet(req, resp);
        process(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        //super.doPost(req, resp);
        process(req, resp);
    }
}
