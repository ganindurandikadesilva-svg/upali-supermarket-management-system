package lk.upalisupermarket.entity;



import java.math.BigDecimal;

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
@Table(name = "invoice_has_item_inventory") // map table with this class


@Data//Generate setters and getters,toString() for accessing private properties
@AllArgsConstructor//generate allarguement constructor
@NoArgsConstructor//generate default or noArgument constructor
public class InvoiceHasInventory {

    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto increment
    private Integer id;

    // @Column(name = "")  when db column name is differnt from tghe name we use if its the same no need to define it
    @NotNull
    private BigDecimal unit_price;

    @NotNull
    private BigDecimal qty;

    @NotNull
    private BigDecimal line_price;

    @NotNull
    private BigDecimal discounted_price;

     @ManyToOne // Relationship type with invoice_has_item_inventory table to invoice table
    @JoinColumn(name = "invoice_id", referencedColumnName = "id")
    @JsonIgnore//block reading property
    private Invoice invoice_id;// type should be "Invoice" since its a foreign key type should from Invoice

    @ManyToOne // Relationship type with invoice_has_item_inventory table to item_inventory table
    @JoinColumn(name = "item_inventory_id", referencedColumnName = "id")
    private Inventory item_inventory_id;// type should be "Inventory" since its a foreign key type should from Inventory


}
