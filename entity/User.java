package lk.upalisupermarket.entity;

import java.time.LocalDateTime;
import java.util.Set;


import jakarta.persistence.CascadeType;
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

@Entity// convert into entity for mapping
@Table(name = "user")// map table with this class

@Data//Generate setters and getters,toString() for accessing private properties
@AllArgsConstructor//generate allarguement constructor
@NoArgsConstructor//generate default or noArgument constructor
public class User {

    @Id// Primary key
    @GeneratedValue(strategy=GenerationType.IDENTITY)//Auto increment
    private Integer id; 

    // @Column(name = "")  when db column name is differnt from tghe name we use if its the same no need to define it

    @NotNull
    private String username ;

    @NotNull
    private String password ; 

    @NotNull
    private String email ;

    @NotNull
    private Boolean status ;

    @NotNull
    private LocalDateTime added_datetime ;

    private LocalDateTime updated_datetime ;

    private LocalDateTime delete_datetime ;

    private String note;

  
    private byte[] user_photo ;//if mediumBlob in db then this should be byte array

    //Relationship type with User table to Employee table(many to one)
    @ManyToOne(optional=true)//required na kiyn eka
    @JoinColumn(name = "employee_id",referencedColumnName = "id")
    private Employee employee_id ;//type should be "Employee" since its a foreign key type should from Employee id is the reference column table

    //(User and role tables)
    @ManyToMany(cascade = CascadeType.MERGE) //Role eke daata access krnna crud operation ktnna nm cascade type all krnn aone
    //many to many nisa join Table
    @JoinTable(name = "user_has_role",joinColumns = @JoinColumn(name="user_id"),inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<Role> roles;
}


