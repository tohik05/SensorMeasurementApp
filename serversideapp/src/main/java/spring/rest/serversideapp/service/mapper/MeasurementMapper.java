package spring.rest.serversideapp.service.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.rest.serversideapp.dto.MeasurementDTO;
import spring.rest.serversideapp.model.Measurement;

@Component
public class MeasurementMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public MeasurementMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Measurement mapToModel(MeasurementDTO measurementDTO){
        return modelMapper.map(measurementDTO, Measurement.class);
    }

    public MeasurementDTO mapToDTO(Measurement measurement){
        return modelMapper.map(measurement, MeasurementDTO.class);
    }
}
