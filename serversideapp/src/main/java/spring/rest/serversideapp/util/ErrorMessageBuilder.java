package spring.rest.serversideapp.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

public class ErrorMessageBuilder {

    public static String errorMessage(BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder();
        List<FieldError> errorList = bindingResult.getFieldErrors();
        for (FieldError error : errorList) {
            errorMessage
                    .append("Error was found in field - ")
                    .append(error.getField())
                    .append(". ")
                    .append(error.getDefaultMessage())
                    .append(". ");
        }
        return errorMessage.toString();
    }
}
