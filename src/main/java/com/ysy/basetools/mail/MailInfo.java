package com.ysy.basetools.mail;

import java.util.List;

/**
 * Created by Administrator on 2018/5/26.
 */
public class MailInfo {
    private String title;//邮件的主题
    private String sendMailAddress;//邮件发送人
    private List<String> recMailAddress;//邮件接收人
    private List<String> copyRecMailAddress;//邮件抄送收人
    private List<String> secretRecMailAddress;//邮件密送人
    private String content;//邮件内容

    public List<String> getCopyRecMailAddress() {
        return copyRecMailAddress;
    }

    public void setCopyRecMailAddress(List<String> copyRecMailAddress) {
        this.copyRecMailAddress = copyRecMailAddress;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSendMailAddress() {
        return sendMailAddress;
    }

    public void setSendMailAddress(String sendMailAddress) {
        this.sendMailAddress = sendMailAddress;
    }

    public List<String> getRecMailAddress() {
        return recMailAddress;
    }

    public void setRecMailAddress(List<String> recMailAddress) {
        this.recMailAddress = recMailAddress;
    }

    public List<String> getSecretRecMailAddress() {
        return secretRecMailAddress;
    }

    public void setSecretRecMailAddress(List<String> secretRecMailAddress) {
        this.secretRecMailAddress = secretRecMailAddress;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
