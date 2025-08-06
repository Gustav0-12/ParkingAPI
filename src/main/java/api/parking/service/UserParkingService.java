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

    @Autowired
    ParkingFeeCalculator calculator;

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
        if (user == null) {
            throw new RuntimeException("Usuario com o cpf " + data.userCPF()+ " não encontrado");
        }

        ParkingSpot availableSpot = parkingSpotService.findFirstByStatus();
        if (availableSpot == null) {
            throw new RuntimeException("Nenhuma vaga disponivel no momento");
        }

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

        BigDecimal valor = calculator.calculateTotalValue(parkingSpot.getDataEntrada(), parkingSpot.getDataSaida());
        parkingSpot.setValor(valor);

        repository.save(parkingSpot);
        return parkingSpot;
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
