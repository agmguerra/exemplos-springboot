package br.com.agmg.parkingcontrol.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import br.com.agmg.parkingcontrol.models.ParkingSpotModel;

public class ParkingSpotDto {

    private UUID id;
    private LocalDateTime registrationDate;
    @NotBlank
    private String parkingSpotNumber;
    @NotBlank
    @Size(max = 7)
    private String licensePlateCar;
    @NotBlank
    private String brandCar;
    @NotBlank
    private String modelCar;
    @NotBlank
    private String colorCar;
    @NotBlank
    private String responsibleName;
    @NotBlank
    private String apartment;
    @NotBlank
    private String block;
    
    public ParkingSpotDto() {}

    public ParkingSpotDto(ParkingSpotModel model) {
        this.apartment = model.getApartment();
        this.block = model.getBlock();
        this.brandCar = model.getBrandCar();
        this.colorCar = model.getColorCar();
        this.id = model.getId();
        this.licensePlateCar = model.getLicensePlateCar();
        this.modelCar = model.getModelCar();
        this.parkingSpotNumber = model.getParkingSpotNumber();
        this.registrationDate = model.getRegistrationDate();
        this.responsibleName = model.getResponsibleName();
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }
    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }
    public String getParkingSpotNumber() {
        return parkingSpotNumber;
    }
    public void setParkingSpotNumber(String parkingSpotNumber) {
        this.parkingSpotNumber = parkingSpotNumber;
    }
    public String getLicensePlateCar() {
        return licensePlateCar;
    }
    public void setLicensePlateCar(String licensePlateCar) {
        this.licensePlateCar = licensePlateCar;
    }
    public String getBrandCar() {
        return brandCar;
    }
    public void setBrandCar(String brandCar) {
        this.brandCar = brandCar;
    }
    public String getModelCar() {
        return modelCar;
    }
    public void setModelCar(String modelCar) {
        this.modelCar = modelCar;
    }
    public String getColorCar() {
        return colorCar;
    }
    public void setColorCar(String colorCar) {
        this.colorCar = colorCar;
    }
    public String getResponsibleName() {
        return responsibleName;
    }
    public void setResponsibleName(String responsibleName) {
        this.responsibleName = responsibleName;
    }
    public String getApartment() {
        return apartment;
    }
    public void setApartment(String apartment) {
        this.apartment = apartment;
    }
    public String getBlock() {
        return block;
    }
    public void setBlock(String block) {
        this.block = block;
    }

    
    
}
