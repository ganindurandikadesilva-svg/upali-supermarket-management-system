package lk.upalisupermarket.entity;


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
@Table(name = "quotation") // map table with this class


@Data//Generate setters and getters,toString() for accessing private properties
@AllArgsConstructor//generate allarguement constructor
@NoArgsConstructor//generate default or noArgument constructor
public class Quotation {

      @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto increment
    private Integer id;

    
    @Column(name ="quotation_no",unique = true)
    // @Column(name = "")  when db column name is differnt from tghe name we use
    @Length(max=10)
    @NotNull
    private String quotation_no;


    @NotNull
    private LocalDate received_date;

    @NotNull
    private LocalDate deadline_date;

    private String note;

    private LocalDateTime updated_datetime;
    
    @NotNull
    private LocalDateTime added_datetime; 

    private LocalDateTime deleted_datetime;
    
    @NotNull
    private Integer added_user_id; 

    private Integer updated_user_id;

    private Integer deleted_user_id;


    @ManyToOne//Relationship type with Quotation table to quotationstatus table
    @JoinColumn(name = "quotation_status_id",referencedColumnName = "id")
    private QuotationStatus quotation_status_id;  //type should be "QuotationStatus" since its a foreign key type should from QuotationStatus 
  
    @ManyToOne//Relationship type with Quotation table to supplier table
    @JoinColumn(name = "supplier_id",referencedColumnName = "id")
    private Supplier supplier_id;  //type should be "Supplier" since its a foreign key type should from Supplier 
    
    @ManyToOne//Relationship type with quatation table to quatation request table
    @JoinColumn(name = "quotation_request_id",referencedColumnName = "id")
    private QuotationRequest quotation_request_id;  //type should be "QuotationRequest" since its a foreign key type should from QuotationRequest 

    @OneToMany(mappedBy ="quotation_id",cascade = CascadeType.ALL ,orphanRemoval = true )//orphanRemoval meken innerform eke data remove krnn facility eka enwa
    private List<QuotationHasItem> quotationHasItemList;

    /* orphanRemoval = true
    If a child (QuotationHasItem) is removed from the list quotationHasItemList, it will be deleted from the database too.
    This is useful for inner forms or child tables in UI. When you remove an item from the form (but not delete the quotation), JPA deletes that child record automatically. */
    
    /* cascade = CascadeType.ALL
    All operations on the parent (Quotation) will cascade to the children (QuotationHasItem).
    Saving an Quotation also saves all its inventory items. */


    
    //for the constructor for getting only the needed data from db (purchase order form)-->apit awashya column withrk arn item select list ekat aywann aform wala
  public  Quotation(Integer id,String quotation_no,LocalDate received_date){
    this.id=id;
    this.quotation_no=quotation_no;
    this.received_date=received_date;
  }
    
}
