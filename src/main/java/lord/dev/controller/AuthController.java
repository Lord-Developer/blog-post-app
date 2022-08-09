package lord.dev.controller;

import lord.dev.dto.request.UserLoginRequest;
import lord.dev.dto.request.UserRegisterRequest;
import lord.dev.dto.response.ApiResponse;
import lord.dev.exception.UserAlreadyRegisteredException;
import lord.dev.exception.UserAuthenticationException;
import lord.dev.sevice.CustomUserService;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final CustomUserService customUserService;

    public AuthController(CustomUserService customUserService) {
        this.customUserService = customUserService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegisterRequest userRegisterRequest) throws UserAlreadyRegisteredException {
        return ResponseEntity.ok(customUserService.registerUser(userRegisterRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequest userLoginRequest) throws UserAuthenticationException {
        return ResponseEntity.ok(customUserService.loginUser(userLoginRequest));
    }

    @GetMapping("/verifyEmail")
    public HttpEntity<?> verifyEmail(@RequestParam String emailCode, @RequestParam String email){
        ApiResponse apiResponse = customUserService.verifyEmail(emailCode, email);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


}

