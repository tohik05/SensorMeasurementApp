package spring.rest.serversideapp.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import spring.rest.serversideapp.dto.MeasurementDTO;
import spring.rest.serversideapp.dto.SensorDTO;
import spring.rest.serversideapp.dto.converters.ConvertToSensorDTO;
import spring.rest.serversideapp.exception.EntityHasAlreadyExistException;
import spring.rest.serversideapp.exception.EntityValidationException;
import spring.rest.serversideapp.model.Sensor;
import spring.rest.serversideapp.repository.SensorRepository;
import spring.rest.serversideapp.service.SensorService;
import spring.rest.serversideapp.validator.SensorValidator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static spring.rest.serversideapp.dto.converters.ConvertToSensor.convertToSensor;
import static spring.rest.serversideapp.dto.converters.ConvertToSensorDTO.convertToSensorDTO;
import static spring.rest.serversideapp.util.ErrorMessageBuilder.errorMessage;

@Service
public class SensorServiceImpl implements SensorService {

    private final SensorRepository sensorRepository;
    private final SensorValidator sensorValidator;

    @Autowired
    public SensorServiceImpl(SensorRepository sensorRepository, SensorValidator sensorValidator) {
        this.sensorRepository = sensorRepository;
        this.sensorValidator = sensorValidator;
    }

    @Override
    @Transactional
    public void create(SensorDTO sensorDTO, BindingResult bindingResult) {
        validateDTO(sensorDTO, bindingResult);

        Sensor sensor = convertToSensor(sensorDTO);
        Optional<Sensor> sensorDB = sensorRepository.findByName(sensorDTO.getName());
        if (sensorDB.isPresent()) {
            throw new EntityHasAlreadyExistException(String.format("Sensor with name '%s' has already exist", sensorDB.get().getName()));
        }

        sensorRepository.save(sensor);
    }

    @Override
    public SensorDTO read(int id) {
        Optional<Sensor> sensorDB = sensorRepository.findById(id);
        return convertToSensorDTO(sensorDB.orElseThrow(
                () -> new EntityNotFoundException(String.format("Sensor with ID '%s' not found", id))));
    }

    @Override
    public void update(int id, SensorDTO sensorDTO, BindingResult bindingResult) {
        validateDTO(sensorDTO, bindingResult);

        Sensor sensorRequest = convertToSensor(sensorDTO);
        Optional<Sensor> sensorDB = sensorRepository.findById(id);
        Sensor sensor = sensorDB.orElseThrow(
                () -> new EntityNotFoundException(String.format("Sensor with ID '%s' not found", id)));
        sensor.setName(sensorRequest.getName());

        sensorRepository.save(sensor);
    }

    @Override
    public void delete(int id) {
        Optional<Sensor> sensorDB = sensorRepository.findById(id);
        sensorRepository.delete(sensorDB.orElseThrow(
                () -> new EntityNotFoundException(String.format("Sensor with ID '%s' not found", id))));
    }

    @Override
    public List<SensorDTO> findAll() {
        return sensorRepository.findAll()
                .stream()
                .map(ConvertToSensorDTO::convertToSensorDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Sensor> findByName(String name) {
        return sensorRepository.findByName(name);
    }

    private void validateDTO(SensorDTO sensorDTO, BindingResult bindingResult) {
        sensorValidator.validate(sensorDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new EntityValidationException(errorMessage(bindingResult));
        }
    }

}
