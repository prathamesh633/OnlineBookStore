package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bittercode.model.Book;
import com.bittercode.model.UserRole;
import com.bittercode.service.BookService;
import com.bittercode.service.impl.BookServiceImpl;
import com.bittercode.util.StoreUtil;

public class ViewBookServlet extends HttpServlet {

    // book service for database operations and logics
    BookService bookService = new BookServiceImpl();

    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType("text/html");

        // Check if the customer is logged in, or else return to login page
        if (!StoreUtil.isLoggedIn(UserRole.CUSTOMER, req.getSession())) {
            RequestDispatcher rd = req.getRequestDispatcher("CustomerLogin.html");
            rd.include(req, res);
            pw.println("<table class=\"tab\"><tr><td>Please Login First to Continue!!</td></tr></table>");
            return;
        }
        try {

            // Read All available books from the database
            List<Book> books = bookService.getAllBooks();

            // Default Page to load data into
            RequestDispatcher rd = req.getRequestDispatcher("CustomerHome.html");
            rd.include(req, res);

            // Set Available Books tab as active
            StoreUtil.setActiveTab(pw, "books");
            
            // Checkout Button
            pw.println("<div style='float:right; margin-right:20px;'><form action=\"cart\" method=\"post\">" 
                    + "<input type='submit' class=\"btn btn-success\" name='cart' value='Proceed to Checkout'/></form>" 
                    + "    </div>");

            // Show the heading for the page
            
            pw.println("<div class='container'><div class='row'>");

            // Add or Remove items from the cart, if requested
            StoreUtil.updateCartItems(req);

            HttpSession session = req.getSession();
            for (Book book : books) {

                // Add each book to display as a card
                pw.println(this.addBookToCard(session, book));

            }

            //End of row and container
            pw.println("</div></div>");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String addBookToCard(HttpSession session, Book book) {
        String bCode = book.getBarcode();
        int bQty = book.getQuantity();

        int cartItemQty = 0;
        if (session.getAttribute("qty_" + bCode) != null) {
            cartItemQty = (int) session.getAttribute("qty_" + bCode);
        }

        String button;
        if (bQty > 0) {
            if (cartItemQty == 0) {
                button = "<form action='viewbook' method='post'>"
                        + "<input type='hidden' name='selectedBookId' value='" + bCode + "'>"
                        + "<input type='hidden' name='qty_" + bCode + "' value='1'/>"
                        + "<input type='submit' class='btn btn-primary' name='addToCart' value='Add To Cart'/></form>";
            } else {
                button = "<form method='post' action='cart'>"
                        + "<button type='submit' name='removeFromCart' class='glyphicon glyphicon-minus btn btn-danger'></button> "
                        + "<input type='hidden' name='selectedBookId' value='" + bCode + "'/>"
                        + cartItemQty
                        + " <button type='submit' name='addToCart' class='glyphicon glyphicon-plus btn btn-success'></button></form>";
            }
        } else {
            button = "<p class='btn btn-danger'>Out Of Stock</p>";
        }

        return "<div class='col-md-4'><div class='card'>"
                + "<div class='row card-body'>"
                + "<img class='col-sm-6' src='logo.png' alt='Card image cap'>"
                + "<div class='col-sm-6'>"
                + "<h5 class='card-title text-success'>" + book.getName() + "</h5>"
                + "<p class='card-text'>"
                + "Author: <span class='text-primary' style='font-weight:bold;'> " + book.getAuthor() + "</span><br>"
                + "</p>"
                + "</div>"
                + "</div>"
                + "<div class='row card-body'>"
                + "<div class='col-sm-6'>"
                + "<p class='card-text'>"
                + "<span>Id: " + bCode + "</span>"
                + (bQty < 20 ? "<br><span class='text-danger'>Only " + bQty + " items left</span>"
                        : "<br><span class='text-success'>Trending</span>")
                + "</p>"
                + "</div>"
                + "<div class='col-sm-6'>"
                + "<p class='card-text'>"
                + "Price: <span style='font-weight:bold; color:green'> &#8377; " + book.getPrice() + " </span>"
                + "</p>"
                + button
                + "</div>"
                + "</div>"
                + "</div></div>";
    }
}
