package com.quicknote.service;

import com.quicknote.CustomUserDetails;
import com.quicknote.dto.AuthRequest;
import com.quicknote.dto.AuthResponse;
import com.quicknote.model.User;
import com.quicknote.repository.UserRepository;
import com.quicknote.security.JwtUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    //    public User registerUser(User user){
//
//        if(userRepository.existsByEmail(user.getEmail())){
//            return ResponseEntity.ok("User Allready Exist");
//        }
//
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        this.userRepository.save(user);
//        return ResponseEntity.ok("User register successfully ...");
//
////        if (userRepository.existsByEmail(user.getEmail())){
////            return ;
////        }
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        return this.userRepository.save(user);
//    }
    public String registerUser(User user) {

        System.out.println("registerUser called "+"???????????"+user.getName()+">>>>>>>"+user.getEmail()+">>>>>>>"+user.getPassword()+">>>>>>>"+"??????????????");
        if (userRepository.existsByEmail(user.getEmail())) {
            System.out.println("User Allready Exist");
            return "User Allready Exist";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        this.userRepository.save(user);
        System.out.println("registerUser called "+"???????????"+user.getName()+">>>>>>>"+user.getEmail()+">>>>>>>"+user.getPassword()+">>>>>>>"+"??????????????");

        return "User register successfully ...";
    }

//    public AuthResponse loginUser(AuthRequest authRequest) {
//        Optional<User> user = userRepository.findByEmail(authRequest.getEmail());
//        if (user.isPresent() && passwordEncoder.matches(authRequest.getPassword(), user.get().getPassword())) {
//            String token = jwtUtil.generateToken(authRequest.getEmail());
//            return new AuthResponse(token);
//        }
//        throw new RuntimeException("Invalid email password");
//    }

    public AuthResponse loginUser(AuthRequest authRequest) {
//        Optional<User> user = userRepository.findByEmail(authRequest.getEmail());
//        if (user.isPresent() && passwordEncoder.matches(authRequest.getPassword(), user.get().getPassword())) {
//            String token = jwtUtil.generateToken(authRequest.getEmail());
//            return new AuthResponse(token);
//        }
//        throw new RuntimeException("Invalid email password");

        Optional<User> user = userRepository.findByEmail(authRequest.getEmail());
        try {

            if (user.isPresent() && passwordEncoder.matches(authRequest.getPassword(), user.get().getPassword())) {
                String token = jwtUtil.generateToken(authRequest.getEmail());
                return new AuthResponse(token);
            }
        }catch (Exception e){
            return new AuthResponse("Invalid User!!!!!!!!!!!!!!!!");
        }
        return new AuthResponse("Invalid User");
//           throw new RuntimeException("Invalid email password");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =   userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"+ username));
        return new CustomUserDetails(user.getEmail(), user.getPassword(), user.getAuthorities());
    }



}


