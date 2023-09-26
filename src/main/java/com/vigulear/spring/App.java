package com.vigulear.spring;

import com.vigulear.spring.model.Person;
import com.vigulear.spring.model.Skill;
import com.vigulear.spring.model.SkillDomain;
import com.vigulear.spring.model.SkillLevel;
import com.vigulear.spring.service.PersonService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

  public static void main(String[] args) {

    ClassPathXmlApplicationContext context =
        new ClassPathXmlApplicationContext("applicationContext.xml");

    Skill skill1 = context.getBean(Skill.class);
    skill1.setDomain(SkillDomain.DEVOPS);
    skill1.setLevel(SkillLevel.LOW);
    skill1.setName("Skill1");

    Skill skill2 = context.getBean(Skill.class);
    skill2.setDomain(SkillDomain.SECURITY);
    skill2.setLevel(SkillLevel.ADVANCED);
    skill2.setName("Skill2");

    Person person = context.getBean(Person.class);
    person.setName("First");
    person.setSurname("First");
    person.setEmail("first.first@gmail.com");

    person.addSkill(skill1);

    PersonService personService = context.getBean(PersonService.class);

    personService.persist(person);

    person = personService.findByEntity(person);

    person.addSkill(skill2);

    person = personService.update(person);

  }
}
