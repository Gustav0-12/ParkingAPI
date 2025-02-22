package api.parking.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "tb_user_parkingspot")
public class UserParkingSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String marca;
    private String cor;
    private String modelo;
    private String placa;
    private BigDecimal valor;
    private String recibo;
    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaida;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "parkingspot_id")
    private ParkingSpot parkingSpot;
}
