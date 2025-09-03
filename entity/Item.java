package lk.upalisupermarket.entity;


import java.math.BigDecimal;
import java.time.LocalDateTime;


import org.hibernate.validator.constraints.Length;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import com.fasterxml.jackson.annotation.JsonInclude;

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

@Entity// convert into entity for mapping
@Table(name="item")// map table with this class


@Data//Generate setters and getters,toString() for accessing private properties
@AllArgsConstructor//generate allarguement constructor
@NoArgsConstructor//generate default or noArgument constructor
@JsonInclude(value =Include.NON_NULL)//remove null values from when we call the service (when we want only some feilds of table)
public class Item {


    @Id// Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;

    @Column(name ="item_no",unique = true)
    @Length(max=6)
    @NotNull
    private String item_no ;

    @NotNull
    private String  itemname; 

    @NotNull
    private Integer rop; 

    @NotNull
    private Integer roq; 

    @NotNull
    private Integer profit_ratio; 

    @NotNull
    private BigDecimal unit_size;

    @NotNull
    private BigDecimal purchase_price;
    
    @NotNull
    private BigDecimal sales_price;

    private byte[] item_photo;

    private String note ; 
  
    private LocalDateTime updated_datetime;
    
    @NotNull
    private LocalDateTime added_datetime; 

    private LocalDateTime deleted_datetime;
    
    @NotNull
    private Integer added_user_id; 

    private Integer updated_user_id;

    private Integer deleted_user_id;

    @ManyToOne//Relationship type with Item table to itemstatus table
    @JoinColumn(name="item_status_id",referencedColumnName ="id")
    private ItemStatus item_status_id;//type should be "ItemStatus" since its a foreign key type should from ItemStatus 


    @ManyToOne//Relationship type with Item table to item brand table
    @JoinColumn(name="item_brand_id",referencedColumnName ="id")
    private Brand item_brand_id;//type should be "Brand" since its a foreign key type should from Brand 


    @ManyToOne//Relationship type with Item table to item sub category table
    @JoinColumn(name="item_sub_category_id",referencedColumnName ="id")
    private SubCategory item_sub_category_id;//type should be "Subcategory" since its a foreign key type should from Subcategory 


    @ManyToOne//Relationship type with Item table to pakage type table
    @JoinColumn(name="package_type_id",referencedColumnName ="id")
    private PackageType package_type_id;//type should be "Packagetype" since its a foreign key type should from Packagetype 


    @ManyToOne//Relationship type with Item table to unit type table
    @JoinColumn(name="unit_type_id",referencedColumnName ="id")
    private UnitType unit_type_id;//type should be "Unittype" since its a foreign key type should from Unittype 



  //for the constructor for getting only the needed data from db (quotation request,purchaseorder form)-->apit awashya column withrk arn item select list ekat aywann aform wala
  public  Item(Integer id,String itemname,String item_no,BigDecimal purchase_price){
    this.id=id;
    this.item_no=item_no;
    this.itemname=itemname;
    this.purchase_price=purchase_price;
  }
  
}
