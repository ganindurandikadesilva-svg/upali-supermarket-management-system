package lk.upalisupermarket.entity;



import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "supplier_payments_has_grn") // map table with this class


@Data//Generate setters and getters,toString() for accessing private properties
@AllArgsConstructor//generate allarguement constructor
@NoArgsConstructor//generate default or noArgument constructor
public class SupplierPaymentHasGrn {

    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto increment
    private Integer id;

    // @Column(name = "")  when db column name is differnt from tghe name we use if its the same no need to define it
    @NotNull
    private BigDecimal total_amount;

    @NotNull
    private BigDecimal paid_amount;

    @NotNull
    private BigDecimal balance_amount;


    @ManyToOne // Relationship type with supplier_payments_has_grn table to supplierpayment table
    @JoinColumn(name = "supplier_payments_id", referencedColumnName = "id")
    @JsonIgnore//block reading property
    private SupplierPayment supplier_payments_id;// type should be "Invoice" since its a foreign key type should from Invoice

    @ManyToOne // Relationship type with supplier_payments_has_grn table to grn table
    @JoinColumn(name = "grn_id", referencedColumnName = "id")
    private Grn grn_id;// type should be "Grn" since its a foreign key type should from Grn


 


}
