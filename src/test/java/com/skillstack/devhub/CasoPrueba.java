package com.skillstack.devhub;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CasoPrueba {
    String id();
    String descripcion();
    String entrada();
    String tipo();
    String esperado();
}
