package spring.rest.clientsideapp;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SendDataToServer {
    public static void main(String[] args) {
        List<String> sensorNames = List.of("Sensor-1", "Sensor-2", "Sensor-3", "Sensor-4", "Sensor-5");
        int counter = 0;
        for (String sensorName : sensorNames) {
            registerNewSensor(sensorName);
            Random random = new Random();
            for (int i = 0; i < 50 + Math.random() * 50; i++) {
                counter++;
                double generateTemperature = Math.random() * 200 - 100; //формула для генерации чисел в диапазоне [-100:100]
                addMeasurements(
                        generateTemperature,
                        random.nextBoolean(),
                        sensorName
                );
            }
        }
        System.out.printf("Всего было произведено %s измерений", counter);
    }

    private static void registerNewSensor(String sensorName) {
        final String url = "http://localhost:8080/sensors";

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("name", sensorName);

        sendPostRequestWithJSONData(url, jsonData);
    }

    private static void addMeasurements(double temperature, boolean isRaining, String sensorName) {
        final String url = "http://localhost:8080/measurements";

        Map<String, Object> sensor = new HashMap<>();
        sensor.put("name", sensorName);
        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("temperature", temperature);
        jsonData.put("raining", isRaining);
        jsonData.put("sensor", sensor);

        sendPostRequestWithJSONData(url, jsonData);
    }

    private static void sendPostRequestWithJSONData(String url, Map<String, Object> jsonData) {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> request = new HttpEntity<>(jsonData, httpHeaders);

        try {
            restTemplate.postForObject(url, request, String.class);
//            System.out.println("Измерение было успешно отправлено на сервер");
        } catch (HttpClientErrorException exception) {
            System.out.println("Произошла ОШИБКА!");
            System.out.println(exception.getMessage());
        }
    }
}
