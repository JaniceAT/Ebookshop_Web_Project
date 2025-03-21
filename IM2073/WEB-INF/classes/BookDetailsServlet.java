
// To save as "ebookshop\WEB-INF\classes\BookDetailsServlet.java".
import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/bookdetails") // Configure the request URL for this servlet
public class BookDetailsServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set the MIME type for the response message
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Get the book_id from the query parameter
        int bookId = Integer.parseInt(request.getParameter("id"));

        try (
                // Step 1: Allocate a database 'Connection' object
                Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/ebookshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                        "myuser", "xxxx"); // Replace with actual database credentials

                // Step 2: Prepare the SQL query to get book details
                PreparedStatement pstmt = conn.prepareStatement(
                        "SELECT b.title, a.name AS author_name, b.price, b.cover_image, b.summary, b.genre " +
                                "FROM books b " +
                                "JOIN authors a ON b.author_id = a.author_id " +
                                "WHERE b.book_id = ?")) {

            // Set the book_id in the query
            pstmt.setInt(1, bookId);
            ResultSet rset = pstmt.executeQuery();

            // Check if the book was found
            if (rset.next()) {
                // Retrieve book details
                String title = rset.getString("title");
                String authorName = rset.getString("author_name");
                double price = rset.getDouble("price");
                String coverImage = rset.getString("cover_image");
                String summary = rset.getString("summary");
                String genre = rset.getString("genre");

                // Display book details with some basic styling
                out.println("<html>");
                out.println("<head>");
                out.println("<link rel='stylesheet' type='text/css' href='bookdetails.css'>");
                out.println("<title>" + title + " - Book Details</title>");
                out.println("</head>");
                out.println("<body>");

                out.println("<div class='book-details'>");
                out.println("<img src='" + coverImage + "' alt='Cover Image'>");

                out.println("<div class='book-info'>");
                out.println("<h2>" + title + "</h2>");

                out.println("<div class='book-info-p'>");

                out.println("<p><strong>Author:</strong> " + authorName + "</p>");
                out.println("<p><strong>Genre:</strong> " + genre + "</p>");
                out.println("<p><strong>Price:</strong> $" + price + "</p>");
                out.println("</div>");

                out.println("<div class='summary'>");
                out.println("<h3>Summary</h3>");
                out.println("<p>" + summary + "</p>");
                out.println("</div>");

                out.println("<form action='/IM2073/home' method='GET'>");
                out.println("<button type='submit'>Back to Home</button>");
                out.println("</form>");

                out.println("</div>");
                out.println("</div>");

                out.println("</body>");
                out.println("</html>");
            } else {
                out.println("<html><body>");
                out.println("<p>Book not found.</p>");
                out.println("</body></html>");
            }

        } catch (SQLException ex) {
            // Handle database connection or query errors
            out.println("<html><body>");
            out.println("<p>Error: " + ex.getMessage() + "</p>");
            out.println("<p>Details: " + ex.toString() + "</p>");
            out.println("</body></html>");
            ex.printStackTrace();
        }
    }
}
