package com.lambdaschool.schools.handlers;

import com.lambdaschool.schools.models.ValidationError;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

@Component
public class HelperFunctions {
    public List<ValidationError> getConstraintViolation(Throwable cause) {
        // Find any data violations that might be associated with the error and report them
        // data validations get wrapped in other exceptions as we work through the Spring
        // exception chain. Hence we have to search the entire Spring Exception Stack
        // to see if we have any violation constraints.
        while ((cause != null) && !(cause instanceof ConstraintViolationException || cause instanceof MethodArgumentNotValidException)) {
            System.out.println(cause.getClass().toString());
            cause = cause.getCause();
        }

        List<ValidationError> listVE = new ArrayList<>();

        // we know that cause either null or an instance of ConstraintViolationException or MethodArgumentNotValidException
        if (cause != null) {
            if (cause instanceof ConstraintViolationException) {
                // this would be an exception from Hibernate
                ConstraintViolationException ex = (ConstraintViolationException) cause;
                ValidationError newVe = new ValidationError();
                newVe.setCode(ex.getMessage());
                newVe.setMessage(ex.getConstraintName());
                listVE.add(newVe);
            } else {
                if (cause instanceof MethodArgumentNotValidException) {
                    // this would be an exception from the @Valid exception
                    MethodArgumentNotValidException ex = (MethodArgumentNotValidException) cause;

                    List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
                    for (FieldError err : fieldErrors)
                    {
                        ValidationError newVe = new ValidationError();
                        newVe.setCode(err.getField());
                        newVe.setMessage(err.getDefaultMessage());
                        listVE.add(newVe);
                    }
                } else {
                    System.out.println("Error in producing constraint violations exceptions. " +
                        "If we see this in the console a major logic error has occurred in the " +
                        "helperfunction.getConstraintViolation method that we should investigate. " +
                        "Note the application will keep running as this only affects exception reporting!");
                }
            }
        }
        return listVE;
    }
}