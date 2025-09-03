package lk.upalisupermarket.entity;

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
@Table(name = "privilege")// map table with this class


@Data//Generate setters and getters,toString() for accessing private properties
@AllArgsConstructor//generate allarguement constructor
@NoArgsConstructor//generate default or noArgument constructor
public class Privilege {

    @Id//primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private Boolean delete_privilege;

    @NotNull
    private Boolean insert_privilege;

    @NotNull
    private Boolean select_privilege;

    @NotNull
    private Boolean update_privilege;

    @ManyToOne//Relationship type with privilege table to module table
    @JoinColumn(name = "module_id",referencedColumnName = "id")
    private Module module_id;//type should be "Module" since its a foreign key type should from Module 


   @ManyToOne//Relationship type with privilege table to role table
   @JoinColumn(name="role_id",referencedColumnName ="id")
    private Role role_id;//type should be "Role" since its a foreign key type should from Role 

}
