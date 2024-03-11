package idusw.javaweb.openapia;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name="datagokr", value = "/data-api")
public class Datagokr extends HelloServlet{

    private String message = "페이커";


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //System.out.println("data-api");
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        int count = Integer.parseInt(request.getParameter("cnt"));
        for(int i = 0; i < count; i++)
           out.println(message);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //System.out.println("data-api");
        PrintWriter out = response.getWriter();
        out.println("email:" +request.getParameter("email"));
        out.println("password:" +request.getParameter("password"));
    }
}
