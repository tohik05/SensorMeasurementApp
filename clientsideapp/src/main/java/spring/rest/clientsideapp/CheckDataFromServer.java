package spring.rest.clientsideapp;

import org.springframework.web.client.RestTemplate;
import spring.rest.clientsideapp.dto.MeasurementDTO;
import spring.rest.clientsideapp.dto.MeasurementResponse;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CheckDataFromServer {
    public static final List<String> sensorNames = List.of("Sensor-1", "Sensor-2", "Sensor-3", "Sensor-4", "Sensor-5");

    public static void main(String[] args) {
        List<Double> results = getTemperature();

        System.out.printf("Всего было получено %s измерений%n", results.size());
        System.out.printf("Самая высокая температура: %.2f%n", Collections.max(results));
        System.out.printf("Самая низкая температура: %.2f%n", Collections.min(results));
        System.out.printf("Средняя температура: %.2f%n", results.stream().collect(Collectors.averagingDouble(Double::doubleValue)));
        System.out.printf("Общее количество дождливых измерений - %s%n", countAllRainingMeasurements());
        System.out.printf("Общее количество солнечных измерений - %s%n", countAllSunnyMeasurements());
        System.out.println("-------------------------------------------");
        for (String sensorName : sensorNames) {
            long rainy = countRainyMeasurementsBySensorName(sensorName);
            long sunny = countSunnyMeasurementsBySensorName(sensorName);
            System.out.printf("%s произвел %s измерений%n", sensorName, rainy + sunny);
            System.out.printf("%s - дождь был зафиксирован - %s раз(а)%n", sensorName, rainy);
            System.out.printf("%s - солнце было зафиксировано - %s раз(а)%n", sensorName, sunny);
            System.out.println("-------------------------------------------------");
        }
    }

    private static List<Double> getTemperature() {
        final RestTemplate restTemplate = new RestTemplate();
        final String url = "http://localhost:8080/measurements";

        MeasurementResponse response = restTemplate.getForObject(url, MeasurementResponse.class);
        if (response == null || response.getMeasurements() == null) {
            return Collections.emptyList();
        }
        return response.getMeasurements()
                .stream()
                .map(MeasurementDTO::getTemperature)
                .collect(Collectors.toList());
    }

    private static long countRainyMeasurementsBySensorName(String sensorName) {
        final RestTemplate restTemplate = new RestTemplate();
        final String url = "http://localhost:8080/measurements?sensor=" + sensorName;

        MeasurementResponse response = restTemplate.getForObject(url, MeasurementResponse.class);
        if (response == null || response.getMeasurements() == null) {
            return 0;
        }
        return response.getMeasurements()
                .stream()
                .filter(MeasurementDTO::getRaining)
                .count();
    }

    private static long countSunnyMeasurementsBySensorName(String sensorName) {
        final RestTemplate restTemplate = new RestTemplate();
        final String url = "http://localhost:8080/measurements?sensor=" + sensorName;

        MeasurementResponse response = restTemplate.getForObject(url, MeasurementResponse.class);
        if (response == null || response.getMeasurements() == null) {
            return 0;
        }
        return response.getMeasurements()
                .stream()
                .filter(Predicate.not(MeasurementDTO::getRaining))
                .count();
    }

    private static int countAllRainingMeasurements() {
        final RestTemplate restTemplate = new RestTemplate();
        final String url = "http://localhost:8080/measurements?raining=true";

        MeasurementResponse response = restTemplate.getForObject(url, MeasurementResponse.class);
        if (response == null || response.getMeasurements() == null) {
            return 0;
        }
        return response.getMeasurements().size();
    }

    private static int countAllSunnyMeasurements() {
        final RestTemplate restTemplate = new RestTemplate();
        final String url = "http://localhost:8080/measurements?raining=false";

        MeasurementResponse response = restTemplate.getForObject(url, MeasurementResponse.class);
        if (response == null || response.getMeasurements() == null) {
            return 0;
        }
        return response.getMeasurements().size();
    }
}
