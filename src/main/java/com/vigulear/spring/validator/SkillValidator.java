package com.vigulear.spring.validator;

import com.vigulear.spring.model.Skill;

public class SkillValidator implements Validator{
    @Override
    public <T> boolean isValid(T object) {
        Skill skill = (Skill) object;
        return !(skill.getName().isEmpty() || skill.getDomain() == null || skill.getLevel() == null);
    }
}
