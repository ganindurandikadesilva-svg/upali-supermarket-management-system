package lk.upalisupermarket.entity;

import java.math.BigDecimal;
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
@Table(name = "invoice") // map table with this class


@Data//Generate setters and getters,toString() for accessing private properties
@AllArgsConstructor//generate allarguement constructor
@NoArgsConstructor//generate default or noArgument constructor
public class Invoice {

    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto increment
    private Integer id;

    @Column(name ="invoice_no",unique = true)
    // @Column(name = "")  when db column name is differnt from tghe name we use
    @Length(max=10)
    @NotNull
    private String invoice_no;

    // @Column(name = "")  when db column name is differnt from tghe name we use
  
    private String customer_name;

   
    private String contact_no;

     @NotNull
    private BigDecimal total_amount;

     @NotNull
    private BigDecimal discount_amount;

     


     @NotNull
    private BigDecimal net_amount;
   
    private String note;
    
    @NotNull
    private LocalDateTime added_datetime; 

    @NotNull
    private Integer added_user_id; 

    @ManyToOne//Relationship type with Invoice table to Customer table
    @JoinColumn(name = "customer_id",referencedColumnName = "id")
    private Customer customer_id;  //type should be "Customer" since its a foreign key type should from Customer 

    @OneToMany(mappedBy ="invoice_id",cascade = CascadeType.ALL ,orphanRemoval = true )//orphanRemoval meken innerform eke data remove krnn facility eka enwa
    private List<InvoiceHasInventory> invoiceHasInventoryList;  //defines a list of child entities (InvoiceHasInventory) that belong to a parent entity-->  Invoice.

    /* orphanRemoval = true
    If a child (InvoiceHasInventory) is removed from the list invoiceHasInventoryList, it will be deleted from the database too.
    This is useful for inner forms or child tables in UI. When you remove an item from the form (but not delete the invoice), JPA deletes that child record automatically. */
    
    /* cascade = CascadeType.ALL
    All operations on the parent (Invoice) will cascade to the children (InvoiceHasInventory).
    Saving an Invoice also saves all its inventory items. */
}
