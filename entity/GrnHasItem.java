package lk.upalisupermarket.entity;



import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "grn_has_item") // map table with this class


@Data//Generate setters and getters,toString() for accessing private properties
@AllArgsConstructor//generate allarguement constructor
@NoArgsConstructor//generate default or noArgument constructor
public class GrnHasItem {


    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto increment
    private Integer id;

    // @Column(name = "")  when db column name is differnt from tghe name we use if its the same no need to define it
    @NotNull
    private BigDecimal purchase_price;
    @NotNull
    private BigDecimal selling_price;

  
     private String batch_no;

     @NotNull
     private LocalDate mfd_date;

     @NotNull
     private LocalDate exp_date;

     @NotNull
     private BigDecimal tot_qty;

     @NotNull
     private BigDecimal free_qty;

     @NotNull
     private BigDecimal qty;

    @NotNull
    private BigDecimal line_price;

     @ManyToOne // Relationship type with grn_has_item table to grn table
    @JoinColumn(name = "grn_id", referencedColumnName = "id")
    @JsonIgnore//block reading property
    private Grn grn_id;// type should be "Grn" since its a foreign key type should from Grn

    @ManyToOne // Relationship type with grn_has_item table to item table
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item_id;// type should be "Item" since its a foreign key type should from Item


}
