package br.com.agmg.parkingcontrol.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.agmg.parkingcontrol.dtos.ParkingSpotDto;
import br.com.agmg.parkingcontrol.util.ErrorCode;
import io.vavr.control.Either;

public interface ParkingSpotService {

    Either<ErrorCode, ParkingSpotDto> save(ParkingSpotDto parkingSpotDto);
    Page<ParkingSpotDto> getAllParkingSpots(Pageable pageable);
    Optional<ParkingSpotDto> getParkingSpotById(UUID id);
    Either<ErrorCode, Boolean> deleteById(UUID id);
    Either<ErrorCode, Boolean> update(UUID id, ParkingSpotDto parkingSpotDto);

}
