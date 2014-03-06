package com.btmatthews.atlas.validation.esapi;

import org.owasp.esapi.ESAPI;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class InputFieldValidator implements ConstraintValidator<InputField, Object> {

    /**
     * The name of the input field.
     */
    private String context;

    /**
     * The key used to retrieve the regular expression for validation of the input field from the ESAPI configuration.
     */
    private String pattern;

    /**
     * The maximum length of the input field.
     */
    private int maximumLength;

    /**
     * Indicates whether the input field can be {@code null} or not.
     */
    private boolean allowNull;


    /**
     * Initialise the validator copying the configuration from the {@code inputField} annotation.
     *
     * @param inputField The annotation used to configure the validator.
     */
    @Override
    public void initialize(final InputField inputField) {
        context = inputField.context();
        pattern = inputField.pattern();
        maximumLength = inputField.maximumLength();
        allowNull = inputField.allowNull();
    }

    /**
     * Perform the input field validation.
     *
     * @param object           The input field value.
     * @param validatorContext The validation context.
     * @return {@code true} if input field value was valid. Otherwise, {@code false}.
     */
    @Override
    public boolean isValid(final Object object,
                           final ConstraintValidatorContext validatorContext) {
        return ESAPI.validator().isValidInput(context, (String) object, pattern, maximumLength, allowNull);
    }
}
