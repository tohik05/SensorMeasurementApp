package spring.rest.serversideapp.dto.converters;

import spring.rest.serversideapp.dto.MeasurementDTO;
import spring.rest.serversideapp.model.Measurement;

import static spring.rest.serversideapp.dto.converters.ConvertToSensor.convertToSensor;

public class ConvertToMeasurement {
    public static Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        Measurement measurement = new Measurement();
        measurement.setTemperature(measurementDTO.getTemperature());
        measurement.setRaining(measurementDTO.getRaining());
        measurement.setSensor(convertToSensor(measurementDTO.getSensor()));
        return measurement;
    }
}
