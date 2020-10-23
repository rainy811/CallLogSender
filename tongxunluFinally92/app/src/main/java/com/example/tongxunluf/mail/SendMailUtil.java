package com.example.tongxunluf.mail;


import androidx.annotation.NonNull;

import java.io.File;

public class SendMailUtil {
    //qq
//    private static final String HOST = "smtp.qq.com";
//    private static final String PORT = "587";
//    private static final String FROM_ADD = "teprinciple@foxmail.com"; //发送方邮箱
//    private static final String FROM_PSW = "lfrlpganzjrwbeci";//发送方邮箱授权码

    //    //163
    private static final String HOST = "smtp.163.com";
    private static final String PORT = "465"; //或者465  994
    private static final String FROM_ADD = "zyx15021132158@163.com";
    private static final String FROM_PSW = "TTHSJCJAHCMCMNNB";

    public static void send(final File file, String toAdd,String content,String title){
        final MailInfo mailInfo = creatMail(toAdd,content,title);
        final MailSender sms = new MailSender();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sms.sendFileMail(mailInfo,file);
            }
        }).start();
    }

    /**
     * 添加邮箱地址，进行发送
     * @param toAdd 邮箱
     */
    public static void send(String toAdd,String content, String title){
        final MailInfo mailInfo = creatMail(toAdd,content,title);
        final MailSender sms = new MailSender();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sms.sendTextMail(mailInfo);
            }
        }).start();
    }

    /**
     *
     * @param toAdd
     * @param content
     * @param title
     * @return
     */
    @NonNull
    private static MailInfo creatMail(String toAdd,String content, String title) {
        final MailInfo mailInfo = new MailInfo();
        mailInfo.setMailServerHost(HOST);
        mailInfo.setMailServerPort(PORT);
        mailInfo.setValidate(true);
        mailInfo.setUserName(FROM_ADD); // 你的邮箱地址
        mailInfo.setPassword(FROM_PSW);// 您的邮箱密码
        mailInfo.setFromAddress(FROM_ADD); // 发送的邮箱
        mailInfo.setToAddress(toAdd); // 发到哪个邮件去
        mailInfo.setSubject(title); // 邮件主题
        mailInfo.setContent(content); // 邮件文本
        return mailInfo;
    }
}
