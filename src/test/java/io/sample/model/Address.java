package io.sample.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author rrodriguez
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;
    private String city;
    private String zipcode;

//The cascade used here is for testing convenience, but is usually wrong in real-world models: 
//Many addresses share the same state 
//Cascading persist from child → shared parent can create duplicates 
//Cascades should normally flow from aggregate root → children
    @JoinColumn(name = "state_id", referencedColumnName = "id")
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private State state;

//    @JoinColumn(name = "country_id", referencedColumnName = "id")
//    @ManyToOne(cascade = CascadeType.PERSIST)
//    private Country country;
}
