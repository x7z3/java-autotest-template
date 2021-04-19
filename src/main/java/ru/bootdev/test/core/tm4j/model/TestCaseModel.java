package ru.bootdev.test.core.tm4j.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TestCaseModel {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String key;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
}
