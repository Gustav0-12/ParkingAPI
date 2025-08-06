package api.parking.repository;

import api.parking.entities.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UserParkingSpotRepositoryTest {

    @Autowired
    UserParkingSpotRepository userParkingSpotRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Deve retornar o estacionamento se existe um registro com recibo e data de saída null")
    void findByReciboAndDataSaidaIsNullCaso1() {
        String recibo = "90903030";
        var user = createUser(new User(null, "Gustavo", "gmail.com", "123", "003344", UserRole.COMMON));
        var parkingSpot = createSpot(new ParkingSpot(null, "P-00", Status.OCCUPIED, LocalDateTime.now()));
        var userParking = createUserParking(new UserParkingSpot(null, "Fiat", "Branco", "2010", "FFF00", BigDecimal.ZERO, recibo, LocalDateTime.now(), null, user, parkingSpot));

        var result = userParkingSpotRepository.findByReciboAndDataSaidaIsNull(recibo);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getRecibo()).isEqualTo(recibo);
        assertThat(result.get().getDataSaida()).isNull();
    }


    @Test
    @DisplayName("Não deve retornar nada se o estacionamento tiver uma data de saída")
    void findByReciboAndDataSaidaIsNullCaso2() {
        String recibo = "303030";
        var user = createUser(new User(null, "Gustavo", "gmail.com", "123", "003344", UserRole.COMMON));
        var parkingSpot = createSpot(new ParkingSpot(null, "P-00", Status.OCCUPIED, LocalDateTime.now()));
        var userParking = createUserParking(new UserParkingSpot(null, "Fiat", "Branco", "2010", "FFF00", BigDecimal.ZERO, recibo, LocalDateTime.now(), LocalDateTime.now().plusHours(1), user, parkingSpot));

        var result = userParkingSpotRepository.findByReciboAndDataSaidaIsNull(recibo);

        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Não deve retornar nada se estacionamento se não existir um registro com recibo")
    void findByReciboAndDataSaidaIsNullCaso3() {
        String recibo = "303030";

        var result = userParkingSpotRepository.findByReciboAndDataSaidaIsNull(recibo);

        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Deve retornar o estacionamento do usuario com o cpf informado")
    void findByUserCpfCaso1() {
        String cpf = "1172200";
        var user = createUser(new User(null, "Gustavo", "gmail.com", "123", cpf, UserRole.COMMON));
        var parkingSpot = createSpot(new ParkingSpot(null, "P-00", Status.OCCUPIED, LocalDateTime.now()));
        var userParking = createUserParking(new UserParkingSpot(null, "Fiat", "Branco", "2010", "FFF00", BigDecimal.ZERO, "T-00", LocalDateTime.now(), LocalDateTime.now().plusHours(1), user, parkingSpot));

        List<UserParkingSpot> result = userParkingSpotRepository.findByUserCpf(cpf);

        assertThat(result).isNotNull().isNotEmpty()
                .first()
                .satisfies(userParkingSpot -> assertThat(userParkingSpot.getUser().getCpf()).isEqualTo(cpf));

    }

    @Test
    @DisplayName("Deve retornar lista vazia se nenhum usuário com o CPF informado tiver estacionamento")
    void findByUserCpfCaso2() {
        String cpf = "1172200";

        List<UserParkingSpot> result = userParkingSpotRepository.findByUserCpf(cpf);

        assertThat(result).isEmpty();

    }

    private ParkingSpot createSpot(ParkingSpot spot) {
        ParkingSpot newSpot = new ParkingSpot();
        newSpot.setCode(spot.getCode());
        newSpot.setStatus(spot.getStatus());
        newSpot.setCreationTime(spot.getCreationTime());
        this.entityManager.persist(newSpot);

        return newSpot;
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

    private UserParkingSpot createUserParking(UserParkingSpot userParking) {
        UserParkingSpot newParking = new UserParkingSpot();
        newParking.setMarca(userParking.getMarca());
        newParking.setCor(userParking.getCor());
        newParking.setModelo(userParking.getModelo());
        newParking.setPlaca(userParking.getPlaca());
        newParking.setValor(userParking.getValor());
        newParking.setRecibo(userParking.getRecibo());
        newParking.setDataEntrada(userParking.getDataEntrada());
        newParking.setDataSaida(userParking.getDataSaida());
        newParking.setUser(userParking.getUser());
        newParking.setParkingSpot(userParking.getParkingSpot());
        this.entityManager.persist(newParking);

        return newParking;
    }
}