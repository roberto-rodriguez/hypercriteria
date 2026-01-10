package io.hypercriteria;

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

    //Specify which property on the domain class to map to. MIght include dotted path name.
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Mapping {

        String value();
    }
 
 
}
