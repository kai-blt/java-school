package com.lambdaschool.schools.controllers;

import com.lambdaschool.schools.exceptions.ResourceNotFoundException;
import com.lambdaschool.schools.models.*;
import com.lambdaschool.schools.services.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(value = "/instructors") // optional
public class InstructorController
{
    @Autowired
    InstructorService instructorService;

    /*
     * Creates the object that is needed to do a client side Rest API call.
     * We are the client getting data from a remote API.
     * We can share this template among endpoints
     */
    private RestTemplate restTemplate = new RestTemplate();


    // http://localhost:2019/instructors/instructor/{instructorid}/advice
    @GetMapping(value = "/instructor/{instructorid}/advice")
    public ResponseEntity<?> getAdvice(@PathVariable long instructorid)
    {
        //Tell RestTemplate what format to expect
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.TEXT_HTML));
        restTemplate.getMessageConverters().add(converter);

        // create the url to access the API
        String requestURL = "https://api.adviceslip.com/advice";

        // create the responseType expected. SlipAdviceReturnData is the data type we are expecting back from the API!
        ParameterizedTypeReference<SlipAdviceReturnData> responseType = new ParameterizedTypeReference<>() {
        };

        // create the response entity. do the get and get back information
        ResponseEntity<SlipAdviceReturnData> responseEntity = restTemplate.exchange(
                requestURL,
                HttpMethod.GET,
                null,
                responseType);
        // we want to return the SlipAdviceReturnData data. From the data that gets returned in the body,
        // get the Slip data only and return it.
        // putting the data into its own object first, prevents the data from being reported to client inside of
        // an embedded. So the response will look more like our clients are use to!
        Slip advice = responseEntity.getBody().getSlip();

        //Make a new Instructor to hold the returned and updated Instructor from addAdvice() method
        //called from the instructorService
        Instructor newInstructor = instructorService.addAdvice(instructorid, advice);
        return new ResponseEntity<>(newInstructor, HttpStatus.OK);
    }

    // http://localhost:2019/instructors/instructor/{instructorid}/advice/{searchterm}
    @GetMapping(value = "/instructor/{instructorid}/advice/{searchterm}")
    public ResponseEntity<?> getAdviceSearch(@PathVariable long instructorid, @PathVariable String searchterm)
    {
        //Tell RestTemplate what format to expect
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        restTemplate.getMessageConverters().add(converter);

        // create the url to access the API
        String requestURL = "https://api.adviceslip.com/advice/search/" + searchterm;

        // create the responseType expected. SlipAdviceReturnDataSearch is the data type we are expecting back from the API!
        ParameterizedTypeReference<SlipAdviceReturnDataSearch> responseType = new ParameterizedTypeReference<>() {
        };

        // create the response entity. do the get and get back information
        ResponseEntity<SlipAdviceReturnDataSearch> responseEntity = restTemplate.exchange(
                requestURL,
                HttpMethod.GET,
                null,
                responseType);

        List<SlipSearch> advice = responseEntity.getBody().getSlips();

        if (advice == null) {
            throw new ResourceNotFoundException("no message available");
        }

        //Make a new Instructor to hold the returned and updated Instructor from addAdvice() method
        //called from the instructorService
        Instructor newInstructor = instructorService.addAdviceSearch(instructorid, advice);
        return new ResponseEntity<>(newInstructor, HttpStatus.OK);
    }

}