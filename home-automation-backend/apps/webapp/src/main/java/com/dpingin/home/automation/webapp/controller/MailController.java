package com.dpingin.home.automation.webapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Properties;

@RestController
@RequestMapping("/mail")
public class MailController
{
	@RequestMapping(value = "/mail", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void mail(@RequestBody Map<String, String> data) throws MessagingException
	{
		String to = data.get("to");
		String from = data.get("from");
		String host = data.get("host");
		String port = data.get("port");
		final String login = data.get("login");
		final String password = data.get("password");

		Properties properties = new Properties();
		properties.put("mail.smtp.from", from);
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", port);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");

		Session session = Session.getDefaultInstance(properties, new Authenticator()
		{

			@Override
			protected PasswordAuthentication getPasswordAuthentication()
			{
				return new PasswordAuthentication(login, password);
			}

		});

		MimeMessage message = new MimeMessage(session);

		message.setFrom(new InternetAddress(from));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

		message.setSubject(data.get("subject"));
		message.setText(data.get("text"));

		Transport.send(message);
	}
}
