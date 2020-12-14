package com.lambdaschool.schools.services;

import com.lambdaschool.schools.models.Instructor;
import com.lambdaschool.schools.models.Slip;

public interface InstructorService {
    Instructor addAdvice(long id, Slip advice);
}
