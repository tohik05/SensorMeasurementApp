package spring.rest.serversideapp.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import spring.rest.serversideapp.dto.SensorDTO;

@Component
public class SensorValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return SensorDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SensorDTO sensorDTO = (SensorDTO) target;

        if (sensorDTO.getName().isEmpty()) {
            errors.rejectValue("name", "400", "VALIDATOR - Name should not be empty");
        } else if (sensorDTO.getName().length() < 3 || sensorDTO.getName().length() > 30) {
            errors.rejectValue("name", "400", "VALIDATOR - Size should be between 3 and 30 character");
        }
    }
}
