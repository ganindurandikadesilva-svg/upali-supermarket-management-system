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
@Table(name = "grn") // map table with this class


@Data//Generate setters and getters,toString() for accessing private properties
@AllArgsConstructor//generate allarguement constructor
@NoArgsConstructor//generate default or noArgument constructor
public class Grn {

      @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto increment
    private Integer id;
    
    @Column(name ="grn_no",unique = true)
    // @Column(name = "")  when db column name is differnt from tghe name we use
    @Length(max=10)
    @NotNull
    private String grn_no;

    @Length(max=10)
    @NotNull
    private String billno;

    // @Column(name = "")  when db column name is differnt from tghe name we use
    @NotNull
    private BigDecimal grandtotal;

    @NotNull
    private BigDecimal net_amount;

     @NotNull
    private BigDecimal paid_amount;

     @NotNull
    private BigDecimal discount_rate;

    @NotNull
    private LocalDate received_date;

    private String note;

    private LocalDateTime updated_datetime;
    
    @NotNull
    private LocalDateTime added_datetime; 

    private LocalDateTime deleted_datetime;
    
    @NotNull
    private Integer added_user_id; 

    private Integer updated_user_id;

    private Integer deleted_user_id;


    @ManyToOne//Relationship type with Grn table to GrnStatus table
    @JoinColumn(name = "grn_status_id",referencedColumnName = "id")
    private GrnStatus grn_status_id;  //type should be "GrnStatus" since its a foreign key type should from GrnStatus 

    @ManyToOne//Relationship type with Grn table to Supplier table
    @JoinColumn(name = "supplier_id",referencedColumnName = "id")
    private Supplier supplier_id;  //type should be "Supplier" since its a foreign key type should from Supplier 

    @ManyToOne//Relationship type with Grn table to purchae order table
    @JoinColumn(name = "purchase_order_id",referencedColumnName = "id")
    private PurchaseOrder purchase_order_id;  //type should be "PurchaseOrder" since its a foreign key type should from PurchaseOrder 

    @OneToMany(mappedBy ="grn_id",cascade = CascadeType.ALL ,orphanRemoval = true )//orphanRemoval meken innerform eke data remove krnn facility eka enwa
    private List<GrnHasItem> grnHasItemList;

    /* orphanRemoval = true
    If a child (GrnHasItem) is removed from the list grnHasItemList, it will be deleted from the database too.
    This is useful for inner forms or child tables in UI. When you remove an item from the form (but not delete the invoice), JPA deletes that child record automatically. */
    
    /* cascade = CascadeType.ALL
    All operations on the parent (Grn) will cascade to the children (GrnHasItem).
    Saving an Grn also saves all its inventory items. */

    //for the constructor for getting only the needed data from db (grn form)-->apit awashya column withrk arn item select list ekat aywann aform wala
    public  Grn(Integer id,String grn_no,BigDecimal grandtotal,BigDecimal paid_amount,BigDecimal net_amount){
    this.id=id;
    this.grn_no=grn_no;
    this.grandtotal=grandtotal;
    this.net_amount=net_amount;
    this.paid_amount=paid_amount;
 
  }
}
