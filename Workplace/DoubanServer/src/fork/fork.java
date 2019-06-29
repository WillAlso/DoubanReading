package fork;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class fork extends HttpServlet {
    
final String serveraddress = "47.103.3.188";
    
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("userName");
        String bookId = request.getParameter("bookId");
        String bookName = request.getParameter("bookName");
        String bookIsbn = request.getParameter("bookIsbn");
        Connection con = null;
        Statement st = null;
        boolean result = false;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter pw = response.getWriter();
        String strsql = "INSERT INTO bookinfo(serialNum,userName,bookId,"
                + "bookName,bookIsbn) VALUE (0,'" + username + 
                "','" + bookId + "','" + bookName +"','" + bookIsbn + "');";
        pw.println(strsql);
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + serveraddress + ":3306/user_info?&useSSL=false&serverTimezone=UTC","root","123456");
            st = con.createStatement();
            result = st.execute(strsql);
            
            st.close();
            con.close();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
