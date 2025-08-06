package api.parking.controller;

import api.parking.dto.UserParkingRequest;
import api.parking.dto.UserParkingResponse;
import api.parking.entities.UserParkingSpot;
import api.parking.service.UserParkingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/parking")
@SecurityRequirement(name = "bearer-key")
public class UserParkingController {

    @Autowired
    UserParkingService service;

    @PostMapping("/check-in")
    public ResponseEntity<UserParkingResponse> checkIn(@RequestBody UserParkingRequest data, UriComponentsBuilder uriBuilder) {
        UserParkingSpot parkingSpot = service.checkIn(data);

        var uri = uriBuilder.path("/parking/{recibo}").buildAndExpand(parkingSpot.getRecibo()).toUri();
        return ResponseEntity.created(uri).body(new UserParkingResponse(parkingSpot));
    }

    @PostMapping("/check-out/{recibo}")
    public ResponseEntity<UserParkingResponse> checkOut(@PathVariable String recibo) {
        UserParkingSpot parkingSpot = service.checkOut(recibo);
        return ResponseEntity.ok(new UserParkingResponse(parkingSpot));
    }

    @GetMapping("/{recibo}")
    public ResponseEntity<UserParkingResponse> findByRecibo(@PathVariable String recibo) {
        UserParkingResponse parkingSpot = service.findByRecibo(recibo);
        return ResponseEntity.ok(parkingSpot);
    }

    @GetMapping("/user/{cpf}")
    public ResponseEntity<List<UserParkingResponse>> findByUserCpf(@PathVariable String cpf) {
        List<UserParkingResponse> parkingSpot = service.findByUserCpf(cpf);
        return ResponseEntity.ok(parkingSpot);
    }
}
