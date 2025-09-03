package lk.upalisupermarket.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // convert into entity for mapping
@Table(name = "item_brand_has_item_category") // map table with this class


@Data//Generate setters and getters,toString() for accessing private properties
@AllArgsConstructor//generate allarguement constructor
@NoArgsConstructor//generate default or noArgument constructor
public class BrandHasCategory {

   

    @Id // Primary key
    @ManyToOne//Relationship type with Brand table to Itemcategory table
    @JoinColumn(name = "item_brand_id",referencedColumnName = "id")
    private Category item_brand_id;

    @Id // Primary key
    @ManyToOne//Relationship type with Brand table to Itemcategory table
    @JoinColumn(name = "item_category_id",referencedColumnName = "id")
    private Category item_category_id;

  
}
