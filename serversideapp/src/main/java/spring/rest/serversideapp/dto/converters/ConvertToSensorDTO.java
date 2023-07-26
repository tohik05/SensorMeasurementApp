package spring.rest.serversideapp.dto.converters;

import spring.rest.serversideapp.dto.SensorDTO;
import spring.rest.serversideapp.model.Sensor;

public class ConvertToSensorDTO {
    public static SensorDTO convertToSensorDTO(Sensor sensor) {
        SensorDTO sensorDTO = new SensorDTO();
        sensorDTO.setName(sensor.getName());
        return sensorDTO;
    }
}
