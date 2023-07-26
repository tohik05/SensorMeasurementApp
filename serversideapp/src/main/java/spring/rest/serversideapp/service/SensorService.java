package spring.rest.serversideapp.service;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import spring.rest.serversideapp.dto.SensorDTO;
import spring.rest.serversideapp.model.Sensor;

import java.util.List;
import java.util.Optional;

public interface SensorService {

    void create(SensorDTO sensorDTO, BindingResult bindingResult);

    SensorDTO read(int id);

    void update(int id, SensorDTO sensorDTO, BindingResult bindingResult);

    void delete(int id);

    List<SensorDTO> findAll();

    Optional<Sensor> findByName(String name);
}
