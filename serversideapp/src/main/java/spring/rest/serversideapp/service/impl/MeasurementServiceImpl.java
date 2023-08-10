package spring.rest.serversideapp.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import spring.rest.serversideapp.dto.MeasurementDTO;
import spring.rest.serversideapp.exception.EntityValidationException;
import spring.rest.serversideapp.model.Measurement;
import spring.rest.serversideapp.model.Sensor;
import spring.rest.serversideapp.repository.MeasurementRepository;
import spring.rest.serversideapp.service.MeasurementService;
import spring.rest.serversideapp.service.SensorService;
import spring.rest.serversideapp.service.mapper.MeasurementMapper;
import spring.rest.serversideapp.validator.MeasurementValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static spring.rest.serversideapp.util.ErrorMessageBuilder.errorMessage;

@Service
public class MeasurementServiceImpl implements MeasurementService {

    private final SensorService sensorService;
    private final MeasurementRepository measurementRepository;
    private final MeasurementValidator measurementValidator;
    private final MeasurementMapper measurementMapper;

    @Autowired
    public MeasurementServiceImpl(MeasurementRepository measurementRepository, SensorService sensorService,
                                  MeasurementValidator measurementValidator, MeasurementMapper measurementMapper) {
        this.measurementRepository = measurementRepository;
        this.sensorService = sensorService;
        this.measurementValidator = measurementValidator;
        this.measurementMapper = measurementMapper;
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

        Measurement measurementForSave = measurementMapper.mapToModel(measurementDTO);
        Sensor sensor = checkSensor(measurementForSave);

        measurementForSave.setSensor(sensor);
        measurementForSave.setCreatedDate(LocalDateTime.now());

        measurementRepository.save(measurementForSave);
    }

    @Override
    public void update(int id, MeasurementDTO measurementDTO, BindingResult bindingResult) {
        validateDTO(measurementDTO, bindingResult);

        Measurement measurementRequest = measurementMapper.mapToModel(measurementDTO);
        Measurement measurementForUpdate = checkMeasurement(id);
        Sensor sensor = checkSensor(measurementRequest);

        measurementForUpdate.setSensor(sensor);
        measurementForUpdate.setTemperature(measurementRequest.getTemperature());
        measurementForUpdate.setRaining(measurementRequest.getRaining());

        measurementRepository.save(measurementForUpdate);
    }

    @Override
    public void delete(int id) {
        measurementRepository.delete(checkMeasurement(id));
    }

    private Measurement checkMeasurement(int id) {
        return measurementRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Measurement with ID '%s' not found", id)));
    }

    private Sensor checkSensor(Measurement measurement) {
        return sensorService.findByName(measurement.getSensor().getName()).orElseThrow(
                () -> new EntityNotFoundException(String.format("Sensor with name '%s' not found", measurement.getSensor().getName())));
    }

    private List<MeasurementDTO> findAllMeasurementsBySensorNameAndRaining(String sensorName, Boolean isRaining) {
        return measurementRepository.findAll()
                .stream()
                .filter(sensor -> sensor.getSensor().getName().equals(sensorName))
                .filter(rain -> rain.getRaining() == isRaining)
                .map(measurementMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    private List<MeasurementDTO> findAllMeasurementsBySensorName(String sensorName) {
        return measurementRepository.findAll()
                .stream()
                .filter(sensor -> sensor.getSensor().getName().equals(sensorName))
                .map(measurementMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    private List<MeasurementDTO> findAllMeasurementsByRaining(Boolean isRaining) {
        return measurementRepository.findAll()
                .stream()
                .filter(rain -> rain.getRaining() == isRaining)
                .map(measurementMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    private List<MeasurementDTO> findAllMeasurements() {
        return measurementRepository.findAll()
                .stream()
                .map(measurementMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    private void validateDTO(MeasurementDTO measurementDTO, BindingResult bindingResult) {
        measurementValidator.validate(measurementDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new EntityValidationException(errorMessage(bindingResult));
        }
    }
}
