package com.quicknote.controller;

import com.quicknote.dto.AuthRequest;
import com.quicknote.dto.AuthResponse;
import com.quicknote.dto.UserDTO;
import com.quicknote.model.User;
import com.quicknote.repository.UserRepository;
import com.quicknote.security.JwtUtil;
import com.quicknote.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name="Auth API", description = "Signup and Login API")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, UserService userService, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

//    @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody User user){
//        if(userRepository.existsByEmail(user.getEmail())){
//            return ResponseEntity.ok("User Allready Exist");
//        }
//
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        this.userRepository.save(user);
//        return ResponseEntity.ok("User register successfully ...");
//    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO user){
        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        return ResponseEntity.ok(this.userService.registerUser(newUser));
    }


//    @PostMapping("/login")
//    public AuthResponse login(@RequestBody AuthRequest authRequest){
//        Optional<User> user = userRepository.findByEmail(authRequest.getEmail());
//        if(user.isPresent() && passwordEncoder.matches(authRequest.getPassword(), user.get().getPassword())){
//            String token = jwtUtil.generateToken(authRequest.getEmail());
//            return new AuthResponse(token);
//        }
//        throw new RuntimeException("Invalid email password");
//    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest authRequest){
       return this.userService.loginUser(authRequest);
    }
}
