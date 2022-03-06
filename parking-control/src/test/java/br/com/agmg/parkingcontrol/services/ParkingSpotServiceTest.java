package br.com.agmg.parkingcontrol.services;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import br.com.agmg.parkingcontrol.dtos.ParkingSpotDto;
import br.com.agmg.parkingcontrol.models.ParkingSpotModel;
import br.com.agmg.parkingcontrol.repositories.ParkingSpotRepository;
import br.com.agmg.parkingcontrol.util.ErrorCode;
import io.vavr.control.Either;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ParkingSpotServiceTest {

    @Mock
    private ParkingSpotRepository repository;
    private ParkingSpotService parkingSpotService;
    private static final UUID ID = UUID.randomUUID();

    @BeforeEach
    void initServiceTest() {
        parkingSpotService = new ParkingSpotServiceImpl(repository);
    }
    
    @Test
    void tryCreateSparkingSpotThatLicensePlateAlrearyExists() {
        var spotModel = getParkingSpotModel(true);
        when(repository.existsByLicensePlateCar(spotModel.getLicensePlateCar()))
            .thenReturn(true);
     
        Either<ErrorCode, ParkingSpotDto> result = parkingSpotService.save(getParkingSpotDto(false));
        assertTrue(result.isLeft());
    }

    @Test
    void tryCreateSparkingSpotThatParkingSpotNumberAlrearyExists() {
        var spotModel = getParkingSpotModel(true);
        when(repository.existsByLicensePlateCar(spotModel.getLicensePlateCar()))
            .thenReturn(false);
        when(repository.existsByParkingSpotNumber(spotModel.getParkingSpotNumber()))
            .thenReturn(true);
     
        Either<ErrorCode, ParkingSpotDto> result = parkingSpotService.save(getParkingSpotDto(false));
        assertTrue(result.isLeft());
    }

    @Test
    void tryCreateSparkingSpotThatApartmentAndBlockExists() {
        var spotModel = getParkingSpotModel(true);
        when(repository.existsByLicensePlateCar(spotModel.getLicensePlateCar()))
            .thenReturn(false);
        when(repository.existsByParkingSpotNumber(spotModel.getParkingSpotNumber()))
            .thenReturn(false);
        when(repository.existsByApartmentAndBlock(spotModel.getApartment(), spotModel.getBlock()))
            .thenReturn(true);
     
        Either<ErrorCode, ParkingSpotDto> result = parkingSpotService.save(getParkingSpotDto(false));
        assertTrue(result.isLeft());
    }
    
    @Test
    void testCreateParkingSpotOk() {
        var spotModel = getParkingSpotModel(true);
        when(repository.existsByLicensePlateCar(spotModel.getLicensePlateCar()))
            .thenReturn(false);
        when(repository.existsByParkingSpotNumber(spotModel.getParkingSpotNumber()))
            .thenReturn(false);
        when(repository.existsByApartmentAndBlock(spotModel.getApartment(), spotModel.getBlock()))
            .thenReturn(false);
        when(repository.save(any(ParkingSpotModel.class)))
            .thenReturn(getParkingSpotModel(true));

        Either<ErrorCode, ParkingSpotDto> result = parkingSpotService.save(getParkingSpotDto(false));
        assertTrue(result.isRight());
    }

    @Test
    void testGetAllParkingSpots() {
        when(repository.findAll(any(Pageable.class))).thenReturn(getPageOfParkingSpotModel());
        var pageOfDtos = parkingSpotService.getAllParkingSpots(Pageable.ofSize(10));
        assertTrue(pageOfDtos.getSize() > 0);
    }

    ParkingSpotModel getParkingSpotModel(boolean saved) {
        ParkingSpotModel model = new ParkingSpotModel();
        if (saved) {
            model.setId(ID);
            model.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        }
        model.setApartment("101");
        model.setBlock("01");
        model.setBrandCar("Mercedes");
        model.setColorCar("Black");
        model.setLicensePlateCar("MDF3021");
        model.setParkingSpotNumber("1023");
        model.setModelCar("C130");
        model.setResponsibleName("João Gusmão");
        return model;
    }
    ParkingSpotDto getParkingSpotDto(boolean saved) {
        ParkingSpotDto dto = new ParkingSpotDto();
        if (saved) {
            dto.setId(ID);
            dto.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        }
        dto.setApartment("101");
        dto.setBlock("01");
        dto.setBrandCar("Mercedes");
        dto.setColorCar("Black");
        dto.setLicensePlateCar("MDF3021");
        dto.setParkingSpotNumber("1023");
        dto.setModelCar("C130");
        dto.setResponsibleName("João Gusmão");
        return dto;
    }
    Page<ParkingSpotModel> getPageOfParkingSpotModel() {
        var listOfParkingSpot = new ArrayList<ParkingSpotModel>();
        var model = new ParkingSpotModel();
        model.setId(UUID.randomUUID());
        model.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        model.setApartment("101");
        model.setBlock("01");
        model.setBrandCar("Mercedes");
        model.setColorCar("Black");
        model.setLicensePlateCar("MDF3021");
        model.setParkingSpotNumber("1023");
        model.setModelCar("C130");
        model.setResponsibleName("João Gusmão");
        listOfParkingSpot.add(model);

        model = new ParkingSpotModel();
        model.setId(UUID.randomUUID());
        model.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        model.setApartment("102");
        model.setBlock("01");
        model.setBrandCar("Mercedes");
        model.setColorCar("Black");
        model.setLicensePlateCar("MDF3022");
        model.setParkingSpotNumber("1024");
        model.setModelCar("C130");
        model.setResponsibleName("João Gusmão");
        listOfParkingSpot.add(model);

        Page<ParkingSpotModel> pageableModels = new PageImpl<>(listOfParkingSpot);
        return pageableModels;
        
    }
}
