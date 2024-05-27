package com.aem.assignment.core.services;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Product Config")
public @interface ProductConfig {
    @AttributeDefinition(name = "Product Path")
    public String getPath() default "";
}
