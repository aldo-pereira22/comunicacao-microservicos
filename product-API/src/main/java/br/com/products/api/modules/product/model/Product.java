package br.com.products.api.modules.product.model;

import br.com.products.api.modules.category.model.Category;
import br.com.products.api.modules.product.dto.ProductRequest;
import br.com.products.api.modules.supplier.model.Supplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
@Builder
@Table(name = "PRODUCT")
@NoArgsConstructor
@AllArgsConstructor
public class Product  {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "FK_SUPPLIER", nullable = false)
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "FK_CATEGORY", nullable = false)
    private Category category;


    @Column(name = "QUANTITY_AVAILABLE", nullable = false)
    private Integer quantityAvailable;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
    }

    public static Product of(ProductRequest request,
                             Supplier supplier,
                             Category category){
        return Product
                .builder()
                .name(request.getName())
                .quantityAvailable(request.getQuantityAvailable())
                .supplier(supplier)
                .category(category)
                .build();
    }

    public void updateStock(Integer quantity){
        this.quantityAvailable = this.quantityAvailable - quantity;
    }
}
