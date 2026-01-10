package io.sample.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.CascadeType;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author rrodriguez
 */
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    private Integer status;

    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JoinColumn(name = "card_id", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Card card;
    
    private Integer intValue;
    private Long longValue;
    private Float floatValue;
    private Double doubleValue;
    private BigInteger bigInteger;
    private BigDecimal bigDecimal;
}
