package com.ysy.basetools.mail;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by Administrator on 2018/5/26.
 */
public class MailUtil {


    public static boolean sendSimpleMail(MailInfo info, Properties properties, Authenticator authenticator) {
        Session session = Session.getDefaultInstance(properties, authenticator);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(info.getSendMailAddress()));//发送人

            //收件人
            if (info.getRecMailAddress() != null) {
                for (String address : info.getRecMailAddress()) {
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(address));
                }
            }
            //抄送人
            if (info.getCopyRecMailAddress() != null) {
                for (String address : info.getCopyRecMailAddress()) {
                    message.addRecipient(Message.RecipientType.CC, new InternetAddress(address));
                }
            }
            //密送人
            if (info.getSecretRecMailAddress() != null) {
                for (String address : info.getSecretRecMailAddress()) {
                    message.addRecipient(Message.RecipientType.BCC, new InternetAddress(address));
                }
            }

            //设置头部
            message.setSubject(info.getTitle());

            //设置内容
            message.setText(info.getContent());

            // 发送消息
            Transport.send(message);
            return true;
        } catch (MessagingException mex) {
            throw new RuntimeException(mex);
        }
    }
}
