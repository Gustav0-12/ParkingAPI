package api.parking.service;

import api.parking.dto.ParkingSpotDTO;
import api.parking.entities.ParkingSpot;
import api.parking.repository.ParkingSpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingSpotService {

    @Autowired
    ParkingSpotRepository repository;

    public ParkingSpot createSpot(ParkingSpotDTO data) {
        Optional<ParkingSpot> existingSpot = repository.findByCode(data.code());
        if (existingSpot.isPresent()) {
            throw new RuntimeException("Vaga já registrada");
        }

        ParkingSpot newSpot = new ParkingSpot();
        newSpot.setCode(data.code());
        newSpot.setStatus(data.status());
        newSpot.setCreationTime(LocalDateTime.now());
        return repository.save(newSpot);
    }

    public List<ParkingSpot> findAll() {
        return repository.findAll();
    }

    public ParkingSpot findByCode(String code) {
        return repository.findByCode(code).orElseThrow(() -> new RuntimeException("Vaga com o codigo " + code + " não encontrada"));
    }
}
