package api.parking.repository;

import api.parking.entities.UserParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserParkingSpotRepository extends JpaRepository<UserParkingSpot, Long> {
    Optional<UserParkingSpot> findByReciboAndDataSaidaIsNull(String recibo);

    @Query("SELECT p FROM UserParkingSpot p JOIN p.user u WHERE u.cpf = :cpf")
    List<UserParkingSpot> findByUserCpf(@Param("cpf") String cpf);
}
