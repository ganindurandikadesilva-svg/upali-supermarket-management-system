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
@Table(name = "customer") // map table with this class


@Data//Generate setters and getters,toString() for accessing private properties
@AllArgsConstructor//generate allarguement constructor
@NoArgsConstructor//generate default or noArgument constructor
public class Customer {

    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto increment
    private Integer id;

    
    @Column(name ="cus_no",unique = true)
    // @Column(name = "")  when db column name is differnt from tghe name we use
    @Length(max=10)
    @NotNull
    private String cus_no;

    // @Column(name = "")  when db column name is differnt from tghe name we use
    @NotNull
    private String customername;

    @NotNull
    private String customeraddress;

    @NotNull
    private String email;

    @NotNull
    @Length(max=12,min = 10)
    private String nic;

    @NotNull
    private String contactno;
   
    private String note;

    private BigDecimal loyalty_point ;

    private LocalDateTime updated_datetime;
    
    @NotNull
    private LocalDateTime added_datetime; 

    private LocalDateTime deleted_datetime;
    
    @NotNull
    private Integer added_user_id; 

    private Integer updated_user_id;

    private Integer deleted_user_id;


    @ManyToOne//Relationship type with Customer table to customer_status table
    @JoinColumn(name = "customer_status_id",referencedColumnName = "id")
    private CustomerStatus customer_status_id;  //type should be "CustomerStatus" since its a foreign key type should from CustomerStatus 

    
}
