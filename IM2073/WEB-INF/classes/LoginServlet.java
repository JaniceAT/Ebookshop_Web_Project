import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ebookshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    private static final String DB_USERNAME = "myuser";
    private static final String DB_PASSWORD = "xxxx";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<link rel='stylesheet' type='text/css' href='login.css'>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("    <title>Five E-Bookshop Login</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <div class='login-container'>");
        out.println("        <h2>Five E-Bookshop</h2>");

        String errorMessage = (String) request.getAttribute("errorMessage");
        if (errorMessage != null) {
            out.println("<p class='error'>" + errorMessage + "</p>");
        }

        out.println("        <form action='/IM2073/login' method='POST'>");
        out.println("            <input type='text' name='username' placeholder='Username' required>");
        out.println("            <input type='password' name='password' placeholder='Password' required>");
        out.println("            <button type='submit'>Login</button>");
        out.println("            <a href='/IM2073/register'>");
        out.println("                <button type='button' class='register-btn'>Register</button>");
        out.println("            </a>");
        out.println("        </form>");
        out.println("    </div>");
        out.println("</body>");
        out.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "SELECT * FROM users WHERE username = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String storedPassword = rs.getString("password");
                        if (storedPassword.equals(password)) {
                            String role = rs.getString("role");
                            int userId = rs.getInt("user_id");

                            HttpSession session = request.getSession();
                            session.setAttribute("user_id", userId);

                            if ("customer".equals(role)) {
                                response.sendRedirect("/IM2073/home");
                            } else if ("seller".equals(role)) {
                                response.sendRedirect("/IM2073/homeseller");
                            }
                        } else {
                            request.setAttribute("errorMessage", "Wrong password.");
                            doGet(request, response);
                        }
                    } else {
                        response.sendRedirect("/IM2073/register?username=" + username);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while connecting to the database.");
            doGet(request, response);
        }
    }
}
