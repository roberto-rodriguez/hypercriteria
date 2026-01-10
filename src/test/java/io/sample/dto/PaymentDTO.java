package io.sample.dto;
 
import java.io.Serializable; 
import java.util.Date; 
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author rrodriguez
 */
@Getter
@Setter
public class PaymentDTO implements Serializable{

    private Long id;

    private Long loanId;

    private Double amount;

    private Long mtn;

    private Long omnexClientId;

    private String borrowerId;

    private Integer status;

    private String repaymentId;

    private String webhookRequest;

    private String loandiskRequest;

    private String loandiskResponse;

    private String exception;

    private Date creationDate;

    private String clientFirstName;
    private String clientLastName;
    private Long clientId;

    public PaymentDTO() {
    }

    public PaymentDTO(Long id, Long loanId, Double amount, Long mtn, Long omnexClientId, String borrowerId, Integer status, String repaymentId, String webhookRequest, String loandiskRequest, String loandiskResponse, String exception, Date creationDate, String clientFirstName, String clientLastName, Long clientId) {
        this.id = id;
        this.loanId = loanId;
        this.amount = amount;
        this.mtn = mtn;
        this.omnexClientId = omnexClientId;
        this.borrowerId = borrowerId;
        this.status = status;
        this.repaymentId = repaymentId;
        this.webhookRequest = webhookRequest;
        this.loandiskRequest = loandiskRequest;
        this.loandiskResponse = loandiskResponse;
        this.exception = exception;
        this.creationDate = creationDate;
        this.clientFirstName = clientFirstName;
        this.clientLastName = clientLastName;
        this.clientId = clientId;
    }
 
     
}
