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
    private static final String ALIDM_SMTP_PORT = "25";//��"80"
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

        props.put("mail.password", "TsRl5201314");

        Authenticator authenticator = new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                // �û���������
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
        // �����ռ����ʼ���ַ������yyy@yyy.com
        InternetAddress to = new InternetAddress(email);
        message.setRecipient(MimeMessage.RecipientType.TO, to);
        
        String ccUser = "1481660657@qq.com";
            // ���ö�����͵�ַ
            if(null != ccUser && !ccUser.isEmpty()){
                @SuppressWarnings("static-access")
                InternetAddress[] internetAddressCC = new InternetAddress().parse(ccUser);
                message.setRecipients(Message.RecipientType.CC, internetAddressCC);
            }
            String bccUser = "1481660657@qq.com";
            // ���ö�����͵�ַ
            if(null != bccUser && !bccUser.isEmpty()){
                @SuppressWarnings("static-access")
                InternetAddress[] internetAddressBCC = new InternetAddress().parse(bccUser);
                message.setRecipients(Message.RecipientType.BCC, internetAddressBCC);
            }
        // �����ʼ�����
        message.setSubject("�����ʼ�");
        // �����ʼ���������
        message.setContent("���Ե�HTML�ʼ�", "text/html;charset=UTF-8");
        //����Ҫ�����ʼ����ٷ�����ʹ�����´������ø�������ͷ������������Ҫ����������������ȷ������CNAME���ã���η�����Ҫ��Tag����Tag�ڿ���̨�Ѵ��������ڣ�Tag����10���Ӻ󷽿�ʹ�ã�
        //String tagName = "Test";
        //HashMap<String, String> trace = new HashMap<>();
        //trace.put("OpenTrace", "1");
        //trace.put("TagName", tagName);
        //String jsonTrace = JSON.toJSONString(trace);
        //String base64Trace = new String(Base64.encodeBase64(jsonTrace.getBytes()));
        //���ø�������ͷ
        //message.addHeader("X-AliDM-Trace", base64Trace);
        // ���͸������ܵ��ʼ���С������15M��������Ϣ����
        BodyPart messageBodyPart = new MimeBodyPart();
        // ��Ϣ
        messageBodyPart.setText("��ϢText");
        // ����������Ϣ
        Multipart multipart = new MimeMultipart();
        // �����ı���Ϣ����
        multipart.addBodyPart(messageBodyPart);
        // ��������
        messageBodyPart = new MimeBodyPart();
        //����Ҫ���͸������ļ�·��
        String filename = file;
        FileDataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        //�������������ģ������ļ�·������������
        messageBodyPart.setFileName(MimeUtility.encodeText(filename));
        multipart.addBodyPart(messageBodyPart);
        // ���ͺ��и�����������Ϣ
        message.setContent(multipart);
        // ���͸������룬����
        // �����ʼ�
        Transport.send(message);
        }
        catch (MessagingException | UnsupportedEncodingException e) {
            String err = e.getMessage();
            // �����ﴦ��message���ݣ� ��ʽ�ǹ̶���
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
