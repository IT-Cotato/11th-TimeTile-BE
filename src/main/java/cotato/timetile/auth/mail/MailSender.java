package cotato.timetile.auth.mail;

import cotato.timetile.global.properties.MailProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailSender {
    
    private final MailProperties mailProperties;
    private final JavaMailSender javaMailSender;

    @Async("mailTaskExecutor")
    public void sendMail(String recipient, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipient);
        message.setSubject("[timetile] 회원가입 인증 코드 안내드립니다.");
        message.setText(generateText(verificationCode));
        message.setFrom(mailProperties.username());
        javaMailSender.send(message);
    }

    private String generateText(String verificationCode) {
        return String.format(
                "안녕하세요.\n" +
                        "timetile 서비스를 이용해 주셔서 감사합니다.\n" +
                        "요청하신 회원가입 인증 코드를 아래에 안내드립니다.\n\n" +
                        "인증 코드: %s\n\n" +
                        "해당 인증 코드를 회원가입 페이지에 입력하여 인증을 완료해주세요.\n" +
                        "*인증 코드는 발송 후 3분간 유효합니다.\n" +
                        "*본 메일은 발신 전용입니다.",
                verificationCode
        );
    }

}
