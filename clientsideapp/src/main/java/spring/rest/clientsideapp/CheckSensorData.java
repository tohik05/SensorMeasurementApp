package spring.rest.clientsideapp;

import org.springframework.web.client.RestTemplate;
import spring.rest.clientsideapp.dto.MeasurementDTO;
import spring.rest.clientsideapp.dto.MeasurementResponse;
import spring.rest.clientsideapp.dto.SensorDTO;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CheckSensorData {
    public static void main(String[] args) {

        for (String sensorName : sensorNames()) {
            System.out.printf("От %s было получено %s измерений%n", sensorName, countOfMeasurements(sensorName));
            System.out.printf("Самая высокая температура: %.2f%n", getHigherTemperature(sensorName));
            System.out.printf("Самая низкая температура: %.2f%n", getLowerTemperature(sensorName));
            System.out.printf("Средняя температура: %.2f%n", getAverageTemperature(sensorName));
            System.out.printf("Дождь был зафиксирован - %s раз(а)%n", countRainyMeasurements(sensorName));
            System.out.printf("Солнце было зафиксировано - %s раз(а)%n", countSunnyMeasurements(sensorName));
            System.out.println("---------------------------------------");
        }
    }

    private static List<MeasurementDTO> getMeasurements() {
        final RestTemplate restTemplate = new RestTemplate();
        final String url = "http://localhost:8080/measurements";

        MeasurementResponse response = restTemplate.getForObject(url, MeasurementResponse.class);
        if (response == null || response.getMeasurements() == null) {
            return Collections.emptyList();
        }
        return response.getMeasurements();
    }

    private static Map<SensorDTO, Double> getTemperatureMap() {
        return getMeasurements()
                .stream()
                .collect(Collectors.toMap(MeasurementDTO::getSensor, MeasurementDTO::getTemperature));
    }

    private static TreeSet<String> sensorNames(){
        return getTemperatureMap().keySet()
                .stream()
                .map(SensorDTO::getName)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    private static long countOfMeasurements(String sensorName){
        return getTemperatureMap().keySet()
                .stream()
                .filter(sensorDTO -> sensorDTO.getName().equals(sensorName))
                .count();
    }

    private static double getHigherTemperature(String sensorName) {
        return getTemperatureMap().entrySet()
                .stream()
                .filter(sensor -> sensor.getKey().getName().equals(sensorName))
                .mapToDouble(Map.Entry::getValue)
                .max()
                .getAsDouble();
    }

    private static double getLowerTemperature(String sensorName) {
        return getTemperatureMap().entrySet()
                .stream()
                .filter(sensor -> sensor.getKey().getName().equals(sensorName))
                .mapToDouble(Map.Entry::getValue)
                .min()
                .getAsDouble();
    }

    private static double getAverageTemperature(String sensorName) {
        return getTemperatureMap().entrySet()
                .stream()
                .filter(sensor -> sensor.getKey().getName().equals(sensorName))
                .mapToDouble(Map.Entry::getValue)
                .average()
                .getAsDouble();
    }

    private static long countRainyMeasurements(String sensorName) {
        return getMeasurements()
                .stream()
                .filter(name -> name.getSensor().getName().equals(sensorName))
                .filter(MeasurementDTO::getRaining)
                .count();
    }

    private static long countSunnyMeasurements(String sensorName) {
        return getMeasurements()
                .stream()
                .filter(name -> name.getSensor().getName().equals(sensorName))
                .filter(Predicate.not(MeasurementDTO::getRaining))
                .count();
    }
}
