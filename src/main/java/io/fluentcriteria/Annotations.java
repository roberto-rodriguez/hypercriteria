package io.fluentcriteria;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author rrodriguez
 */
public class Annotations {

    @Retention(RetentionPolicy.RUNTIME)
    public @interface ConstructorName {

        String value();
    } 

    //Specify which attribute on the domain class to map to. MIght include dotted path name.
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Mapping {

        String value();
    }
 
 
}
