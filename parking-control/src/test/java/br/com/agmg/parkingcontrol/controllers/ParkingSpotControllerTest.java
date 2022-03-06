package br.com.agmg.parkingcontrol.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import br.com.agmg.parkingcontrol.config.DateConfig;
import br.com.agmg.parkingcontrol.config.MessagePropertiesConfig;
import br.com.agmg.parkingcontrol.dtos.ParkingSpotDto;
import br.com.agmg.parkingcontrol.models.ParkingSpotModel;
import br.com.agmg.parkingcontrol.repositories.ParkingSpotRepository;
import br.com.agmg.parkingcontrol.services.ParkingSpotService;
import br.com.agmg.parkingcontrol.util.ErrorCode;
import io.vavr.control.Either;

@WebMvcTest({ParkingSpotController.class})
@Import({MessagePropertiesConfig.class, DateConfig.class})
@TestPropertySource(locations={"classpath:messages.properties", "classpath:application.properties"})
public class ParkingSpotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    ParkingSpotService service;

    @MockBean
    ParkingSpotRepository repository;

    private static final UUID ID = UUID.randomUUID();

    @Test
    void testCreateParkingSpotAlreadyCreated() throws Exception {
        Either<ErrorCode, ParkingSpotDto> either = Either.left(new ErrorCode("msg.error.parkingspot.licenseplate.alreadyinuse"));
        when(service.save(any(ParkingSpotDto.class))).thenReturn(either);
        mockMvc.perform(
            MockMvcRequestBuilders.post("/parking-spot")
                .content(getParkingSpotDtoAsString(false))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isConflict());
    }

    @Test
    void testCreateNewParkingSpot() throws Exception {
        Either<ErrorCode, ParkingSpotDto> either = Either.right(getParkingSpotDto(true));
        when(service.save(any(ParkingSpotDto.class))).thenReturn(either);
        when(repository.save(any(ParkingSpotModel.class))).thenReturn(getParkingSpotModel(true));
        mockMvc.perform(
            MockMvcRequestBuilders.post("/parking-spot")
                .content(getParkingSpotDtoAsString(false))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isCreated())
        .andExpect(content().json(asJsonString(getParkingSpotDto(true))));
    }

    public String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    String getParkingSpotDtoAsString(boolean saved) {
        String json = "{%s%s\"parkingSpotNumber\":\"2059\"," +
                      "\"licensePlateCar\":\"RRS8563\",\"brandCar\":\"Audi\"," +
                      "\"modelCar\":\"q5\",\"colorCar\":\"blue\"," +
                      "\"responsibleName\":\"Alexandre Guedes\",\"apartment\":\"206\",\"block\":\"B\"}";

        if (saved) {
            return json.formatted("\"id\":\"%s\",".formatted(ID),
                "\"registrationDate\":\"%s\",".formatted(LocalDateTime.now(ZoneId.of("UTC"))));
        } 
        return json.formatted("","");
    }

    ParkingSpotModel getParkingSpotModel(boolean saved) {
        ParkingSpotModel model = new ParkingSpotModel();
        if (saved) {
            model.setId(ID);
            model.setRegistrationDate(LocalDateTime.of(2022, 3, 1, 12, 0, 0));
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
            dto.setRegistrationDate(LocalDateTime.of(2022, 3, 1, 12, 0, 0));
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
    
}
