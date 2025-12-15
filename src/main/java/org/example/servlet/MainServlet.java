package org.example.servlet;

import org.example.config.JavaConfig;
import org.example.config.JavaConfig2;
import org.example.controller.PostController;
import org.example.repository.PostRepository;
import org.example.service.PostService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
    private PostController controller;

    @Override
    public void init() throws ServletException {
        // final var context = new AnnotationConfigApplicationContext(JavaConfig.class);
        final var context = new AnnotationConfigApplicationContext(JavaConfig2.class);
        controller = (PostController) context.getBean("postController");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // super.service(req, resp);

        // resp.setContentType("text/plain");
        // resp.getWriter().print("ok");

        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();

            if (method.equals("GET") && path.equals("/api/posts")) {
                controller.all(resp);
                return;
            }
            if(method.equals("GET") && path.matches("/api/posts/\\d+")) {
                final var id = Long.parseLong(path.substring(path.lastIndexOf("/")));
                controller.getById(id, resp);
                return;
            }
            if (method.equals("POST") && path.equals("/api/posts")) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals("DELETE") && path.matches("/api/posts/\\d+")) {
                final var id = Long.parseLong(path.substring(path.lastIndexOf("/")));
                controller.removeById(id, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
