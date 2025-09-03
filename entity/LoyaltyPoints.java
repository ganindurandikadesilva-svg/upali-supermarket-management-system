package lk.upalisupermarket.entity;

import java.math.BigDecimal;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity// convert into entity for mapping
@Table(name = "loyalty_table")// map table with this class


@Data//Generate setters and getters,toString() for accessing private properties
@AllArgsConstructor//generate allarguement constructor
@NoArgsConstructor//generate default or noArgument constructor
public class LoyaltyPoints {

    @Id//primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String loyalty_name;

    @NotNull
    private BigDecimal start_point;

    @NotNull
    private BigDecimal end_point;

    @NotNull
    private BigDecimal discount_rate;

    @NotNull
    private BigDecimal point_increase_rate;

    @NotNull
    private Boolean discount_availability;

    @NotNull
    private BigDecimal invoice_amount_limit;



//for the constructor for getting only the needed data from db (loyaltypoints for invoiceform)-->apit awashya column withrk arn item select list ekat aywann aform wala
   public  LoyaltyPoints(BigDecimal discount_rate,BigDecimal point_increase_rate){
    this.discount_rate=discount_rate;
    this.point_increase_rate=point_increase_rate;
   
  }

}
