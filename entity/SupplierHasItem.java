package lk.upalisupermarket.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity// convert into entity for mapping
@Table(name = "supplier_has_item")// map table with this class


@Data//Generate setters and getters,toString() for accessing private properties
@AllArgsConstructor//generate allarguement constructor
@NoArgsConstructor//generate default or noArgument constructor
public class SupplierHasItem {

    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne // Relationship type with supplierhasitem table to supplier table
    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    private Supplier supplier_id;// type should be "Supplier" since its a foreign key type should from Supplier

    @ManyToOne // Relationship type with supplierhasitem table to item table
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item_id;// type should be "Item" since its a foreign key type should from Item
}
