package lk.upalisupermarket.entity;


import java.math.BigDecimal;
import java.time.LocalDate;

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
@Table(name = "item_inventory") // map table with this class


@Data//Generate setters and getters,toString() for accessing private properties
@AllArgsConstructor//generate allarguement constructor
@NoArgsConstructor//generate default or noArgument constructor
public class Inventory {


    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto increment
    private Integer id;

    // @Column(name = "")  when db column name is differnt from tghe name we use
    @NotNull
    private String batch_no ;

    @NotNull
    private BigDecimal total_qty ;
   
    @NotNull
    private BigDecimal sales_price ;

    
    @NotNull
    private BigDecimal available_qty ;

      @NotNull
    private LocalDate exp_date ;



       @NotNull
    private LocalDate manufacture_date ;

    @ManyToOne // Relationship type with grn_has_item table to item table
    @JoinColumn(name = "item_id", referencedColumnName = "id")
     private Item item_id;
  



}
