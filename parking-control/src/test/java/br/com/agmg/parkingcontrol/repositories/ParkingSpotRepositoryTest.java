package br.com.agmg.parkingcontrol.repositories;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.agmg.parkingcontrol.models.ParkingSpotModel;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ParkingSpotRepositoryTest {

    @Autowired
    private ParkingSpotRepository parkingSpotRepository;
    private UUID id;

    @BeforeEach
    void initUseCase() {
        var parkingSpot = parkingSpotRepository.save(getParkingSpotModel());
        this.id = parkingSpot.getId();
    }

    @AfterEach
    void destroyUseCase() {
        parkingSpotRepository.deleteById(id);
    }
    
    ParkingSpotModel getParkingSpotModel() {
        var model = new ParkingSpotModel();
        model.setApartment("101");
        model.setBlock("01");
        model.setBrandCar("Mercedes");
        model.setColorCar("Black");
        model.setLicensePlateCar("MDF3021");
        model.setModelCar("C130");
        model.setParkingSpotNumber("1023");
        model.setResponsibleName("João Gusmão");
        model.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        return model;
    }

    @Test
    void testExistsByLicensePlateCar() {
        assertTrue(parkingSpotRepository.existsByLicensePlateCar("MDF3021"));
    }
    
}
