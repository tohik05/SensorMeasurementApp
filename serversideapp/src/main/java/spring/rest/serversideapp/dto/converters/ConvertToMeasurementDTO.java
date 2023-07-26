package spring.rest.serversideapp.dto.converters;

import spring.rest.serversideapp.dto.MeasurementDTO;
import spring.rest.serversideapp.model.Measurement;

import static spring.rest.serversideapp.dto.converters.ConvertToSensorDTO.convertToSensorDTO;

public class ConvertToMeasurementDTO {
    public static MeasurementDTO convertToMeasurementDTO(Measurement measurement) {
        MeasurementDTO measurementDTO = new MeasurementDTO();
        measurementDTO.setTemperature(measurement.getTemperature());
        measurementDTO.setRaining(measurement.getRaining());
        measurementDTO.setSensor(convertToSensorDTO(measurement.getSensor()));
        return measurementDTO;
    }
}
