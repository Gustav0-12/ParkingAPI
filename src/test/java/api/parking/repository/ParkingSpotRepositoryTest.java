package api.parking.repository;

import api.parking.entities.ParkingSpot;
import api.parking.entities.Status;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class ParkingSpotRepositoryTest {

    @Autowired
    ParkingSpotRepository parkingSpotRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Deve retornar uma vaga com esse codigo")
    void findByCodeCaso1() {
        String code = "123";
        ParkingSpot parkingSpot = new ParkingSpot(null, code, Status.AVAILABLE, LocalDateTime.now());
        this.createSpot(parkingSpot);

        Optional<ParkingSpot> result = parkingSpotRepository.findByCode(code);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getCode()).isEqualTo(code);
        assertThat(result.get().getStatus()).isEqualTo(Status.AVAILABLE);
    }

    @Test
    @DisplayName("Não deve retornar uma vaga se não existir")
    void findByCodeCaso2() {
        String code = "678";
        Optional<ParkingSpot> result = parkingSpotRepository.findByCode(code);

        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Deve retornar uma vaga disponivel")
    void findFirstByStatusCaso1() {
        ParkingSpot parkingSpot = new ParkingSpot(null, "111", Status.AVAILABLE, LocalDateTime.now());
        this.createSpot(parkingSpot);

        Optional<ParkingSpot> result = parkingSpotRepository.findFirstByStatus(Status.AVAILABLE);

        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Não deve retornar uma vaga se não estiver disponivel")
    void findFirstByStatusCaso2() {
        ParkingSpot parkingSpot = new ParkingSpot(null, "111", Status.OCCUPIED, LocalDateTime.now());
        this.createSpot(parkingSpot);

        Optional<ParkingSpot> result = parkingSpotRepository.findFirstByStatus(Status.AVAILABLE);

        assertThat(result.isEmpty()).isTrue();
    }

    private ParkingSpot createSpot(ParkingSpot spot) {
        ParkingSpot newSpot = new ParkingSpot();
        newSpot.setCode(spot.getCode());
        newSpot.setStatus(spot.getStatus());
        newSpot.setCreationTime(spot.getCreationTime());
        this.entityManager.persist(newSpot);

        return newSpot;
    }
}