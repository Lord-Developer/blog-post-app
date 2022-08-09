package lord.dev.email.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Service
public class EmailService {


    private final JavaMailSender javaMailSender;

    @Value("${email.from.address}")
    private String fromAddress;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMessage(String toEmail, String subject, String emailCode) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(fromAddress);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText("<a href = 'http://ec2-174-129-154-224.compute-1.amazonaws.com:8000/api/auth/verifyEmail?emailCode=" + emailCode +"&email="+toEmail + "'>Verify Email</a>");

        javaMailSender.send(mimeMessage);
    }


}
