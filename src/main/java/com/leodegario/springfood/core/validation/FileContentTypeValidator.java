package com.leodegario.springfood.core.validation;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class FileContentTypeValidator implements ConstraintValidator<FileContentType, MultipartFile> {

    private List<String> allowedList;

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext constraintValidatorContext) {
        return value == null || allowedList.contains(value.getContentType());
    }


    @Override
    public void initialize(FileContentType constraintAnnotation) {
        this.allowedList = Arrays.asList(constraintAnnotation.allowed());
    }
}
