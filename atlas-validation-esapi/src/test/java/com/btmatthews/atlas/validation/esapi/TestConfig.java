package com.btmatthews.atlas.validation.esapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validator;
import javax.validation.ValidatorFactory;


@Configuration
public class TestConfig {

    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }
}
