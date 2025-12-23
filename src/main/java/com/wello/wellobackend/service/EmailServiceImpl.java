package com.wello.wellobackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendOtpEmail(String toEmail, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Wello - Mã xác thực đăng ký");

            String htmlContent = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<link href='https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap' rel='stylesheet'>"
                    +
                    "<style>" +
                    "body { font-family: 'Poppins', Arial, sans-serif; background-color: #faf8f3; margin: 0; padding: 0; }"
                    +
                    ".container { max-width: 600px; margin: 40px auto; background-color: white; border-radius: 16px; box-shadow: 0 8px 24px rgba(139, 90, 43, 0.15); overflow: hidden; }"
                    +
                    ".header { background: linear-gradient(135deg, #d4a574 0%, #8b5a2b 100%); padding: 50px 20px; text-align: center; }"
                    +
                    ".header h1 { color: white; margin: 0; font-size: 32px; font-weight: 700; text-shadow: 2px 2px 4px rgba(0,0,0,0.2); }"
                    +
                    ".header p { color: #fff9e6; margin: 10px 0 0 0; font-size: 14px; }" +
                    ".content { padding: 50px 40px; }" +
                    ".greeting { color: #8b5a2b; font-size: 18px; font-weight: 600; margin-bottom: 15px; }" +
                    ".message { color: #4a4a4a; font-size: 16px; line-height: 1.8; margin: 15px 0; }" +
                    ".otp-container { background: linear-gradient(135deg, #fff9e6 0%, #ffeaa7 100%); border: 3px dashed #d4a574; border-radius: 12px; padding: 30px; margin: 35px 0; text-align: center; }"
                    +
                    ".otp-label { color: #8b5a2b; font-size: 14px; font-weight: 600; text-transform: uppercase; letter-spacing: 1px; margin-bottom: 10px; }"
                    +
                    ".otp-code { background: linear-gradient(135deg, #d4a574 0%, #8b5a2b 100%); color: white; font-size: 42px; font-weight: 700; letter-spacing: 12px; padding: 20px 30px; border-radius: 8px; display: inline-block; box-shadow: 0 4px 12px rgba(139, 90, 43, 0.3); }"
                    +
                    ".timer { background-color: #fff3cd; border-left: 4px solid #d4a574; padding: 15px 20px; margin: 25px 0; border-radius: 6px; }"
                    +
                    ".timer-text { color: #8b5a2b; font-size: 15px; margin: 0; }" +
                    ".timer-text strong { color: #6d4c2a; font-weight: 700; }" +
                    ".warning { color: #6c757d; font-size: 14px; font-style: italic; margin-top: 25px; }" +
                    ".footer { background: linear-gradient(to right, #faf8f3, #fff9e6); padding: 30px; text-align: center; border-top: 2px solid #f0e6d2; }"
                    +
                    ".footer-brand { color: #8b5a2b; font-size: 18px; font-weight: 700; margin-bottom: 10px; }" +
                    ".footer-text { color: #6c757d; font-size: 13px; line-height: 1.6; margin: 8px 0; }" +
                    ".divider { height: 2px; background: linear-gradient(to right, transparent, #d4a574, transparent); margin: 25px 0; }"
                    +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='container'>" +
                    "<div class='header'>" +
                    "<h1>🌟 Wello</h1>" +
                    "<p>Hành trình sức khỏe của bạn bắt đầu từ đây</p>" +
                    "</div>" +
                    "<div class='content'>" +
                    "<p class='greeting'>Xin chào! 👋</p>" +
                    "<p class='message'>Cảm ơn bạn đã tin tưởng và lựa chọn <strong>Wello</strong> để đồng hành cùng hành trình chăm sóc sức khỏe. Để hoàn tất đăng ký, vui lòng sử dụng mã xác thực bên dưới:</p>"
                    +
                    "<div class='otp-container'>" +
                    "<div class='otp-label'>Mã xác thực của bạn</div>" +
                    "<div class='otp-code'>" + otp + "</div>" +
                    "</div>" +
                    "<div class='timer'>" +
                    "<p class='timer-text'>⏰ Mã OTP này có hiệu lực trong <strong>5 phút</strong>. Vui lòng nhập mã trước khi hết hạn.</p>"
                    +
                    "</div>" +
                    "<div class='divider'></div>" +
                    "<p class='warning'>⚠️ Nếu bạn không yêu cầu mã này, vui lòng bỏ qua email này. Tài khoản của bạn vẫn an toàn.</p>"
                    +
                    "</div>" +
                    "<div class='footer'>" +
                    "<p class='footer-brand'>Wello - Sống khỏe mỗi ngày</p>" +
                    "<p class='footer-text'>Email này được gửi tự động, vui lòng không trả lời.</p>" +
                    "<p class='footer-text' style='margin-top: 15px; color: #999; font-size: 11px;'>© 2024 Wello. All rights reserved.</p>"
                    +
                    "</div>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            helper.setText(htmlContent, true);
            mailSender.send(message);
            System.out.println("OTP email sent successfully to: " + toEmail);
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
            throw new RuntimeException("Không thể gửi email OTP. Vui lòng thử lại sau.");
        }
    }

    @Override
    public void sendPasswordResetEmail(String toEmail, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Wello - Mã xác thực đặt lại mật khẩu");

            String htmlContent = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<link href='https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap' rel='stylesheet'>"
                    +
                    "<style>" +
                    "body { font-family: 'Poppins', Arial, sans-serif; background-color: #faf8f3; margin: 0; padding: 0; }"
                    +
                    ".container { max-width: 600px; margin: 40px auto; background-color: white; border-radius: 16px; box-shadow: 0 8px 24px rgba(139, 90, 43, 0.15); overflow: hidden; }"
                    +
                    ".header { background: linear-gradient(135deg, #d4a574 0%, #8b5a2b 100%); padding: 50px 20px; text-align: center; }"
                    +
                    ".header h1 { color: white; margin: 0; font-size: 32px; font-weight: 700; text-shadow: 2px 2px 4px rgba(0,0,0,0.2); }"
                    +
                    ".header p { color: #fff9e6; margin: 10px 0 0 0; font-size: 14px; }" +
                    ".content { padding: 50px 40px; }" +
                    ".greeting { color: #8b5a2b; font-size: 18px; font-weight: 600; margin-bottom: 15px; }" +
                    ".message { color: #4a4a4a; font-size: 16px; line-height: 1.8; margin: 15px 0; }" +
                    ".otp-container { background: linear-gradient(135deg, #fff9e6 0%, #ffeaa7 100%); border: 3px dashed #d4a574; border-radius: 12px; padding: 30px; margin: 35px 0; text-align: center; }"
                    +
                    ".otp-label { color: #8b5a2b; font-size: 14px; font-weight: 600; text-transform: uppercase; letter-spacing: 1px; margin-bottom: 10px; }"
                    +
                    ".otp-code { background: linear-gradient(135deg, #d4a574 0%, #8b5a2b 100%); color: white; font-size: 42px; font-weight: 700; letter-spacing: 12px; padding: 20px 30px; border-radius: 8px; display: inline-block; box-shadow: 0 4px 12px rgba(139, 90, 43, 0.3); }"
                    +
                    ".timer { background-color: #fff3cd; border-left: 4px solid #d4a574; padding: 15px 20px; margin: 25px 0; border-radius: 6px; }"
                    +
                    ".timer-text { color: #8b5a2b; font-size: 15px; margin: 0; }" +
                    ".timer-text strong { color: #6d4c2a; font-weight: 700; }" +
                    ".warning { color: #6c757d; font-size: 14px; font-style: italic; margin-top: 25px; }" +
                    ".footer { background: linear-gradient(to right, #faf8f3, #fff9e6); padding: 30px; text-align: center; border-top: 2px solid #f0e6d2; }"
                    +
                    ".footer-brand { color: #8b5a2b; font-size: 18px; font-weight: 700; margin-bottom: 10px; }" +
                    ".footer-text { color: #6c757d; font-size: 13px; line-height: 1.6; margin: 8px 0; }" +
                    ".divider { height: 2px; background: linear-gradient(to right, transparent, #d4a574, transparent); margin: 25px 0; }"
                    +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='container'>" +
                    "<div class='header'>" +
                    "<h1>🔐 Wello</h1>" +
                    "<p>Đặt lại mật khẩu của bạn</p>" +
                    "</div>" +
                    "<div class='content'>" +
                    "<p class='greeting'>Xin chào! 👋</p>" +
                    "<p class='message'>Chúng tôi nhận được yêu cầu đặt lại mật khẩu cho tài khoản <strong>Wello</strong> của bạn. Để tiếp tục, vui lòng sử dụng mã xác thực bên dưới:</p>"
                    +
                    "<div class='otp-container'>" +
                    "<div class='otp-label'>Mã xác thực của bạn</div>" +
                    "<div class='otp-code'>" + otp + "</div>" +
                    "</div>" +
                    "<div class='timer'>" +
                    "<p class='timer-text'>⏰ Mã OTP này có hiệu lực trong <strong>5 phút</strong>. Vui lòng nhập mã trước khi hết hạn.</p>"
                    +
                    "</div>" +
                    "<div class='divider'></div>" +
                    "<p class='warning'>⚠️ Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này. Tài khoản của bạn vẫn an toàn và không có thay đổi nào được thực hiện.</p>"
                    +
                    "</div>" +
                    "<div class='footer'>" +
                    "<p class='footer-brand'>Wello - Sống khỏe mỗi ngày</p>" +
                    "<p class='footer-text'>Email này được gửi tự động, vui lòng không trả lời.</p>" +
                    "<p class='footer-text' style='margin-top: 15px; color: #999; font-size: 11px;'>© 2024 Wello. All rights reserved.</p>"
                    +
                    "</div>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            helper.setText(htmlContent, true);
            mailSender.send(message);
            System.out.println("Password reset email sent successfully to: " + toEmail);
        } catch (Exception e) {
            System.err.println("Error sending password reset email: " + e.getMessage());
            throw new RuntimeException("Không thể gửi email đặt lại mật khẩu. Vui lòng thử lại sau.");
        }
    }
}
