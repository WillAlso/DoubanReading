package adduser;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.alibaba.fastjson.JSON;

public class adduser extends HttpServlet{
final String serveraddress = "47.103.3.188";
    
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");;
        String email1 = request.getParameter("email1");;
        String email2 = request.getParameter("email2");;
        Connection con = null;
        Statement st = null;
        boolean result = false;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter pw = response.getWriter();
        String strsql = "INSERT INTO userbasicinfo(serialNum,userName,email,"
                + "email_kindle,isVIP) VALUE (0,'" + username + 
                "','" + email1 + "','" + email2 +"',0);";
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
