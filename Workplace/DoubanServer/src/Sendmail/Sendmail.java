package Sendmail;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

public class Sendmail extends HttpServlet {
    
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String address = request.getParameter("email");
        if(address.equals("") || address == null){
            return;
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter pw = response.getWriter();
        Map<String,String> map = new HashMap<>();
        Sendmail send = new Sendmail();
        String body = send.send(address);
        map.put("code", body);
        String jstr = JSON.toJSONString(map);
        pw.print(jstr); 
        pw.close();
    }

    private final String accessKey = "***";         //Is it interesting?
    private final String accessSecret = "***";
    private String email = null;
    
    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getAccessSecret() {
        return accessSecret;
    }

    public String send(String address){
        Sendmail sendMail = new Sendmail();
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", sendMail.getAccessKey(), sendMail.getAccessSecret());
        email = address;
        
        IAcsClient client = new DefaultAcsClient(profile);
        SingleSendMailRequest request = new SingleSendMailRequest();
        int randnum = (new Random()).nextInt(1000000);
        String rand = Integer.toString(randnum);
        try {
            request.setAccountName("willalso@www.fenggangguo.xyz");
            request.setFromAlias("豆瓣阅读");
            request.setAddressType(1);
            request.setTagName("WillAlso");
            request.setReplyToAddress(true);
            request.setToAddress(email);
            request.setSubject("豆瓣阅读邮箱绑定");
            String body = "您的验证码是" + rand;
            request.setHtmlBody(body);
            SingleSendMailResponse httpResponse = client.getAcsResponse(request);
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return rand;
    }
}
