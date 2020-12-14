package com.lambdaschool.schools.services;

import com.lambdaschool.schools.models.Instructor;
import com.lambdaschool.schools.models.Slip;
import com.lambdaschool.schools.models.SlipSearch;

import java.util.List;

public interface InstructorService {
    Instructor addAdvice(long id, Slip advice);

    Instructor addAdviceSearch(long id, List<SlipSearch> advice);
}
