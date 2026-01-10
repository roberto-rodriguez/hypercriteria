package io.sample.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

/**
 *
 * @author Author <@author>
 */
@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private Boolean active;

    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @JoinColumn(name = "address_id", referencedColumnName = "id")
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Address address;

    //The cascade used here is for testing convenience, but is usually wrong in real-world models: 
    //Many users share the same role 
    //Cascading persist from child → shared parent can create duplicates 
    //Cascades should normally flow from aggregate root → children
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Role role;

    @OneToMany(
            mappedBy = "user",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true
    )
    List<Payment> payments;

    public void addPaymentWithNumericValues(Integer n) {
        payments.add(Payment.builder()
                .intValue(n)
                .longValue(n.longValue())
                .floatValue(n.floatValue())
                .doubleValue(n.doubleValue())
                .bigInteger(new BigInteger(n.toString()))
                .bigDecimal(new BigDecimal(n.toString()))
                .user(this)
                .build()
        );
    }

    public void addPayment(Payment payment) {
        payments.add(payment);
        payment.setUser(this);
    }

}
