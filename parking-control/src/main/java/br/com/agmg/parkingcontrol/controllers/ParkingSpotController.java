package br.com.agmg.parkingcontrol.controllers;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Patterns.$Left;
import static io.vavr.Patterns.$Right;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.agmg.parkingcontrol.dtos.ParkingSpotDto;
import br.com.agmg.parkingcontrol.services.ParkingSpotService;
import br.com.agmg.parkingcontrol.util.ErrorCode;
import br.com.agmg.parkingcontrol.util.MessageHandler;
import io.vavr.control.Either;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingSpotController {

    private ParkingSpotService parkingSpotService;
    private MessageHandler messageHandler;

    ParkingSpotController(ParkingSpotService parkingSpotService, MessageHandler messageHandler) {
        this.parkingSpotService = parkingSpotService;
        this.messageHandler = messageHandler;
    }

    @PostMapping
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDto parkingSpotDto) {
        Either<ErrorCode, ParkingSpotDto> result = parkingSpotService.save(parkingSpotDto);
        return Match(result).of(
            Case($Left($()), errorCode -> ResponseEntity.status(HttpStatus.CONFLICT).body(messageHandler.getMessage(errorCode.getReason()))),
            Case($Right($()), parkingSpotSaved -> ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotSaved))
        );
    }

    @GetMapping
    public ResponseEntity<Page<ParkingSpotDto>> getAllParkingSpots(
        @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok().body(parkingSpotService.getAllParkingSpots(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getParkingSpotById(@PathVariable(value = "id") UUID id) {
        var optionalParkingSpotDto = parkingSpotService.getParkingSpotById(id);
        return Match(optionalParkingSpotDto.isEmpty()).of(
            Case($(true), ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageHandler.getMessage("msg.error.parkingspot.notfound"))),
            Case($(false), ResponseEntity.ok().body(optionalParkingSpotDto.get()))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteParkingSpotById(@PathVariable(value = "id") UUID id) {
        Either<ErrorCode, Boolean> result = parkingSpotService.deleteById(id);
        return Match(result).of(
            Case($Left($()), errorCode -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageHandler.getMessage(errorCode.getReason()))),
            Case($Right($()), ResponseEntity.ok().body(messageHandler.getMessage("msg.error.parkingspot.deleted")))
        );
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateParkingSpot(@PathVariable(value = "id") UUID id,
                    @RequestBody @Valid ParkingSpotDto parkingSpotDto) {
        Either<ErrorCode, Boolean> result = parkingSpotService.update(id, parkingSpotDto);
        return Match(result).of(
            Case($Left($()), errorCode -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageHandler.getMessage(errorCode.getReason()))),
            Case($Right($()), ResponseEntity.ok().body(messageHandler.getMessage("msg.error.parkingspot.updated")))
        );
    }
}
