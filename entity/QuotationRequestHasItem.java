package lk.upalisupermarket.entity;




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
@Table(name = "quotation_request_has_item") // map table with this class


@Data//Generate setters and getters,toString() for accessing private properties
@AllArgsConstructor//generate allarguement constructor
@NoArgsConstructor//generate default or noArgument constructor
public class QuotationRequestHasItem {

    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto increment
    private Integer id;

    // @Column(name = "")  when db column name is differnt from tghe name we use if its the same no need to define it
    
    @NotNull
    private Integer qty;

    @ManyToOne // Relationship type with QuotationRequestHasItem table to QuotationRequest table
    @JoinColumn(name = "quotation_request_id", referencedColumnName = "id")
    @JsonIgnore//block reading property
    private QuotationRequest quotation_request_id;// type should be "QuotationRequest" since its a foreign key type should from QuotationRequest

    @ManyToOne // Relationship type with QuotationRequestHasItem table to item table
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item_id;// type should be "Item" since its a foreign key type should from Item


}
