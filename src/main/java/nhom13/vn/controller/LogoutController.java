package nhom13.vn.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet(urlPatterns = {"/logout"})
public class LogoutController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        // 1. Lấy session hiện tại
        HttpSession session = req.getSession(false); 
        
        // 2. Nếu session tồn tại thì hủy nó đi
        if (session != null) {
            session.invalidate(); // Xóa toàn bộ dữ liệu session [1, 3, 4]
        }
        
        // 3. Chuyển hướng người dùng về trang Login hoặc Trang chủ
        resp.sendRedirect(req.getContextPath() + "/login"); // [4, 5]
    }
}