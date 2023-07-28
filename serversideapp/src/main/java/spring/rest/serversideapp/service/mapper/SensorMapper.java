package spring.rest.serversideapp.service.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.rest.serversideapp.dto.SensorDTO;
import spring.rest.serversideapp.model.Sensor;

@Component
public class SensorMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public SensorMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Sensor mapToModel(SensorDTO sensorDTO){
        return modelMapper.map(sensorDTO, Sensor.class);
    }

    public SensorDTO mapToDTO(Sensor sensor){
        return modelMapper.map(sensor, SensorDTO.class);
    }
}
