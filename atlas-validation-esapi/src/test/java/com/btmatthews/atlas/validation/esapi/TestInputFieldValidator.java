package com.btmatthews.atlas.validation.esapi;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class TestInputFieldValidator {

    @Autowired
    private Validator validator;

    @Rule
    public ErrorCollector collector = new ErrorCollector();

    private TestForm form;

    @Before
    public void setup() {
        form = new TestForm();
    }

    @Test
    public void valueCannotBeNull() {
        form.setValue(null);
        final Set<ConstraintViolation<TestForm>> errors = validator.validate(form);
        assertEquals(1, errors.size());
    }

    @Test
    public void valueCannotBeNEmpty() {
        form.setValue("");
        final Set<ConstraintViolation<TestForm>> errors = validator.validate(form);
        assertEquals(1, errors.size());
    }

    @Test
    public void valueCannotBeTooLong() {
        form.setValue("0123456789A");
        final Set<ConstraintViolation<TestForm>> errors = validator.validate(form);
        assertEquals(1, errors.size());
    }
}
