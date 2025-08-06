package api.parking.repository;

import api.parking.entities.User;
import api.parking.entities.UserRole;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Deve retornar um usuario com esse cpf")
    void findByCpfCaso1() {
        String cpf = "112233";
        User user = new User(1L, "Gustavo", "gmail.com", "123", cpf, UserRole.COMMON);
        this.createUser(user);

        Optional<User> result = userRepository.findByCpf(cpf);
        assertThat(result.isPresent()).isTrue();
    }


    @Test
    @DisplayName("Não deve retornar um usuario se não existir")
    void findByCpfCaso2() {
        String cpf = "112233";

        Optional<User> result = userRepository.findByCpf(cpf);
        assertThat(result.isEmpty()).isTrue();
    }

    private User createUser(User user) {
        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        newUser.setCpf(user.getCpf());
        newUser.setRole(user.getRole());
        this.entityManager.persist(newUser);
        return newUser;
    }
}