package com.cbz.librarymanagementsystem.utils;

import org.springframework.mail.SimpleMailMessage;

import static com.cbz.librarymanagementsystem.utils.SystemConst.FORM_EMAIL;

public class DefaultMailMessage extends SimpleMailMessage {

    public DefaultMailMessage(String email,String subject,String sendInfo) {
        setFrom(FORM_EMAIL);
        setTo(email);
        setSubject(subject);
        setText(sendInfo);
    }

    public DefaultMailMessage() {
    }
}
