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
@Table(name = "item_seasonal_discount") // map table with this class


@Data//Generate setters and getters,toString() for accessing private properties
@AllArgsConstructor//generate allarguement constructor
@NoArgsConstructor//generate default or noArgument constructor
public class ItemSeasonalDiscount {

    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto increment
    private Integer id;

    // @Column(name = "")  when db column name is differnt from tghe name we use
    @NotNull
    private LocalDate start_date;

    // @Column(name = "")  when db column name is differnt from tghe name we use
    @NotNull
    private LocalDate end_date;
 
    @NotNull
    private BigDecimal discount_rate;

    @NotNull
    private Integer minimum_qty;

    @ManyToOne//Relationship type with item seasonal discount table  to Item table
    @JoinColumn(name = "item_id",referencedColumnName = "id")
    private Item item_id;


     //for the constructor for getting only the needed data from db (invoice form ekt discount prce calculate krnna) -->apit awashya column withrk arn item select list ekat aywann aform wala
    public  ItemSeasonalDiscount(BigDecimal discount_rate){

    this.discount_rate=discount_rate;
  
  }
}
