package kindle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.alibaba.fastjson.JSON;

public class kindle  extends HttpServlet{
    
    final String serveraddress = "47.103.3.188";
    
    private static final String ALIDM_SMTP_HOST = "smtpdm.aliyun.com";
    private static final String ALIDM_SMTP_PORT = "25";//或"80"
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("userName");
        String content = request.getParameter("content");
        String filename = request.getParameter("filename");
        String email = null;
        
        Connection con = null;
        Statement st = null;
        ResultSet result = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + serveraddress + ":3306/user_info?&useSSL=false&serverTimezone=UTC","root","123456");
            st = con.createStatement();
            String strsql = "SELECT email_kindle FROM userbasicinfo WHERE userName='"+
                    username + "';";
            result = st.executeQuery(strsql);
            if(result.next()){
                String str = result.getString("email_kindle");
                email = str;
            }
            System.out.println(email);
            result.close();
            st.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(email.equals("") || email == null){
            return;
        }
        File file = new File(filename);
        FileOutputStream fs = new FileOutputStream(file,true);
        OutputStreamWriter fw = new OutputStreamWriter(fs);
        fw.write(content);
        fw.flush();
        fw.close();
        
        PrintWriter pw = response.getWriter();
        if(file.exists()){
            pw.println("success");
            pw.print(file.getPath());
        }
        pw.close();
        sendmail(email,file.getPath());
    }
    
    public void sendmail(String email,String file){
final Properties props = new Properties();
        
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", ALIDM_SMTP_HOST);
        props.put("mail.smtp.port", ALIDM_SMTP_PORT);

        props.put("mail.user", "willalso@www.fenggangguo.xyz");

        props.put("mail.password", "***");

        Authenticator authenticator = new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };

        Session mailSession = Session.getInstance(props, authenticator);

        MimeMessage message = new MimeMessage(mailSession){

        };
        try {
        InternetAddress from = new InternetAddress("willalso@www.fenggangguo.xyz", "WillAlso");
        message.setFrom(from);
        Address[] a = new Address[1];
        a[0] = new InternetAddress("***");
        message.setReplyTo(a);
        // 设置收件人邮件地址，比如yyy@yyy.com
        InternetAddress to = new InternetAddress(email);
        message.setRecipient(MimeMessage.RecipientType.TO, to);
        
        String ccUser = "1481660657@qq.com";
            // 设置多个抄送地址
            if(null != ccUser && !ccUser.isEmpty()){
                @SuppressWarnings("static-access")
                InternetAddress[] internetAddressCC = new InternetAddress().parse(ccUser);
                message.setRecipients(Message.RecipientType.CC, internetAddressCC);
            }
            String bccUser = "1481660657@qq.com";
            // 设置多个密送地址
            if(null != bccUser && !bccUser.isEmpty()){
                @SuppressWarnings("static-access")
                InternetAddress[] internetAddressBCC = new InternetAddress().parse(bccUser);
                message.setRecipients(Message.RecipientType.BCC, internetAddressBCC);
            }
        // 设置邮件标题
        message.setSubject("测试邮件");
        // 设置邮件的内容体
        message.setContent("测试的HTML邮件", "text/html;charset=UTF-8");
        //若需要开启邮件跟踪服务，请使用以下代码设置跟踪链接头。首先域名需要备案，设置且已正确解析了CNAME配置；其次发信需要打Tag，此Tag在控制台已创建并存在，Tag创建10分钟后方可使用；
        //String tagName = "Test";
        //HashMap<String, String> trace = new HashMap<>();
        //trace.put("OpenTrace", "1");
        //trace.put("TagName", tagName);
        //String jsonTrace = JSON.toJSONString(trace);
        //String base64Trace = new String(Base64.encodeBase64(jsonTrace.getBytes()));
        //设置跟踪链接头
        //message.addHeader("X-AliDM-Trace", base64Trace);
        // 发送附件，总的邮件大小不超过15M，创建消息部分
        BodyPart messageBodyPart = new MimeBodyPart();
        // 消息
        messageBodyPart.setText("消息Text");
        // 创建多重消息
        Multipart multipart = new MimeMultipart();
        // 设置文本消息部分
        multipart.addBodyPart(messageBodyPart);
        // 附件部分
        messageBodyPart = new MimeBodyPart();
        //设置要发送附件的文件路径
        String filename = file;
        FileDataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        //处理附件名称中文（附带文件路径）乱码问题
        messageBodyPart.setFileName(MimeUtility.encodeText(filename));
        multipart.addBodyPart(messageBodyPart);
        // 发送含有附件的完整消息
        message.setContent(multipart);
        // 发送附件代码，结束
        // 发送邮件
        Transport.send(message);
        }
        catch (MessagingException | UnsupportedEncodingException e) {
            String err = e.getMessage();
            // 在这里处理message内容， 格式是固定的
            System.out.println(err);
        }
        File f = new File(file);
        if(f.exists()){
            if(f.isFile()){
            f.delete();
            }
        }
    }
    
}
