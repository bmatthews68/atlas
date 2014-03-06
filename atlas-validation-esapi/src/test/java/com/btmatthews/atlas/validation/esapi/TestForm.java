package com.btmatthews.atlas.validation.esapi;

public class TestForm {

    @InputField(pattern="Value", maximumLength = 10, allowNull = false)
    private String value;

    public String getValue() { return value; }
    public void setValue(final String value) { this.value = value; }
}
