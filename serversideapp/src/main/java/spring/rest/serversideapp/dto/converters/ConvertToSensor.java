package spring.rest.serversideapp.dto.converters;

import spring.rest.serversideapp.dto.SensorDTO;
import spring.rest.serversideapp.model.Sensor;

public class ConvertToSensor {
    public static Sensor convertToSensor(SensorDTO sensorDTO) {
        Sensor sensor = new Sensor();
        sensor.setName(sensorDTO.getName());
        return sensor;
    }
}
