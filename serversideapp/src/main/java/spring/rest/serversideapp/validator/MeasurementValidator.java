package spring.rest.serversideapp.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import spring.rest.serversideapp.dto.MeasurementDTO;

@Component
public class MeasurementValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return MeasurementDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MeasurementDTO measurementDTO = (MeasurementDTO) target;

        if (measurementDTO.getTemperature() == null) {
            errors.rejectValue("temperature", "400", "VALIDATOR - Weather temperature must not be null");
        } else if (measurementDTO.getTemperature() < -100.0 || measurementDTO.getTemperature() > 100.0) {
            errors.rejectValue("temperature", "400", "VALIDATOR - Temperature should be between -100 and 100");
        }

        if (measurementDTO.getRaining() == null) {
            errors.rejectValue("raining", "400", "VALIDATOR - Weather state must not be null");
        }

        if (measurementDTO.getSensor() == null) {
            errors.rejectValue("sensor", "400", "VALIDATOR - Sensor name must not be null");
        }
    }
}
