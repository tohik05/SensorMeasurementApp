package spring.rest.serversideapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import spring.rest.serversideapp.dto.SensorDTO;
import spring.rest.serversideapp.dto.SensorResponse;
import spring.rest.serversideapp.service.impl.SensorServiceImpl;


@RestController
@RequestMapping("/sensors")
public class SensorController {

    private final SensorServiceImpl sensorService;

    @Autowired
    public SensorController(SensorServiceImpl sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping()
    public ResponseEntity<SensorResponse> findAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new SensorResponse(sensorService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SensorDTO> read(@PathVariable(name = "id") int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(sensorService.read(id));
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody SensorDTO sensorDTO, BindingResult bindingResult) {
        sensorService.create(sensorDTO, bindingResult);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable(name = "id") int id,
                                         @RequestBody SensorDTO sensorDTO, BindingResult bindingResult) {
        sensorService.update(id, sensorDTO, bindingResult);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Sensor successfully updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable(name = "id") int id) {
        sensorService.delete(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Sensor successfully deleted");
    }
}
