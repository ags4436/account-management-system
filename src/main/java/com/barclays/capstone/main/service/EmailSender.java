package com.barclays.capstone.main.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * 
 * @author Divya Raisinghani, Harsh Das, Aakash Gouri Shankar
 * @Description Email Sender Service
 * 
 */


@Service
@Component
public class EmailSender {
	
	@Autowired
	private JavaMailSender mailSender;

	public void sendEmail(String toEmail,String subject,String body) {
        SimpleMailMessage msg=new SimpleMailMessage();
        System.out.println(msg);
        msg.setFrom("harshdas562@gmail.com");
        msg.setTo(toEmail);
        msg.setText(body);
        msg.setSubject(subject);
        System.out.println(msg);
        mailSender.send(msg);
        System.out.println("Mail sent succesfully..");
     }
}
