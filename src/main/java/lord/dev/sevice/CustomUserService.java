package lord.dev.sevice;

import lord.dev.dto.request.UserLoginRequest;
import lord.dev.dto.request.UserRegisterRequest;
import lord.dev.dto.response.ApiResponse;
import lord.dev.email.service.EmailService;
import lord.dev.exception.UserAlreadyRegisteredException;
import lord.dev.exception.UserInvalidPasswordException;
import lord.dev.exception.UserNotFoundException;
import lord.dev.model.User;
import lord.dev.model.enums.ERole;
import lord.dev.repository.RoleRepository;
import lord.dev.repository.UserRepository;
import lord.dev.security.jwt.JWTokenProvider;
import lord.dev.security.response.JWTokenResponse;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

@Service
public class CustomUserService {

    private final static Logger logger = LoggerFactory.getLogger(CustomUserService.class);


    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;
    private final JWTokenProvider jwTokenProvider;
    private final EmailService emailService;

    public CustomUserService(RoleRepository roleRepository, PasswordEncoder passwordEncoder, UserRepository userRepository, AuthenticationManager authenticationManager, ModelMapper modelMapper, JWTokenProvider jwTokenProvider, EmailService emailService) {
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.modelMapper = modelMapper;
        this.jwTokenProvider = jwTokenProvider;
        this.emailService = emailService;
    }

    public JWTokenResponse registerUser(UserRegisterRequest userRegisterRequest) {
        String[] tokens = new String[2];
        User user = new User();

        try {
            if(userRepository.existsByEmail(userRegisterRequest.getEmail())){
                throw new UserAlreadyRegisteredException(
                        "User already registered with : " + userRegisterRequest.getEmail()
                );
            }
            userRegisterRequest.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));
            user.setRoles(Collections.singleton(roleRepository.findByName(ERole.ROLE_USER)));
            user.setUsername(userRegisterRequest.getEmail().substring(0, userRegisterRequest.getEmail().indexOf("@")));
            user.setEmailCode(UUID.randomUUID().toString());
            modelMapper.map(userRegisterRequest, user);
            User entity = userRepository.save(user);
            tokens = jwTokenProvider.generateJwtTokens(entity);
            emailService.sendMessage(user.getEmail(), "Verify Email",  entity.getEmailCode());

        } catch (
                IllegalArgumentException | ClassCastException | IllegalStateException |
                        InvalidDataAccessApiUsageException e)
        {
            logger.error(e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());

        }

        return new JWTokenResponse(OK.value(), OK.name(), tokens[0], tokens[1]);
    }

    public JWTokenResponse loginUser(UserLoginRequest userLoginRequest) {

        User user = userRepository.findByUsernameOrEmail(userLoginRequest.getUsernameOrEmail(), userLoginRequest.getUsernameOrEmail())
                .orElseThrow(
                        () -> new UserNotFoundException(
                                "User not found with : " + userLoginRequest.getUsernameOrEmail()
                        )
                );

        if (passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
            String[] tokens = jwTokenProvider.generateJwtTokens(user);
            return new JWTokenResponse(OK.value(), OK.name(), tokens[0], tokens[1]);
        } else {
            throw new UserInvalidPasswordException("Wrong password");

        }
    }

    public ApiResponse verifyEmail(String emailCode, String email) {
        Optional<User> byEmailAndEmailCode = userRepository.findByEmailAndEmailCode(email, emailCode);
        if(byEmailAndEmailCode.isPresent()){
            User user = byEmailAndEmailCode.get();
            user.setEnabled(true);
            user.setEmailCode(null);
            userRepository.save(user);
            return new ApiResponse("Email is verified!", true);
        }
        return new ApiResponse("Email is already verified!", false);

    }

}
