package api.parking.service;

import api.parking.dto.UserParkingRequest;
import api.parking.entities.*;
import api.parking.repository.UserParkingSpotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserParkingServiceTest {
    @Mock
    private UserParkingSpotRepository repository;

    @Mock
    private ParkingSpotService parkingSpotService;

    @Mock
    private UserService userService;

    @Mock
    private ParkingFeeCalculator calculator;

    @Autowired
    @InjectMocks
    private UserParkingService userParkingService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Deve realizar check-in com sucesso quando usuário existe e há vaga disponível")
    void checkInCase1() {
        User user = new User(1L, "Mario", "g@gmail.com", "12345", "99999999", UserRole.COMMON);
        when(userService.findByCpf("99999999")).thenReturn(user);

        ParkingSpot parkingSpot = new ParkingSpot(1L, "ABC-12", Status.AVAILABLE, LocalDateTime.now());
        when(parkingSpotService.findFirstByStatus()).thenReturn(parkingSpot);

        UserParkingRequest estacionamento = new UserParkingRequest("FIAT", "Branco", "Uno", "91919191", user.getCpf());
        userParkingService.checkIn(estacionamento);

        verify(repository, times(1)).save(any());
    }


    @Test
    @DisplayName("Deve lançar exceção quando o usuário não for encontrado")
    void checkInCase2() {
        String cpfInvalido = "0000000";
        when(userService.findByCpf(cpfInvalido)).thenReturn(null);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            UserParkingRequest estacionamento = new UserParkingRequest("FIAT", "Branco", "Uno", "91919191", cpfInvalido);
            userParkingService.checkIn(estacionamento);
        });

        assertEquals("Usuario com o cpf "+ cpfInvalido + " não encontrado", thrown.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando não houver vaga disponível")
    void checkInCase3() {
        User user = new User(1L, "Mario", "g@gmail.com", "12345", "99999999", UserRole.COMMON);
        when(userService.findByCpf("99999999")).thenReturn(user);

        when(parkingSpotService.findFirstByStatus()).thenReturn(null);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            UserParkingRequest estacionamento = new UserParkingRequest("FIAT", "Branco", "Uno", "91919191", "99999999");
            userParkingService.checkIn(estacionamento);
        });

        assertEquals("Nenhuma vaga disponivel no momento", thrown.getMessage());
    }

    @Test
    @DisplayName("Deve realizar check-out com sucesso quando recibo for válido e não houver data de saída")
    void checkOutCase1() {
        String recibo = "101010";

        UserParkingSpot estacionamento = new UserParkingSpot();
        estacionamento.setRecibo(recibo);
        estacionamento.setDataEntrada(LocalDateTime.now().minusHours(1));
        estacionamento.setParkingSpot(new ParkingSpot());
        estacionamento.getParkingSpot().setStatus(Status.OCCUPIED);

        when(repository.findByReciboAndDataSaidaIsNull(recibo)).thenReturn(Optional.of(estacionamento));
        when(calculator.calculateTotalValue(any(), any())).thenReturn(new BigDecimal("9.25"));

        UserParkingSpot resultado = userParkingService.checkOut(recibo);

        assertEquals(new BigDecimal("9.25"), resultado.getValor());
        assertEquals(Status.AVAILABLE, resultado.getParkingSpot().getStatus());
        verify(repository, times(1)).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o recibo não for encontrado")
    void checkOutCase2() {
        String reciboInvalido = "101010";
        when(repository.findByReciboAndDataSaidaIsNull(reciboInvalido)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userParkingService.checkOut(reciboInvalido);
        });

        assertEquals("Estacionamento não encontrado", thrown.getMessage());
    }
}

