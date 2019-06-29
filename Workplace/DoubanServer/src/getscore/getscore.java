package getscore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.alibaba.fastjson.JSON;

public class getscore extends HttpServlet {
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter pw = response.getWriter();
        String bookId = request.getParameter("bookId");
        url = new URL("https://book.douban.com/subject/" + bookId + "/");
        try {
            get_content();
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashMap<String, String> map = new HashMap<>();
        for (int k = 5; k > 0; k--) {
            map.put(Integer.toString(k), info[k - 1].substring(0, info[k - 1].length() - 1));
        }
        String jstr = JSON.toJSONString(map);
        pw.print(jstr);
        pw.close();
    }

    String[] info;
    URL url;

    public void get_content() throws Exception {
        
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
        String s = null;
        StringBuffer result = new StringBuffer();
        while ((s = br.readLine()) != null) {
            result.append(s + "\n");
        }
        int startPos = result.indexOf("<span class=\"rating_per\">");
        int endPos = result.indexOf("<div class=\"gtleft\">");
        StringBuffer temp1 = new StringBuffer(result.substring(startPos, endPos));
        int cnt = 0;
        int i = 0;
        info = new String[5];
        while (true) {
            int start = temp1.indexOf("rating_per");
            if (start < 0 || cnt > 4) {
                break;
            }
            temp1 = new StringBuffer(temp1.substring(start));
            int end = temp1.indexOf("<");
            String t = temp1.substring(12, end);
            temp1 = new StringBuffer(temp1.substring(end, temp1.length()));
            if (i < 5) {
                info[i] = t;
                i++;
            }
            cnt++;
        }
        for (String str : info) {
            System.out.println(str);
        }
        

    }
}
