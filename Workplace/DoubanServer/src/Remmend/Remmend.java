package Remmend;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.*;
import javax.servlet.http.*;
import com.alibaba.fastjson.JSON;
import java.util.HashMap;
import java.util.Map;

public class Remmend extends HttpServlet {
    
    final String serveraddress = "47.103.3.188";
    
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection con = null;
        Statement st = null;
        ResultSet result = null;
        Map<String,String> map = new HashMap<>();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter pw = response.getWriter();
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + serveraddress + ":3306/user_info?&useSSL=false&serverTimezone=UTC","root","123456");
            st = con.createStatement();
            String strsql = "SELECT * FROM recommendedbook;";
            result = st.executeQuery(strsql);
            while(result.next()){
                int i = result.getInt("ranking");
                String str = result.getString("bookId");
                map.put(Integer.toString(i), str);
            }
            String jstr = JSON.toJSONString(map);
            pw.print(jstr);
            result.close();
            st.close();
            con.close();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}