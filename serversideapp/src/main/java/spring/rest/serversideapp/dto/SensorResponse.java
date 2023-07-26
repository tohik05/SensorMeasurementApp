package spring.rest.serversideapp.dto;

import java.util.List;

public class SensorResponse {

    List<SensorDTO> sensorDTOList;

    public SensorResponse(List<SensorDTO> sensorDTOList) {
        this.sensorDTOList = sensorDTOList;
    }

    public List<SensorDTO> getSensorDTOList() {
        return sensorDTOList;
    }

    public void setSensorDTOList(List<SensorDTO> sensorDTOList) {
        this.sensorDTOList = sensorDTOList;
    }
}
