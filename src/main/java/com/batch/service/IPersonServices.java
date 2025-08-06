package com.batch.service;


import com.batch.entities.Person;

import java.util.List;

public interface IPersonServices {

    void saveAll(List<Person> personList);
}
