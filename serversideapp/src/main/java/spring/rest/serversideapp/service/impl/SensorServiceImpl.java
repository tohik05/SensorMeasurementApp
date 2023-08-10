package spring.rest.serversideapp.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import spring.rest.serversideapp.dto.SensorDTO;
import spring.rest.serversideapp.exception.EntityHasAlreadyExistException;
import spring.rest.serversideapp.exception.EntityValidationException;
import spring.rest.serversideapp.model.Sensor;
import spring.rest.serversideapp.repository.SensorRepository;
import spring.rest.serversideapp.service.SensorService;
import spring.rest.serversideapp.service.mapper.SensorMapper;
import spring.rest.serversideapp.validator.SensorValidator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static spring.rest.serversideapp.util.ErrorMessageBuilder.errorMessage;

@Service
public class SensorServiceImpl implements SensorService {

    private final SensorRepository sensorRepository;
    private final SensorValidator sensorValidator;
    private final SensorMapper sensorMapper;

    @Autowired
    public SensorServiceImpl(SensorRepository sensorRepository, SensorValidator sensorValidator, SensorMapper sensorMapper) {
        this.sensorRepository = sensorRepository;
        this.sensorValidator = sensorValidator;
        this.sensorMapper = sensorMapper;
    }

    @Override
    @Transactional
    public void create(SensorDTO sensorDTO, BindingResult bindingResult) {
        validateDTO(sensorDTO, bindingResult);

        checkSensorByName(sensorDTO.getName());

        sensorRepository.save(sensorMapper.mapToModel(sensorDTO));
    }

    @Override
    public SensorDTO read(int id) {
        return sensorMapper.mapToDTO(checkSensorById(id));
    }

    @Override
    public void update(int id, SensorDTO sensorDTO, BindingResult bindingResult) {
        validateDTO(sensorDTO, bindingResult);

        Sensor sensorRequest = sensorMapper.mapToModel(sensorDTO);
        Sensor sensorForUpdate = checkSensorById(id);
        sensorForUpdate.setName(sensorRequest.getName());

        sensorRepository.save(sensorForUpdate);
    }

    @Override
    public void delete(int id) {
        sensorRepository.delete(checkSensorById(id));
    }

    @Override
    public List<SensorDTO> findAll() {
        return sensorRepository.findAll()
                .stream()
                .map(sensorMapper::mapToDTO)
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

    private Sensor checkSensorById(int id){
        return sensorRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Sensor with ID '%s' not found", id)));
    }

    private void checkSensorByName(String name) {
        Optional<Sensor> sensorDB = sensorRepository.findByName(name);
        if (sensorDB.isPresent()) {
            throw new EntityHasAlreadyExistException(String.format("Sensor with name '%s' has already exist", name));
        }
    }
}
