package api.parking.repository;

import api.parking.entities.ParkingSpot;
import api.parking.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {
    Optional<ParkingSpot> findByCode(String code);

    Optional<ParkingSpot> findFirstByStatus(Status status);
}
