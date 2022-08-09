package lord.dev;

import lord.dev.model.Role;
import lord.dev.model.User;
import lord.dev.model.enums.ERole;
import lord.dev.repository.RoleRepository;
import lord.dev.repository.UserRepository;
import lord.dev.sevice.CustomUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class BlogPostAppApplication {



    public static void main(String[] args) {
        SpringApplication.run(BlogPostAppApplication.class, args);
    }


    @Bean
    CommandLineRunner runner(PasswordEncoder passwordEncoder,
                             UserRepository userRepository,
                             RoleRepository roleRepository) {
        return args -> {
            Role user_role = roleRepository.save(new Role(1, ERole.ROLE_USER));
            Role admin_role = roleRepository.save(new Role(2, ERole.ROLE_ADMIN));
            Set<Role> roles = new HashSet<>();
            roles.add(user_role);
            roles.add(admin_role);
            userRepository.save(new User(1,"Nodir",
                    "Lord_Dev","beknazarovking772@gmail.com",
                    passwordEncoder.encode("7272"),
                    roles,true,true,true,true, null
            ));
        };
    }
}
