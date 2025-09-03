package lk.upalisupermarket.entity;

import java.time.LocalDateTime;
import java.util.Set;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // convert into entity for mapping
@Table(name = "supplier") // map table with this class


@Data//Generate setters and getters,toString() for accessing private properties
@AllArgsConstructor//generate allarguement constructor
@NoArgsConstructor//generate default or noArgument constructor
public class Supplier {

      @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto increment
    private Integer id;
    
    @Column(name ="supplier_regno",unique = true)
    // @Column(name = "")  when db column name is differnt from the name we use
    @Length(max=10)
    @NotNull
    private String supplier_regno;

    // @Column(name = "")  when db column name is differnt from tghe name we use
    @NotNull
    private String suppliername;

    @NotNull
    private String email;

    @NotNull
    private String contact_no;

    @NotNull
    private String address;

    @NotNull
    private String account_no;

    @NotNull
    private String account_name;

    @NotNull
    private String bank_name;

    @NotNull
    private String bank_branch;
 
    @NotNull
    private String contactperson_name;

    @NotNull
    private String contactperson_telno;

    @NotNull
    private String buisness_reg_no;
 
    private String note;

    private LocalDateTime updated_datetime;
    
    @NotNull
    private LocalDateTime added_datetime; 

    private LocalDateTime deleted_datetime;
    
    @NotNull
    private Integer added_user_id; 

    private Integer updated_user_id;

    private Integer deleted_user_id;


    @ManyToOne//Relationship type with Supplier table to supplier_status table
    @JoinColumn(name = "supplier_status_id",referencedColumnName = "id")
    private SupplierStatus supplier_status_id;  //type should be "SupplierStatus" since its a foreign key type should from EmployeeStatus 

    @ManyToMany(cascade = CascadeType.MERGE) //Role eke daata access krnna crud operation ktnna nm cascade type all krnn aone
    //many to many nisa join Table
    @JoinTable(name = "supplier_has_item",joinColumns = @JoinColumn(name="supplier_id"),inverseJoinColumns = @JoinColumn(name="item_id"))
    private Set<Item> suppliedItems;//This defines the Java field holding the set of items a supplier can supply.
//meka map kre many to many anith ewa one to many kre(pod-->purchaseordehasitem)

    /*@ManyToMany(cascade = CascadeType.MERGE)
This says that a Supplier can supply many Items, and an Item can be supplied by many Suppliers.

    cascade = CascadeType.MERGE means:
If the Supplier is updated (merged), the associated items will be merged too
    
    joinColumns = @JoinColumn(name = "supplier_id")
This is the foreign key pointing to the Supplier in the join table.
    
    inverseJoinColumns = @JoinColumn(name = "item_id")
This is the foreign key pointing to the Item in the join table.

private Set<Item> suppliedItems;
This defines the Java field holding the set of items a supplier can supply.*/


}
