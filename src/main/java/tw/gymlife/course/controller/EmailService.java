//package tw.gymlife.course.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//@Service
//public class EmailService {
//
//    @Autowired
//    private JavaMailSender javaMailSender;
//
//    public void sendTestEmail() {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo("zerorich1225@gmail.com");
//        message.setSubject("Test Email");
//        message.setText("This is a test email.");
//
//        javaMailSender.send(message);
//    }
//}
