import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/home") // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)
public class EShopDisplayServlet extends HttpServlet {

   @Override
   public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      // Read the JSON body from the request
      BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
      StringBuilder stringBuilder = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
         stringBuilder.append(line);
         System.out.println(line);
      }

      // Parse the JSON
      String jsonString = stringBuilder.toString();
      JSONObject jsonObject = new JSONObject(jsonString);

      // Get the items array and finalTotalPrice
      JSONArray itemsArray = jsonObject.getJSONArray("items");
      double finalTotalPrice = jsonObject.getDouble("finalTotalPrice");

      // Retrieve user_id (customerId) from session
      HttpSession session = request.getSession(false); // false means don't create new session if not found
      if (session == null || session.getAttribute("user_id") == null) {
         response.getWriter().write("Error: User is not logged in.");
         return;
      }
      int customerId = (int) session.getAttribute("user_id"); // Retrieve user ID

      try (Connection conn = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/ebookshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
            "myuser", "xxxx")) {

         // Loop through each item in the cart and insert it into the order_records table
         String insertSQL = "INSERT INTO order_records (book_id, customer_id, qty_ordered, total_price) VALUES (?, ?, ?, ?)";
         try (PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
            for (int i = 0; i < itemsArray.length(); i++) {
               JSONObject item = itemsArray.getJSONObject(i);
               int bookId = item.getInt("bookId");
               int qtyOrdered = item.getInt("quantity");
               double totalPrice = item.getDouble("total");

               // Set the values for the prepared statement
               stmt.setInt(1, bookId); // Set book_id from JavaScript
               stmt.setInt(2, customerId); // Set customer_id from session
               stmt.setInt(3, qtyOrdered); // Set qty_ordered from JavaScript
               stmt.setDouble(4, totalPrice); // Set total_price from JavaScript

               // Add the current INSERT statement to the batch
               stmt.addBatch();
            }

            // Execute the batch (insert all records in one go)
            stmt.executeBatch();
         }
         response.sendRedirect("/thankyou.html");

      } catch (SQLException ex) {
         response.setContentType("application/json");
         response.getWriter().write("{\"message\": \"Error: " + ex.getMessage() + "\"}");
      }
   }

   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();

      HttpSession session = request.getSession(false);
      Integer customerId = null;
      String name = null;

      // Get customer ID from session
      if (session != null) {
         customerId = (Integer) session.getAttribute("user_id");
      }

      if (customerId == null) {
         // Redirect to login page if user is not logged in
         response.sendRedirect("/IM2073/login");
         return;
      }

      // Fetch user name from the database based on customerId
      try (
            Connection conn = DriverManager.getConnection(
                  "jdbc:mysql://localhost:3306/ebookshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                  "myuser", "xxxx");
            PreparedStatement stmt = conn.prepareStatement("SELECT name FROM users WHERE user_id = ?")) {
         stmt.setInt(1, customerId); // Set the customerId to the query
         ResultSet rset = stmt.executeQuery();

         if (rset.next()) {
            name = rset.getString("name"); // Fetch the name from the result set
         }

      } catch (SQLException ex) {
         out.println("<p>Error: " + ex.getMessage() + "</p>");
         ex.printStackTrace();
      }

      // Fallback if name is not found
      if (name == null) {
         name = "Guest"; // Default to "Guest" if the name is not found
      }

      // Continue with the HTML rendering
      out.println("<!DOCTYPE html>");
      out.println("<html>");
      out.println("<head>");
      out.println("<title>Five E-Bookshop</title>");
      out.println("<link rel='stylesheet' type='text/css' href='shopdisplay.css?v=1'>");
      out.println("</head>");
      out.println("<body>");
      out.println("<div class='header'>Hi, " + name + "!</div>");
      out.println("<h2>Welcome to Five E-Bookshop</h2>");

      // Your Java code to fetch books and display them
      try (
            Connection conn = DriverManager.getConnection(
                  "jdbc:mysql://localhost:3306/ebookshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                  "myuser", "xxxx");
            Statement stmt2 = conn.createStatement();) {

         String sqlStr = "SELECT b.book_id, b.title, a.name AS author_name, b.price, b.cover_image " +
               "FROM books b " +
               "JOIN authors a ON b.author_id = a.author_id " +
               "WHERE b.qty > 0";

         ResultSet rset2 = stmt2.executeQuery(sqlStr);

         out.println("<div class='book-grid'>");
         while (rset2.next()) {
            int bookId = rset2.getInt("book_id");
            String title = rset2.getString("title");
            String authorName = rset2.getString("author_name");
            double price = rset2.getDouble("price");
            String coverImage = rset2.getString("cover_image");

            out.println("<div class='book-container'>");
            out.println("<img src='" + coverImage + "' alt='Cover Image'>");
            out.println("<h3>" + title + "</h3>");
            out.println("<p>by " + authorName + "</p>");
            out.println("<p class='price'>$" + price + "</p>");
            out.println("<div class='quantity-controls'>");
            out.println("<button onclick='updateQuantity(" + bookId + ", -1, " + price + ")'>-</button>");
            out.println("<input type='text' id='quantity-" + bookId
                  + "' value='0' readonly style='width: 40px; text-align: center;'>");
            out.println("<button onclick='updateQuantity(" + bookId + ", 1, " + price + ")'>+</button>");
            out.println("</div>");
            out.println("<a href='bookdetails?id=" + bookId + "' class='details-btn'>Details</a>");
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

      out.println("<script src='shopdisplay.js'></script>");
      out.println("</body>");
      out.println("</html>");
   }

}
