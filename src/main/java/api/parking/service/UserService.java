package api.parking.service;

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

    public User findByCpf(String cpf) {
        return repository.findByCpf(cpf).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public void saveUser(User user) {
         repository.save(user);
    }
}
