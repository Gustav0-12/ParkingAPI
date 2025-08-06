package api.parking.controller;

import api.parking.dto.ParkingSpotDTO;
import api.parking.entities.ParkingSpot;
import api.parking.service.ParkingSpotService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parkingspot")
@SecurityRequirement(name = "bearer-key")
public class ParkingSpotController {

    @Autowired
    ParkingSpotService service;

    @PostMapping("/create")
    public ResponseEntity<ParkingSpot> createSpot(@RequestBody ParkingSpotDTO data) {
        ParkingSpot newParkingSpot = service.createSpot(data);
        return ResponseEntity.ok(newParkingSpot);
    }

    @GetMapping
    public ResponseEntity<List<ParkingSpot>> findAll() {
        List<ParkingSpot> parkingSpots = service.findAll();
        return ResponseEntity.ok(parkingSpots);
    }

    @GetMapping("/{code}")
    public ResponseEntity<ParkingSpot> findByCode(@PathVariable String code) {
        ParkingSpot parkingSpot = service.findByCode(code);
        return ResponseEntity.ok(parkingSpot);
    }
}
