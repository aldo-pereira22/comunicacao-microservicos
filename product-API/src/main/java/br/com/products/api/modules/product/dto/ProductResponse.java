package br.com.products.api.modules.product.dto;

import br.com.products.api.modules.category.dto.CategoryResponse;
import br.com.products.api.modules.category.model.Category;
import br.com.products.api.modules.product.model.Product;
import br.com.products.api.modules.supplier.dto.SupplierResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Integer id;
    private String name;
    private Integer quantityAvailable;
    @JsonProperty("createdAt")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    private SupplierResponse supplierResponse;
    private CategoryResponse categoryResponse;


//    public static CategoryResponse of(CategoryRequest request){
//        var response = new CategoryResponse();
//        BeanUtils.copyProperties(request, response);
//        return response;
//    }

    public static ProductResponse of(Product product) {
        return ProductResponse
                .builder()
                .id(product.getId())
                .name(product.getName())
                .quantityAvailable(product.getQuantityAvailable())
                .createdAt(product.getCreatedAt())
                .supplierResponse(SupplierResponse.of(product.getSupplier()))
                .categoryResponse(CategoryResponse.of(product.getCategory()))
                .build();
    }
}
