package api.parking.repository;

import api.parking.entities.UserParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserParkingSpotRepository extends JpaRepository<UserParkingSpot, Long> {
}
