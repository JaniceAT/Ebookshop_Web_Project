import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/homeseller")
public class EShopSellerDisplayServlet extends HttpServlet {

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                        throws ServletException, IOException {
                String bookId = request.getParameter("id");

                // Check if bookId is not null and try to delete the book from the database
                if (bookId != null && !bookId.isEmpty()) {
                        try (Connection conn = DriverManager.getConnection(
                                        "jdbc:mysql://localhost:3306/ebookshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                                        "myuser", "xxxx");
                                        PreparedStatement stmt = conn
                                                        .prepareStatement("DELETE FROM books WHERE book_id = ?")) {

                                stmt.setInt(1, Integer.parseInt(bookId));
                                int rowsAffected = stmt.executeUpdate();

                                response.setContentType("text/plain");
                                if (rowsAffected > 0) {
                                        response.getWriter().write("Book deleted successfully.");
                                } else {
                                        response.getWriter().write("Book not found.");
                                }
                        } catch (SQLException ex) {
                                response.getWriter().write("Error deleting book: " + ex.getMessage());
                        }
                } else {
                        response.getWriter().write("No book ID provided.");
                }
        }

        @Override
        public void doGet(HttpServletRequest request, HttpServletResponse response)
                        throws ServletException, IOException {
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Seller - Five E-Bookshop</title>");
                out.println("<link rel='stylesheet' type='text/css' href='sellerdisplay.css'>");
                out.println("</head>");
                out.println("<body>");

                out.println("<div class='navbar'>");
                out.println("<ul>");
                out.println("<li><a href='/IM2073/homeseller'>Home</a></li>");
                out.println("<li><a href='/IM2073/addbookprocess'>Add Book</a></li>");
                out.println("</ul>");
                out.println("</div>");

                out.println("<div class='header'>Welcome to the Seller Page</div>");

                try (Connection conn = DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/ebookshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                                "myuser", "xxxx");
                                Statement stmt = conn.createStatement()) {

                        String sqlStr = "SELECT b.book_id, b.title, a.name AS author_name, b.price, b.cover_image, b.qty "
                                        +
                                        "FROM books b " +
                                        "JOIN authors a ON b.author_id = a.author_id";

                        ResultSet rset = stmt.executeQuery(sqlStr);

                        out.println("<div class='book-grid'>");
                        while (rset.next()) {
                                int bookId = rset.getInt("book_id");
                                String title = rset.getString("title");
                                String authorName = rset.getString("author_name");
                                double price = rset.getDouble("price");
                                String coverImage = rset.getString("cover_image");
                                int quantity = rset.getInt("qty");

                                out.println("<div class='book-container'>");
                                out.println("<img src='" + coverImage + "' alt='Cover Image'>");
                                out.println("<h3>" + title + "</h3>");
                                out.println("<p>by " + authorName + "</p>");
                                out.println("<p class='price'>$" + price + "</p>");
                                out.println("<p class='stock'>Stock: " + quantity + "</p>");

                                // Quantity controls
                                out.println("<div class='quantity-controls'>");
                                out.println("<button onclick='updateQuantity(" + bookId + ", -1, " + price
                                                + ")'>-</button>");
                                out.println("<input type='text' id='quantity-" + bookId
                                                + "' value='0' readonly style='width: 40px; text-align: center;'>");
                                out.println("<button onclick='updateQuantity(" + bookId + ", 1, " + price
                                                + ")'>+</button>");
                                out.println("</div>");

                                out.println("<div class='btn-group'>");
                                out.println("<a href='bookdetails?id=" + bookId + "' class='details-btn'>Details</a>");
                                out.println("<a href='/IM2073/editbook?id=" + bookId
                                                + "' class='details-btn'>Edit</a>");
                                out.println("<button class='details-btn' onclick='deleteBook(" + bookId
                                                + ")'>Delete</button>");
                                out.println("</div>");

                                out.println("</div>");
                        }
                        out.println("</div>");

                } catch (SQLException ex) {
                        out.println("<p>Error: " + ex.getMessage() + "</p>");
                        ex.printStackTrace();
                }

                out.println("<div id='cart'>");
                out.println("<h4>Shopping Cart</h4>");
                out.println("<ul id='cart-items'></ul>");
                out.println("<p id='cart-total'>Total: $0.00</p>");
                out.println("<button class='buy-now-btn' onclick='buyNow()'>Buy Now</button>");
                out.println("</div>");

                out.println("</body>");
                out.println("<script src='shopdisplay.js' defer></script>");
                out.println("</html>");
        }
}
