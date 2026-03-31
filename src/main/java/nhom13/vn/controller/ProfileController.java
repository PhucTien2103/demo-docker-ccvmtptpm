package nhom13.vn.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

import nhom13.vn.entity.User;
import nhom13.vn.service.IUserService;
import nhom13.vn.service.impl.UserServiceImpl;

@WebServlet(urlPatterns = {"/my-profile", "/update-profile"})
public class ProfileController  extends HttpServlet {
	private static final long serialVersionUID = 1L;
	IUserService userService = new UserServiceImpl();

	//Hien thi profile
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("account");

        String idParam = req.getParameter("id");
        User user;

        if (idParam == null) {
            user = currentUser;
        } else {
            int id = Integer.parseInt(idParam);
            user = userService.findById(id);

            if (user == null) {
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }

            if (!isAllowed(currentUser, user)) {
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }
        }

        req.setAttribute("user", user);
        req.getRequestDispatcher("/view/profile/profile.jsp")
                .forward(req, resp);
    }

	//Update profile
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");

		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("account");

		String fullName = req.getParameter("fullName");
		String email = req.getParameter("email");

		user.setFullName(fullName);
		user.setEmail(email);

		userService.update(user);

		session.setAttribute("account", user);

		resp.sendRedirect(req.getContextPath() + "/my-profile?seccess=1");
	}

    private boolean isAllowed(User currentUser, User targetUser) {

        String role = currentUser.getRole();
        String targetRole = targetUser.getRole();

        // Xem chính mình
        if (currentUser.getId() == targetUser.getId()) {
            return true;
        }

        // Manager xem employee
        if ("MANAGER".equals(role) && "EMPLOYEE".equals(targetRole)) {
            return true;
        }

        // Admin xem employee + manager
        if ("SUPER_ADMIN".equals(role) &&
                ("EMPLOYEE".equals(targetRole) || "MANAGER".equals(targetRole))) {
            return true;
        }

        return false;
    }
}
