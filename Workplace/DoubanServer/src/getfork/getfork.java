package getfork;

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

public class getfork extends HttpServlet {
    
    final String serveraddress = "47.103.3.188";
    
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection con = null;
        String userName = request.getParameter("userName");
        Statement st = null;
        ResultSet result = null;
        Map<String,String> map = new HashMap<>();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter pw = response.getWriter();
        int cnt = 0;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + serveraddress + ":3306/user_info?&useSSL=false&serverTimezone=UTC","root","123456");
            st = con.createStatement();
            String strsql = "SELECT * FROM bookinfo WHERE userName='" + userName + "';";
            System.out.println(strsql);
            result = st.executeQuery(strsql);
            while(result.next()){
                int serialNum = result.getInt("serialNum");
                String bookId = result.getString("bookId");
                String bookName = result.getString("bookName");
                String bookIsbn = result.getString("bookIsbn");
                map.put("'" + ++cnt + "'", bookId);
                map.put("'" + ++cnt + "'", bookName);
                map.put("'" + ++cnt + "'", bookIsbn);
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
