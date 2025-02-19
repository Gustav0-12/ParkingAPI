package api.parking.service;

import api.parking.dto.UserResponseDTO;
import api.parking.entities.User;
import api.parking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    public User findByEmail(String email) {
        return (User) repository.findByEmail(email);
    }

    public void saveUser(User user) {
        User savedUser = repository.save(user);
        new UserResponseDTO(savedUser.getId(), savedUser.getName(), savedUser.getEmail(), savedUser.getPassword(), savedUser.getRole());
    }
}
