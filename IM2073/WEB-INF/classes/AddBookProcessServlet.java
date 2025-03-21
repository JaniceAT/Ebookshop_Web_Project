import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/addbookprocess")
public class AddBookProcessServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Display the Add Book form when the page is accessed via GET
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<link rel='stylesheet' type='text/css' href='addbook.css'>");

        out.println("    <meta charset='UTF-8'>");
        out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("    <title>Add New Book - Five E-Bookshop</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h2>Add a New Book</h2>");

        out.println("<form action='addbookprocess' method='POST'>");
        out.println("    <div class='form-group'>");
        out.println("        <label for='title'>Book Title:</label>");
        out.println("        <input type='text' id='title' name='title' required>");
        out.println("    </div>");

        out.println("    <div class='form-group'>");
        out.println("        <label for='author'>Author Name:</label>");
        out.println("        <input type='text' id='author' name='author' required>");
        out.println("    </div>");

        out.println("    <div class='form-group'>");
        out.println("        <label for='genre'>Genre:</label>");
        out.println("        <input type='text' id='genre' name='genre' required>");
        out.println("    </div>");

        out.println("    <div class='form-group'>");
        out.println("        <label for='price'>Price:</label>");
        out.println("        <input type='number' id='price' name='price' step='0.01' required>");
        out.println("    </div>");

        out.println("    <div class='form-group'>");
        out.println("        <label for='qty'>Quantity:</label>");
        out.println("        <input type='number' id='qty' name='qty' required>");
        out.println("    </div>");

        out.println("    <div class='form-group'>");
        out.println("        <label for='cover_image'>Cover Image URL:</label>");
        out.println("        <input type='text' id='cover_image' name='cover_image' required>");
        out.println("    </div>");

        out.println("    <div class='form-group'>");
        out.println("        <label for='summary'>Summary:</label>");
        out.println("        <textarea id='summary' name='summary' rows='4' required></textarea>");
        out.println("    </div>");

        out.println("    <button type='submit'>Add Book</button>");
        out.println("</form>");

        out.println("</body>");
        out.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String title = request.getParameter("title");
        String author = request.getParameter("author");
        double price = Double.parseDouble(request.getParameter("price"));
        String coverImage = request.getParameter("cover_image");
        int qty = Integer.parseInt(request.getParameter("qty"));
        String genre = request.getParameter("genre"); // Get the genre
        String summary = request.getParameter("summary"); // Get the summary

        // Variable to store authorId
        int authorId = -1;

        try (
                // Database connection setup
                Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/ebookshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                        "myuser", "xxxx");

                // Check if author exists
                PreparedStatement checkAuthorStmt = conn.prepareStatement(
                        "SELECT author_id FROM authors WHERE name = ?");) {

            // Checking if the author already exists
            checkAuthorStmt.setString(1, author);
            try (ResultSet rs = checkAuthorStmt.executeQuery()) {
                if (rs.next()) {
                    authorId = rs.getInt("author_id"); // Author exists, get the ID
                } else {
                    // If author doesn't exist, insert a new author and get the authorId
                    String insertAuthorQuery = "INSERT INTO authors (name) VALUES (?)";
                    try (PreparedStatement insertAuthorStmt = conn.prepareStatement(insertAuthorQuery,
                            Statement.RETURN_GENERATED_KEYS)) {
                        insertAuthorStmt.setString(1, author);
                        insertAuthorStmt.executeUpdate();

                        // Retrieve the generated authorId
                        try (ResultSet generatedKeys = insertAuthorStmt.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                authorId = generatedKeys.getInt(1); // Get the new author ID
                            }
                        }
                    }
                }
            }

            // If no authorId was set, something went wrong
            if (authorId == -1) {
                response.getWriter().println("Error: Could not find or add the author.");
                return;
            }

            // Now insert the book into the books table with the authorId, genre, and
            // summary
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO books (title, author_id, price, cover_image, qty, genre, summary) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                stmt.setString(1, title);
                stmt.setInt(2, authorId); // Using the dynamic authorId
                stmt.setDouble(3, price);
                stmt.setString(4, coverImage);
                stmt.setInt(5, qty);
                stmt.setString(6, genre); // Set genre
                stmt.setString(7, summary); // Set summary

                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    response.sendRedirect("homeseller"); // Redirect back to the seller's home page
                } else {
                    response.getWriter().println("Error: Could not add the book.");
                }
            }

        } catch (SQLException ex) {
            // Print error message
            response.getWriter().println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
