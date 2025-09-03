package lk.upalisupermarket.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // convert into entity for mapping
@Table(name = "customer_payment") // map table with this class


@Data//Generate setters and getters,toString() for accessing private properties
@AllArgsConstructor//generate allarguement constructor
@NoArgsConstructor//generate default or noArgument constructor
public class CustomerPayment {

      @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto increment
    private Integer id;

    
    @Column(name ="billno",unique = true)
    // @Column(name = "")  when db column name is differnt from tghe name we use
    @Length(max=10)
    @NotNull
    private String billno;

    // @Column(name = "")  when db column name is differnt from the name we use
    @NotNull
    private BigDecimal total_amount;

    @NotNull
    private BigDecimal paid_amount;

    @NotNull
    private BigDecimal balance_amount;

    @NotNull
    private LocalDateTime added_datetime; 

    private LocalDateTime transfer_datetime; 

    private Integer transfer_id;

    private String transfer_name;
    
    private String note;


    @ManyToOne//Relationship type with customer_payment table to customer_payment_method table
    @JoinColumn(name = "customer_payment_method_id",referencedColumnName = "id")
    private CustomerPaymentMethod customer_payment_method_id;  //type should be "CustomerPaymentMethod" since its a foreign key type should from CustomerPaymentMethod 

    @ManyToOne//Relationship type with customer_payment table to invoice table
    @JoinColumn(name = "invoice_id",referencedColumnName = "id")
    private Invoice invoice_id;  //type should be "Invoice" since its a foreign key type should from Invoice 

    
}
