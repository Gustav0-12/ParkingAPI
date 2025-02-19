package api.parking.controller;

import api.parking.dto.LoginDTO;
import api.parking.dto.RegisterDTO;
import api.parking.dto.TokenDTO;
import api.parking.dto.UserResponseDTO;
import api.parking.entities.User;
import api.parking.security.TokenService;
import api.parking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenService tokenService;

    @Autowired
    UserService userService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDTO data) {
        try {
            var login = new UsernamePasswordAuthenticationToken(data.email(), data.password());
            var authentication = authenticationManager.authenticate(login);

            var token = tokenService.generateToken((User) authentication.getPrincipal());
            return ResponseEntity.ok(new TokenDTO(token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody RegisterDTO data) {
        if (userService.findByEmail(data.email()) != null) {
            throw new RuntimeException("Email já cadastrado");
        }

        var encryptedPassword = passwordEncoder.encode(data.password());

        User newUser = new User();
        newUser.setName(data.name());
        newUser.setEmail(data.email());
        newUser.setPassword(encryptedPassword);
        newUser.setCpf(data.cpf());
        newUser.setRole(data.role());
        userService.saveUser(newUser);

        return ResponseEntity.ok(new UserResponseDTO(newUser.getId(), newUser.getName(), newUser.getEmail(), newUser.getCpf(), newUser.getRole()));
    }
}
