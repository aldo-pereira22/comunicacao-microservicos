package br.com.products.api.modules.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockDto implements Serializable {

    private String salesId;
    private List<ProductQuantityDto> products;
    private String transactinid;

}
