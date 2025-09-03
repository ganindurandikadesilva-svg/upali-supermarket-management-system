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
@Table(name = "purchase_order") // map table with this class


@Data//Generate setters and getters,toString() for accessing private properties
@AllArgsConstructor//generate allarguement constructor
@NoArgsConstructor//generate default or noArgument constructor
public class PurchaseOrder {

      @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto increment
    private Integer id;

    
    @Column(name ="pod_no",unique = true)
    // @Column(name = "")  when db column name is differnt from tghe name we use
    @Length(max=10)
    @NotNull
    private String pod_no;

    // @Column(name = "")  when db column name is differnt from tghe name we use
    @NotNull
    private BigDecimal grandtotal;

    @NotNull
    private LocalDate required_date;

    private String note;

    private LocalDateTime updated_datetime;
    
    @NotNull
    private LocalDateTime added_datetime; 

    private LocalDateTime deleted_datetime;
    
    @NotNull
    private Integer added_user_id; 

    private Integer updated_user_id;

    private Integer deleted_user_id;


    @ManyToOne//Relationship type with PurchaseOrder table to purchaseorderStatus table
    @JoinColumn(name = "purchase_order_status_id",referencedColumnName = "id")
    private PurchaseOrderStatus purchase_order_status_id;  //type should be "PurchaseOrderStatus" since its a foreign key type should from PurchaseOrderStatus 
  
    @ManyToOne//Relationship type with purchaseorder table to Supplier table
    @JoinColumn(name = "supplier_id",referencedColumnName = "id")
    private Supplier supplier_id;  //type should be "Supplier" since its a foreign key type should from Supplier 
    
    @ManyToOne//Relationship type with PurchaseOrder table to quatation table
    @JoinColumn(name = "quotation_id",referencedColumnName = "id")
    private Quotation quotation_id;  //type should be "Quotation" since its a foreign key type should from Quotation 

    @OneToMany(mappedBy ="purchase_order_id",cascade = CascadeType.ALL ,orphanRemoval = true )//orphanRemoval meken innerform eke dat remove krnn facility eka enwa
    private List<PurchaseOrderHasItem> purchaseOrderHasItemList;

     /* orphanRemoval = true
    If a child (PurchaseOrderHasItem) is removed from the list purchaseOrderHasItemList, it will be deleted from the database too.
    This is useful for inner forms or child tables in UI. When you remove an item from the form (but not delete the purchaseorder), JPA deletes that child record automatically. */
    
    /* cascade = CascadeType.ALL
    All operations on the parent (PurchaseOrder) will cascade to the children (PurchaseOrderHasItem).
    Saving an PurchaseOrder also saves all its inventory items. */


//for the constructor for getting only the needed data from db (purchase order form)-->apit awashya column withrk arn item select list ekat aywann aform wala
  public  PurchaseOrder(Integer id,String pod_no,BigDecimal grandtotal){
    this.id=id;
    this.pod_no=pod_no;
    this.grandtotal=grandtotal;
  }


  }
