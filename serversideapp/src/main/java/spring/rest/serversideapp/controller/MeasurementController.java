package spring.rest.serversideapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import spring.rest.serversideapp.dto.MeasurementDTO;
import spring.rest.serversideapp.dto.MeasurementResponse;
import spring.rest.serversideapp.service.impl.MeasurementServiceImpl;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {

    private final MeasurementServiceImpl measurementService;

    @Autowired
    public MeasurementController(MeasurementServiceImpl measurementService) {
        this.measurementService = measurementService;
    }

    @GetMapping
    public ResponseEntity<MeasurementResponse> findAll(@RequestParam(value = "sensor", required = false) String sensorName,
                                                       @RequestParam(value = "raining", required = false) Boolean isRaining) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MeasurementResponse(measurementService.findAll(sensorName, isRaining)));
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody MeasurementDTO measurementDTO, BindingResult bindingResult) {
        measurementService.create(measurementDTO, bindingResult);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable(name = "id") int id,
                                         @RequestBody MeasurementDTO measurementDTO, BindingResult bindingResult) {
        measurementService.update(id, measurementDTO, bindingResult);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Measurement successfully updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable(name = "id") int id) {
        measurementService.delete(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Measurement successfully deleted");
    }
}
