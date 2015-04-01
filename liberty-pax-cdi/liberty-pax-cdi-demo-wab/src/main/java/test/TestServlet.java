package test;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebServlet(name="Hello", urlPatterns = {"/hello"})
public class TestServlet extends HttpServlet{

    @Inject
    Bean bean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("---------------------- Servlet aufgerufen ----------------------------");
        resp.getOutputStream().println("Hello");
        if(bean != null)
            resp.getOutputStream().println(bean.sayHello());
        else
            resp.getOutputStream().println("kein CDI :-(");
    }

}
