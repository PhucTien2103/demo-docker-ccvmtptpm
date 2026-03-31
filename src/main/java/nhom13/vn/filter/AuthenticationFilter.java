package nhom13.vn.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import nhom13.vn.entity.User;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String uri = req.getRequestURI();

        HttpSession session = req.getSession(false);
        User user = (session != null)
                ? (User) session.getAttribute("account")
                : null;

        // Cho phép truy cập các trang public
        if (uri.contains("/login")
                || uri.contains("/signup")
                || uri.contains("/forgot-password")
                || uri.contains("/view")
                || uri.contains("/css")
                || uri.contains("/js")
                || uri.contains("/tesst")) {

            chain.doFilter(request, response);
            return;
        }

        // Chưa đăng nhập
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String role = user.getRole();

        // Kiểm tra quyền Admin
        if (uri.contains("/admin") && !"SUPER_ADMIN".equals(role)) {

            resp.sendRedirect(req.getContextPath() + "/login");
            return;

        }



        // Kiểm tra quyền Manager
        if (uri.contains("/manager") && !"MANAGER".equals(role)) {
            System.out.println(">>> BLOCKED MANAGER <<<");
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Kiểm tra quyền Employee
        if (uri.contains("/employee") && !"EMPLOYEE".equals(role)) {

            resp.sendRedirect(req.getContextPath() + "/login");
            return;

        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}