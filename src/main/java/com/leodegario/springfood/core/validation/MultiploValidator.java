package com.leodegario.springfood.core.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class MultiploValidator implements ConstraintValidator<Multiplo, Number> {

    private int numeroMultiplo;

    @Override
    public boolean isValid(Number value, ConstraintValidatorContext constraintValidatorContext) {
        boolean valido = true;

        if(value != null){
            var valorDecimal = BigDecimal.valueOf(value.doubleValue());
            var multiploDecimal = BigDecimal.valueOf(this.numeroMultiplo);
            var resto = valorDecimal.remainder(multiploDecimal);

            valido = BigDecimal.ZERO.compareTo(resto) == 0;

        }

        return valido;
    }


    //Inicializa a instancia do validador
    @Override
    public void initialize(Multiplo constraintAnnotation) {
        this.numeroMultiplo = constraintAnnotation.numero();
    }
}
