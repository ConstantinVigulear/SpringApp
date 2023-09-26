package com.vigulear.spring.validator;

import com.vigulear.spring.model.Person;

public class PersonValidator implements Validator {
  @Override
  public <T> boolean isValid(T object) {
    Person person = (Person) object;

    return !(person.getName().isEmpty()
        || person.getSurname().isEmpty()
        || person.getEmail().isEmpty());
  }
}
