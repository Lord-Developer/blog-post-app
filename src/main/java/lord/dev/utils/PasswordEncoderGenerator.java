package lord.dev.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

public class PasswordEncoderGenerator {

    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println("password");
        System.out.println(passwordEncoder.encode("password"));
        System.out.println("admin");
        System.out.println(passwordEncoder.encode("admin"));

    }
}
