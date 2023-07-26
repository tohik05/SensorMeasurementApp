package spring.rest.serversideapp.dto;

public class MeasurementDTO {

    private Double temperature;

    private Boolean isRaining;

    private SensorDTO sensor;

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Boolean getRaining() {
        return isRaining;
    }

    public void setRaining(Boolean isRaining) {
        this.isRaining = isRaining;
    }

    public SensorDTO getSensor() {
        return sensor;
    }

    public void setSensor(SensorDTO sensor) {
        this.sensor = sensor;
    }
}
