package br.com.products.api.modules.supplier.dto;

import br.com.products.api.modules.category.model.Category;
import br.com.products.api.modules.supplier.model.Supplier;
import org.springframework.beans.BeanUtils;

public class SupplierResponse {
    private Integer id;
    private String name;


    public static SupplierResponse of(Supplier supplier) {
        var response = new SupplierResponse();
        BeanUtils.copyProperties(supplier,response);
        return response;
    }
}
