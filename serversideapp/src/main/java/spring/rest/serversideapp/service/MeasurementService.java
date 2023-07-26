package spring.rest.serversideapp.service;

import org.springframework.validation.BindingResult;
import spring.rest.serversideapp.dto.MeasurementDTO;
import spring.rest.serversideapp.dto.SensorDTO;

import java.util.List;

public interface MeasurementService {

    List<MeasurementDTO> findAll(String sensorName, Boolean isRaining);

    void create(MeasurementDTO measurementDTO, BindingResult bindingResult);

    void update(int id, MeasurementDTO measurementDTO, BindingResult bindingResult);

    void delete(int id);

}
