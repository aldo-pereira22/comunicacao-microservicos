package br.com.products.api.modules.supplier.dto;

import br.com.products.api.modules.category.dto.CategoryResponse;
import br.com.products.api.modules.category.model.Category;
import br.com.products.api.modules.supplier.model.Supplier;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class SupplierResponse {
    private Integer id;
    private String name;
//    private SupplierResponse supplierResponse;
//    private CategoryResponse categoryResponse;


    public static SupplierResponse of(Supplier supplier) {
        var response = new SupplierResponse();
        BeanUtils.copyProperties(supplier,response);
        return response;
    }
}
