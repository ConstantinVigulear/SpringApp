package com.vigulear.spring;

import com.vigulear.spring.configuration.MyConfig;
import com.vigulear.spring.model.Person;
import com.vigulear.spring.model.Skill;
import com.vigulear.spring.model.SkillDomain;
import com.vigulear.spring.model.SkillLevel;
import com.vigulear.spring.service.PersonService;
import com.vigulear.spring.service.SkillService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {

  public static void main(String[] args) {

    AnnotationConfigApplicationContext context =
        new AnnotationConfigApplicationContext(MyConfig.class);

    Skill skill1 =
        context
            .getBean(Skill.class)
            .setDomain(SkillDomain.DEVOPS)
            .setLevel(SkillLevel.LOW)
            .setName("Skill1");

    Skill skill2 =
        context
            .getBean(Skill.class)
            .setDomain(SkillDomain.SECURITY)
            .setLevel(SkillLevel.ADVANCED)
            .setName("Skill2");

    Person person =
        context
            .getBean(Person.class)
            .setName("First")
            .setSurname("First")
            .setEmail("first.first@gmail.com");

    PersonService personService = context.getBean(PersonService.class);
    person.addSkill(skill1);
    personService.persist(person);
    person = personService.findByEntity(person);

    SkillService skillService = context.getBean(SkillService.class);
    skillService.persist(skill2);
    skill2 = skillService.findByEntity(skill2);

    person.addSkill(skill2);

    person = personService.update(person);
  }
}
