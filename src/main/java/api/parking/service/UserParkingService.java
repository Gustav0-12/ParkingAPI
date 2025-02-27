package api.parking.service;

import api.parking.dto.UserParkingResponse;
import api.parking.dto.UserParkingRequest;
import api.parking.entities.ParkingSpot;
import api.parking.entities.Status;
import api.parking.entities.User;
import api.parking.entities.UserParkingSpot;
import api.parking.repository.UserParkingSpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

@Service
public class UserParkingService {

    @Autowired
    UserParkingSpotRepository repository;

    @Autowired
    ParkingSpotService parkingSpotService;

    @Autowired
    UserService userService;

    public BigDecimal calculateTotalValue(LocalDateTime dataEntrada, LocalDateTime dataSaida) {
        long minutes = dataEntrada.until(dataSaida, ChronoUnit.MINUTES);
        BigDecimal valorTotal;

        if (minutes < 15) {
            valorTotal = BigDecimal.valueOf(5.00);
        } else if (minutes < 60) {
            valorTotal = BigDecimal.valueOf(9.25);
        } else {
            double additionalHours = Math.ceil((minutes - 60 ) / 60);
            double additionalFee = 2.00;
            valorTotal = BigDecimal.valueOf(9.25).add(new BigDecimal(additionalFee * additionalHours));
        }
        return valorTotal;
    }

    public String gerarRecibo() {
        Random gerador = new Random();

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int randomChar = gerador.nextInt(10);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }

    public UserParkingSpot checkIn(UserParkingRequest data) {
        User user = userService.findByCpf(data.userCPF());

        ParkingSpot availableSpot = parkingSpotService.findFirstByStatus();

        UserParkingSpot parkingSpot = new UserParkingSpot();
        parkingSpot.setUser(user);
        parkingSpot.setParkingSpot(availableSpot);
        availableSpot.setStatus(Status.OCCUPIED);
        parkingSpot.setMarca(data.marca());
        parkingSpot.setValor(BigDecimal.ZERO);
        parkingSpot.setCor(data.cor());
        parkingSpot.setModelo(data.modelo());
        parkingSpot.setPlaca(data.placa());
        parkingSpot.setDataEntrada(LocalDateTime.now());
        parkingSpot.setRecibo(gerarRecibo());

        return repository.save(parkingSpot);
    }

    public UserParkingSpot checkOut(String recibo) {
        UserParkingSpot parkingSpot = repository.findByReciboAndDataSaidaIsNull(recibo).orElseThrow(() -> new RuntimeException("Estacionamento não encontrado"));
        parkingSpot.getParkingSpot().setStatus(Status.AVAILABLE);
        parkingSpot.setDataSaida(LocalDateTime.now());

        BigDecimal valor = calculateTotalValue(parkingSpot.getDataEntrada(), parkingSpot.getDataSaida());
        parkingSpot.setValor(valor);

        return repository.save(parkingSpot);
    }

    public UserParkingResponse findByRecibo(String recibo) {
        UserParkingSpot parkingSpot = repository.findByReciboAndDataSaidaIsNull(recibo).orElseThrow(() -> new RuntimeException("Estacionamento não registrado"));
        return new UserParkingResponse(parkingSpot);
    }

    public List<UserParkingResponse> findByUserCpf(String cpf) {
        List<UserParkingSpot> parkingSpot = repository.findByUserCpf(cpf);

        return parkingSpot.stream().map(p -> new UserParkingResponse(
                p.getMarca(),
                p.getCor(),
                p.getModelo(),
                p.getPlaca(),
                p.getUser().getCpf(),
                p.getRecibo(),
                p.getValor(),
                p.getDataEntrada(),
                p.getDataSaida(),
                p.getParkingSpot().getCode())).toList();
    }
}
