package br.com.products.api.modules.supplier.service;

import br.com.products.api.modules.category.dto.CategoryRequest;
import br.com.products.api.modules.category.dto.CategoryResponse;
import br.com.products.api.modules.category.model.Category;
import br.com.products.api.modules.supplier.dto.SupplierRequest;
import br.com.products.api.modules.supplier.dto.SupplierResponse;
import br.com.products.api.modules.supplier.model.Supplier;
import br.com.products.api.modules.supplier.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    public SupplierResponse save(SupplierRequest request) {
        validateSupplierNameInformed(request);
        var supplier = supplierRepository.save(Supplier.of(request));
        return SupplierResponse.of(supplier);
    }

    private void validateSupplierNameInformed(SupplierRequest request){
        if( isEmpty (request.getName())){
            throw new HttpMessageNotReadableException("The supplier description was not informed");
        }
    }
}
