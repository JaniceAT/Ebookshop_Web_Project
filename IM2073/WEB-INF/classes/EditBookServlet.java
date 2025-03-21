import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/editbook")
public class EditBookServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int bookId = Integer.parseInt(request.getParameter("id")); // Get the book ID from the request

        // Database connection setup
        String dbUrl = "jdbc:mysql://localhost:3306/ebookshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
        String dbUser = "myuser";
        String dbPassword = "xxxx";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM books WHERE book_id = ?");
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Display the edit form with the book's data
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();

                out.println("<!DOCTYPE html>");
                out.println("<html lang=\"en\">");
                out.println("<head>");
                out.println("<meta charset=\"UTF-8\">");
                out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
                out.println("<title>Edit Book - Five E-Bookshop</title>");
                out.println("<link rel='stylesheet' type='text/css' href='editbook.css'>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h2>Edit Book</h2>");
                out.println("<form action='/IM2073Project/editbook' method='POST'>");
                out.println("<input type='hidden' name='book_id' value='" + bookId + "'>");
                out.println("<div class=\"form-group\">");
                out.println("<label for=\"title\">Book Title:</label>");
                out.println("<input type=\"text\" name=\"title\" value=\"" + rs.getString("title") + "\" required>");
                out.println("</div>");

                out.println("<div class=\"form-group\">");
                out.println("<label for=\"author_id\">Author ID:</label>");
                out.println(
                        "<input type=\"number\" name=\"author_id\" value=\"" + rs.getInt("author_id") + "\" required>");
                out.println("</div>");

                out.println("<div class=\"form-group\">");
                out.println("<label for=\"genre\">Genre:</label>");
                out.println("<input type=\"text\" name=\"genre\" value=\"" + rs.getString("genre") + "\" required>");
                out.println("</div>");

                out.println("<div class=\"form-group\">");
                out.println("<label for=\"price\">Price:</label>");
                out.println("<input type=\"number\" name=\"price\" step=\"0.01\" value=\"" + rs.getDouble("price")
                        + "\" required>");
                out.println("</div>");

                out.println("<div class=\"form-group\">");
                out.println("<label for=\"qty\">Quantity:</label>");
                out.println("<input type=\"number\" name=\"qty\" value=\"" + rs.getInt("qty") + "\" required>");
                out.println("</div>");

                out.println("<div class=\"form-group\">");
                out.println("<label for=\"cover_image\">Cover Image URL:</label>");
                out.println("<input type=\"text\" name=\"cover_image\" value=\"" + rs.getString("cover_image") + "\">");
                out.println("</div>");

                out.println("<div class=\"form-group\">");
                out.println("<label for=\"summary\">Summary:</label>");
                out.println("<textarea name=\"summary\">" + rs.getString("summary") + "</textarea>");
                out.println("</div>");

                out.println("<button type=\"submit\">Update Book</button>");
                out.println("</form>");
                out.println("</body>");
                out.println("</html>");

            } else {
                response.getWriter().println("Error: Book not found.");
            }

        } catch (SQLException e) {
            response.getWriter().println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Retrieve form data
        int bookId = Integer.parseInt(request.getParameter("book_id"));
        String title = request.getParameter("title");
        int authorId = Integer.parseInt(request.getParameter("author_id"));
        String genre = request.getParameter("genre");
        double price = Double.parseDouble(request.getParameter("price"));
        int qty = Integer.parseInt(request.getParameter("qty"));
        String coverImage = request.getParameter("cover_image");
        String summary = request.getParameter("summary");

        // Database connection setup
        String dbUrl = "jdbc:mysql://localhost:3306/ebookshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
        String dbUser = "myuser";
        String dbPassword = "xxxx";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            // Update the book details
            String updateQuery = "UPDATE books SET title = ?, author_id = ?, genre = ?, price = ?, qty = ?, cover_image = ?, summary = ? WHERE book_id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                stmt.setString(1, title);
                stmt.setInt(2, authorId);
                stmt.setString(3, genre);
                stmt.setDouble(4, price);
                stmt.setInt(5, qty);
                stmt.setString(6, coverImage);
                stmt.setString(7, summary);
                stmt.setInt(8, bookId);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    response.sendRedirect("homeseller"); // Redirect to the seller's home page or any other page
                } else {
                    response.getWriter().println("Error: Could not update the book.");
                }
            }

        } catch (SQLException ex) {
            response.getWriter().println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
