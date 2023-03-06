package br.com.products.api.modules.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductQuantityDto implements Serializable {
    private Integer productId;
    private Integer quantity;
}
