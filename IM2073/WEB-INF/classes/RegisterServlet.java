import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ebookshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    private static final String DB_USERNAME = "myuser";
    private static final String DB_PASSWORD = "xxxx";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String username = (String) request.getAttribute("username");
        String name = (String) request.getAttribute("name");
        String phoneNumber = (String) request.getAttribute("phone_number");
        String email = (String) request.getAttribute("email");
        String shippingAddress = (String) request.getAttribute("shipping_address");
        String usernameError = (String) request.getAttribute("usernameError");
        String passwordError = (String) request.getAttribute("passwordError");
        String phoneError = (String) request.getAttribute("phoneError");
        String emailError = (String) request.getAttribute("emailError");

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <title>Register - Five E-Bookshop</title>");
        out.println("    <link rel='stylesheet' type='text/css' href='register.css'>");
        out.println("</head>");
        out.println("<body>");

        out.println("    <div class='register-container'>");
        out.println("        <h2>Register</h2>");
        out.println("        <form action='/IM2073/register' method='POST'>");

        out.println("            <div class='input-group'>");
        out.println("                <label for='username'>Username:</label>");
        out.println("                <div class='input-container'>");
        out.println("                    <input type='text' id='username' name='username' value='"
                + (username != null ? username : "") + "'>");
        if (usernameError != null)
            out.println("                    <span class='error-message'>" + usernameError + "</span>");
        out.println("                </div>");
        out.println("            </div>");

        out.println("            <div class='input-group'>");
        out.println("                <label for='password'>Password:</label>");
        out.println("                <div class='input-container'>");
        out.println("                    <input type='password' id='password' name='password'>");
        if (passwordError != null)
            out.println("                    <span class='error-message'>" + passwordError + "</span>");
        out.println("                </div>");
        out.println("            </div>");

        out.println("            <div class='input-group'>");
        out.println("                <label for='name'>Full Name:</label>");
        out.println("                <div class='input-container'>");
        out.println("                    <input type='text' id='name' name='name' value='" + (name != null ? name : "")
                + "'>");
        out.println("                </div>");
        out.println("            </div>");

        out.println("            <div class='input-group'>");
        out.println("                <label for='phone_number'>Phone Number:</label>");
        out.println("                <div class='input-container'>");
        out.println("                    <input type='text' id='phone_number' name='phone_number' value='"
                + (phoneNumber != null ? phoneNumber : "") + "'>");
        if (phoneError != null)
            out.println("                    <span class='error-message'>" + phoneError + "</span>");
        out.println("                </div>");
        out.println("            </div>");

        out.println("            <div class='input-group'>");
        out.println("                <label for='email'>Email:</label>");
        out.println("                <div class='input-container'>");
        out.println("                    <input type='email' id='email' name='email' value='"
                + (email != null ? email : "") + "'>");
        if (emailError != null)
            out.println("                    <span class='error-message'>" + emailError + "</span>");
        out.println("                </div>");
        out.println("            </div>");

        out.println("            <div class='input-group'>");
        out.println("                <label for='shipping_address'>Shipping Address:</label>");
        out.println("                <div class='input-container'>");
        out.println("                    <input type='text' id='shipping_address' name='shipping_address' value='"
                + (shippingAddress != null ? shippingAddress : "") + "'>");
        out.println("                </div>");
        out.println("            </div>");

        out.println("            <button type='submit'>Register</button>");
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
        String name = request.getParameter("name");
        String phoneNumber = request.getParameter("phone_number");
        String email = request.getParameter("email");
        String shippingAddress = request.getParameter("shipping_address");

        boolean hasError = false;

        // Check if username is empty
        if (username == null || username.trim().isEmpty()) {
            request.setAttribute("usernameError", "Username cannot be empty.");
            hasError = true;
        }

        if (password.length() < 8) {
            request.setAttribute("passwordError", "Password must be at least 8 characters long.");
            hasError = true;
        }

        if (!phoneNumber.matches("\\d+")) {
            request.setAttribute("phoneError", "Phone number must contain only digits.");
            hasError = true;
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!email.matches(emailRegex)) {
            request.setAttribute("emailError", "Invalid email format.");
            hasError = true;
        }

        // If there are any errors, return to the form with error messages
        if (hasError) {
            request.setAttribute("name", name);
            request.setAttribute("phone_number", phoneNumber);
            request.setAttribute("email", email);
            request.setAttribute("shipping_address", shippingAddress);
            doGet(request, response);
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String checkUserSql = "SELECT * FROM users WHERE username = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkUserSql)) {
                checkStmt.setString(1, username);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        request.setAttribute("usernameError", "Username already exists. Try another one.");
                        hasError = true;
                    }
                }
            }

            if (hasError) {
                doGet(request, response);
                return;
            }

            String insertUserSql = "INSERT INTO users (username, password, name, phone_number, email, shipping_address) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertUserSql)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.setString(3, name);
                stmt.setString(4, phoneNumber);
                stmt.setString(5, email);
                stmt.setString(6, shippingAddress);
                stmt.executeUpdate();
            }

            response.sendRedirect("/IM2073/login");
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("usernameError", "An error occurred while registering. Please try again.");
            doGet(request, response);
        }
    }

}
