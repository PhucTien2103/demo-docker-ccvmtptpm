package nhom13.vn.controller;
import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nhom13.vn.entity.User;
import nhom13.vn.service.IUserService;
import nhom13.vn.service.impl.UserServiceImpl;
@WebServlet({
        "/manager/zemployeez",
        "/manager/zemployeez/add",
        "/manager/zemployeez/insert",
        "/manager/zemployeez/edit",
        "/manager/zemployeez/update",
        "/manager/zemployeez/delete"
})
public class ManagerEmployeeController extends HttpServlet {

    IUserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String uri = req.getRequestURI();

        // ADD FORM
        if (uri.contains("/add")) {
            req.getRequestDispatcher("/view/manager/employee-form.jsp")
                    .forward(req, resp);
        }

        // EDIT FORM
        else if (uri.contains("/edit")) {
            int id = Integer.parseInt(req.getParameter("id"));
            User user = userService.findById(id);

            req.setAttribute("user", user);
            req.getRequestDispatcher("/view/manager/employee-form.jsp")
                    .forward(req, resp);
        }

        // DELETE
        else if (uri.contains("/delete")) {
            int id = Integer.parseInt(req.getParameter("id"));

            User user = userService.findById(id);
            user.setStatus(0);
            userService.update(user);

            resp.sendRedirect(req.getContextPath() + "/manager/zemployeez");
        }

        // LIST (ĐỂ CUỐI)
        else if (uri.contains("/manager/zemployeez")) {
            List<User> list = userService.findByRole("EMPLOYEE");
            req.setAttribute("list", list);
            req.getRequestDispatcher("/view/manager/employees.jsp")
                    .forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String uri = req.getRequestURI();

        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");

        if (uri.contains("/insert")) {

            String username = req.getParameter("username");
            String password = req.getParameter("password");

            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setFullName(fullName); // ✅ giờ sẽ lưu được
            user.setEmail(email);
            user.setRole("EMPLOYEE");
            user.setStatus(1);

            userService.insert(user); // ✅ đúng chuẩn

            resp.sendRedirect(req.getContextPath() + "/manager/zemployeez");
        }

        // UPDATE
        else if (uri.contains("/update")) {

            int id = Integer.parseInt(req.getParameter("id"));
            User user = userService.findById(id);

            user.setFullName(fullName);
            user.setEmail(email);

            userService.update(user);

            resp.sendRedirect(req.getContextPath() + "/manager/zemployeez");
        }
    }
}