package lk.upalisupermarket.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // convert into entity for mapping
@Table(name = "supplier_payments") // map table with this class


@Data//Generate setters and getters,toString() for accessing private properties
@AllArgsConstructor//generate allarguement constructor
@NoArgsConstructor//generate default or noArgument constructor
public class SupplierPayment {

      @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto increment
    private Integer id;
    
    @Column(name ="bill_no",unique = true)
    // @Column(name = "")  when db column name is differnt from tghe name we use
    @Length(max=10)
    @NotNull
    private String bill_no;

    // @Column(name = "")  when db column name is differnt from the name we use
    @NotNull
    private BigDecimal total_amount;

    @NotNull
    private BigDecimal paid_amount;

    @NotNull
    private BigDecimal balance_amount;

    @NotNull
    private LocalDateTime added_datetime; 

    @NotNull
    private Integer added_user_id;

    private String cheque_no;

    private LocalDate cheque_date;

    private LocalDateTime transfer_datetime; 

    private Integer transfer_id;
    
    private String note;


    @ManyToOne//Relationship type with supplier_payments table to payments_method table
    @JoinColumn(name = "payment_method_id",referencedColumnName = "id")
    private SupplierPaymentMethod payment_method_id;  //type should be "SupplierPaymentMethod" since its a foreign key type should from SupplierPaymentMethod 

    @ManyToOne//Relationship type with supplier_payments table to supplier table
    @JoinColumn(name = "supplier_id",referencedColumnName = "id")
    private Supplier supplier_id;  //type should be "Supplier" since its a foreign key type should from Supplier 

     @OneToMany(mappedBy ="supplier_payments_id",cascade = CascadeType.ALL ,orphanRemoval = true )//orphanRemoval meken innerform eke dat remove krnn facility eka enwa
    private List<SupplierPaymentHasGrn> supplierPaymentHasGrnList;

    

     /* orphanRemoval = true
    If a child (SupplierPaymentHasGrn) is removed from the list supplierPaymentHasGrnList, it will be deleted from the database too.
    This is useful for inner forms or child tables in UI. When you remove an item from the form (but not delete the purchaseorder), JPA deletes that child record automatically. */
    
    /* cascade = CascadeType.ALL
    All operations on the parent (PurchaseOrder) will cascade to the children (SupplierPaymentHasGrn).
    Saving an PurchaseOrder also saves all its inventory items. */

}
