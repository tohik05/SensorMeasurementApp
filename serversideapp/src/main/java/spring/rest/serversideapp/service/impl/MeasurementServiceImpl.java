package spring.rest.serversideapp.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import spring.rest.serversideapp.dto.MeasurementDTO;
import spring.rest.serversideapp.dto.converters.ConvertToMeasurementDTO;
import spring.rest.serversideapp.exception.EntityValidationException;
import spring.rest.serversideapp.model.Measurement;
import spring.rest.serversideapp.model.Sensor;
import spring.rest.serversideapp.repository.MeasurementRepository;
import spring.rest.serversideapp.service.MeasurementService;
import spring.rest.serversideapp.service.SensorService;
import spring.rest.serversideapp.validator.MeasurementValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static spring.rest.serversideapp.dto.converters.ConvertToMeasurement.convertToMeasurement;
import static spring.rest.serversideapp.util.ErrorMessageBuilder.errorMessage;

@Service
public class MeasurementServiceImpl implements MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final SensorService sensorService;
    private final MeasurementValidator measurementValidator;

    @Autowired
    public MeasurementServiceImpl(MeasurementRepository measurementRepository, SensorService sensorService, MeasurementValidator measurementValidator) {
        this.measurementRepository = measurementRepository;
        this.sensorService = sensorService;
        this.measurementValidator = measurementValidator;
    }

    @Override
    public List<MeasurementDTO> findAll(String sensorName, Boolean isRaining) {
        if (sensorName == null && isRaining == null) {
            return findAllMeasurements();
        } else if (sensorName == null) {
            return findAllMeasurementsByRaining(isRaining);
        } else if (isRaining == null) {
            return findAllMeasurementsBySensorName(sensorName);
        } else return findAllMeasurementsBySensorNameAndRaining(sensorName, isRaining);
    }

    @Override
    @Transactional
    public void create(MeasurementDTO measurementDTO, BindingResult bindingResult) {
        validateDTO(measurementDTO, bindingResult);

        Measurement measurement = convertToMeasurement(measurementDTO);
        Optional<Sensor> sensor = sensorService.findByName(measurement.getSensor().getName());

        measurement.setSensor(sensor.orElseThrow(
                () -> new EntityNotFoundException(String.format("Sensor with name '%s' has not found", measurement.getSensor().getName()))));
        measurement.setCreatedDate(LocalDateTime.now());

        measurementRepository.save(measurement);
    }

    @Override
    public void update(int id, MeasurementDTO measurementDTO, BindingResult bindingResult) {
        validateDTO(measurementDTO, bindingResult);

        Measurement measurementRequest = convertToMeasurement(measurementDTO);
        Optional<Measurement> measurementDB = measurementRepository.findById(id);
        Measurement measurement = measurementDB.orElseThrow(
                () -> new EntityNotFoundException(String.format("Measurement with ID '%s' not found", id)));

        Optional<Sensor> sensorByName = sensorService.findByName(measurementRequest.getSensor().getName());
        measurement.setSensor(sensorByName.orElseThrow(
                () -> new EntityNotFoundException(String.format("Sensor with name '%s' not found", measurementRequest.getSensor().getName()))));
        measurement.setTemperature(measurementRequest.getTemperature());
        measurement.setRaining(measurementRequest.getRaining());

        measurementRepository.save(measurement);
    }

    @Override
    public void delete(int id) {
        Optional<Measurement> measurementDB = measurementRepository.findById(id);

        measurementRepository.delete(measurementDB.orElseThrow(
                () -> new EntityNotFoundException(String.format("Measurement with ID '%s' not found", id))));
    }

    private List<MeasurementDTO> findAllMeasurementsBySensorNameAndRaining(String sensorName, Boolean isRaining) {
        return measurementRepository.findAll()
                .stream()
                .filter(sensor -> sensor.getSensor().getName().equals(sensorName))
                .filter(rain -> rain.getRaining() == isRaining)
                .map(ConvertToMeasurementDTO::convertToMeasurementDTO)
                .collect(Collectors.toList());
    }

    private List<MeasurementDTO> findAllMeasurementsBySensorName(String sensorName) {
        return measurementRepository.findAll()
                .stream()
                .filter(sensor -> sensor.getSensor().getName().equals(sensorName))
                .map(ConvertToMeasurementDTO::convertToMeasurementDTO)
                .collect(Collectors.toList());
    }

    private List<MeasurementDTO> findAllMeasurementsByRaining(Boolean isRaining) {
        return measurementRepository.findAll()
                .stream()
                .filter(rain -> rain.getRaining() == isRaining)
                .map(ConvertToMeasurementDTO::convertToMeasurementDTO)
                .collect(Collectors.toList());
    }

    private List<MeasurementDTO> findAllMeasurements() {
        return measurementRepository.findAll()
                .stream()
                .map(ConvertToMeasurementDTO::convertToMeasurementDTO)
                .collect(Collectors.toList());
    }

    private void validateDTO(MeasurementDTO measurementDTO, BindingResult bindingResult) {
        measurementValidator.validate(measurementDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new EntityValidationException(errorMessage(bindingResult));
        }
    }
}
