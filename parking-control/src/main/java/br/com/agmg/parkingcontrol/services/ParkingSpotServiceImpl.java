package br.com.agmg.parkingcontrol.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.agmg.parkingcontrol.dtos.ParkingSpotDto;
import br.com.agmg.parkingcontrol.models.ParkingSpotModel;
import br.com.agmg.parkingcontrol.repositories.ParkingSpotRepository;
import br.com.agmg.parkingcontrol.util.ErrorCode;
import io.vavr.control.Either;

@Service
public class ParkingSpotServiceImpl implements ParkingSpotService {

    final ParkingSpotRepository repository;

    public ParkingSpotServiceImpl(ParkingSpotRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Either<ErrorCode, ParkingSpotDto> save(ParkingSpotDto parkingSpotDto) {
        if (repository.existsByLicensePlateCar(parkingSpotDto.getLicensePlateCar())) {
            return Either.left(new ErrorCode("msg.error.parkingspot.licenseplate.alreadyinuse"));
        }
        if (repository.existsByParkingSpotNumber(parkingSpotDto.getParkingSpotNumber())) {
            return Either.left(new ErrorCode("msg.error.parkingspot.alreadyinuse"));
        }
        if (repository.existsByApartmentAndBlock(parkingSpotDto.getApartment(), parkingSpotDto.getBlock())) {
            return Either.left(new ErrorCode("msg.error.parkingspot.alreadyregistered"));
        }
        var parkingSpotModel = new ParkingSpotModel();
        BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
        parkingSpotModel.setRegistrationDate(
            LocalDateTime.now(ZoneId.of("UTC"))
        );

        parkingSpotModel = repository.save(parkingSpotModel);
        BeanUtils.copyProperties(parkingSpotModel, parkingSpotDto);

        return Either.right(parkingSpotDto);
    }

    @Override
    public Page<ParkingSpotDto> getAllParkingSpots(Pageable pageable) {
        Page<ParkingSpotModel> result = repository.findAll(pageable);
        Page<ParkingSpotDto> pageableDtos = new PageImpl<>(result.stream().map(
            model -> new ParkingSpotDto(model)).collect(Collectors.toList()), pageable, result.getSize()
        );
        return pageableDtos;
    }

    @Override
    public Optional<ParkingSpotDto> getParkingSpotById(UUID id) {
        var parkingSpotModel = repository.findById(id);
        return parkingSpotModel.isPresent() 
                ? Optional.of(new ParkingSpotDto(parkingSpotModel.get())) 
                : Optional.empty();
    }

    @Override
    public Either<ErrorCode, Boolean> deleteById(UUID id) {
        if (id == null) {
            return Either.left(new ErrorCode("msg.error.id.could-notbenull"));
        }
        var optionalParkingSpot = repository.findById(id);
        if (optionalParkingSpot.isEmpty()) {
            return Either.left(new ErrorCode("msg.error.parkingspot.notfound"));
        }
        repository.deleteById(id);
        return Either.right(Boolean.TRUE);
    }

    @Override
    public Either<ErrorCode, Boolean> update(UUID id, ParkingSpotDto parkingSpotDto) {
        if (id == null || parkingSpotDto == null) {
            return Either.left(new ErrorCode("msg.error.invalid.parameters"));
        }
        parkingSpotDto.setId(id);
        parkingSpotDto.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        var parkingSpotModel = new ParkingSpotModel();
        BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
        repository.save(parkingSpotModel);
        return Either.right(Boolean.TRUE);
    }
    
}
