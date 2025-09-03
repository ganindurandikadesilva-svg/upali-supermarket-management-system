package lk.upalisupermarket.entity;



import java.time.LocalDate;
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

@Entity// convert into entity for mapping
@Table(name="employee")// map table with this class


@Data//Generate setters and getters,toString() for accessing private properties
@AllArgsConstructor//generate allarguement constructor
@NoArgsConstructor//generate default or noArgument constructor
public class Employee {

    @Id// Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;

    @Column(name ="emp_no",unique = true)
    @Length(max=8)
    @NotNull
    private String emp_no ;

    @NotNull
    private String  fullname; 

    @NotNull
    private String callingname; 

    @Column(name ="email",unique = true)
    @NotNull
    private String email;

    @NotNull
    private String gender;

    @NotNull
    private LocalDate dob;

    @Column(name ="mobile_no",unique = true)
    @Length(max=10)
    @NotNull
    private String mobile_no;

    @NotNull
    private String civil_status;

    @NotNull
    private String address;
    
    @Length(max=10)
    private String land_no;

    private String note ; 

    @Column(name ="nic",unique = true)
    @Length(max=12,min = 10)
    private String nic ;

    private byte[] emp_photo;

  
    private LocalDateTime updated_datetime;
    
    @NotNull
    private LocalDateTime added_datetime; 

    private LocalDateTime deleted_datetime;
    
    @NotNull
    private Integer added_user_id; 

    private Integer updated_user_id;

    private Integer deleted_user_id;

    @ManyToOne//Relationship type with Employee table to emp_status table
    @JoinColumn(name = "emp_status_id",referencedColumnName = "id")
    private EmployeeStatus emp_status_id;  //type should be "EmployeeStatus" since its a foreign key type should from EmployeeStatus 

    @ManyToOne//Relationship type with Employee table to Designation table
    @JoinColumn(name ="designation_id",referencedColumnName = "id" )
    private Designation designaton_id; //type should be "Designation" since its a foreign key type should from Designation 

   
      //for the constructor for getting only the needed data from db (grn form)-->apit awashya column withrk arn item select list ekat aywann aform wala
    public  Employee(String fullname,String nic,String mobile_no,String email,EmployeeStatus emp_status_id,Designation designaton_id){
    this.fullname=fullname;
    this.nic=nic;
    this.mobile_no=mobile_no;
    this.email=email;
    this.emp_status_id=emp_status_id;
    this.designaton_id=designaton_id;
 
  }
}
