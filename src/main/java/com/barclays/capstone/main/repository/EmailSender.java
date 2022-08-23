package com.barclays.capstone.main.repository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


@Component
public class EmailSender {
	
	@Autowired
	private JavaMailSender mailSender;

	void sendEmail(String toEmail,String subject,String body) {
        SimpleMailMessage msg=new SimpleMailMessage();
        msg.setFrom("harshdas562@gmail.com");
        msg.setTo(toEmail);
        msg.setText(body);
        msg.setSubject(subject);
        mailSender.send(msg);
        System.out.println("Mail sent succesfully..");
     }
}
