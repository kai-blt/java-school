package com.lambdaschool.schools.services;

import com.lambdaschool.schools.exceptions.ResourceNotFoundException;
import com.lambdaschool.schools.models.Instructor;
import com.lambdaschool.schools.models.Slip;
import com.lambdaschool.schools.repositories.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "instructorService")
public class InstructorServiceImpl implements  InstructorService{
    @Autowired
    InstructorRepository instructorepo;


    //Finds Instructor by Id and then sets its advice to the Advice
    //received as a response from the external API!
    @Override
    public Instructor addAdvice(long id, Slip advice) {
        Instructor newInstructor = instructorepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor id " + id + " not found!"));
        newInstructor.setAdvice(advice.getAdvice());
        return newInstructor;
    }
}
