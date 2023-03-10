package br.com.products.api.modules.supplier.service;

import br.com.products.api.config.exception.SuccessResponse;
import br.com.products.api.config.exception.ValidationException;
import br.com.products.api.modules.category.dto.CategoryRequest;
import br.com.products.api.modules.category.dto.CategoryResponse;
import br.com.products.api.modules.category.model.Category;
import br.com.products.api.modules.product.service.ProductService;
import br.com.products.api.modules.supplier.dto.SupplierRequest;
import br.com.products.api.modules.supplier.dto.SupplierResponse;
import br.com.products.api.modules.supplier.model.Supplier;
import br.com.products.api.modules.supplier.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ProductService productService;

    public List<SupplierResponse> findByname(String name) {
        if (isEmpty(name)) {
            throw new ValidationException("The Supplier name must be informed");
        }
        return supplierRepository
                .findByNameIgnoreCaseContaining(name)
                .stream()
                .map(SupplierResponse::of)
                .collect(Collectors.toList());

    }

    public SupplierResponse findByIdResponse(Integer id) {
        validateInformedId(id);
        return SupplierResponse.of(findById(id));
    }

    public Supplier findById(Integer id) {
        return supplierRepository
                .findById(id)
                .orElseThrow(() -> new ValidationException("There's no supplier for the given ID"));
    }


    public SupplierResponse save(SupplierRequest request) {
        validateSupplierNameInformed(request);
        var supplier = supplierRepository.save(Supplier.of(request));
        return SupplierResponse.of(supplier);
    }

    public SupplierResponse update(SupplierRequest request, Integer id) {
        validateSupplierNameInformed(request);
        var supplier = Supplier.of(request);
        supplier.setId(id);
        supplierRepository.save(supplier);
        return SupplierResponse.of(supplier);
    }

    private void validateSupplierNameInformed(SupplierRequest request) {
        if (isEmpty(request.getName())) {
            throw new HttpMessageNotReadableException("The supplier description was not informed");
        }
    }

    public List<SupplierResponse> findAll() {
        return supplierRepository
                .findAll()
                .stream()
                .map(supplier -> SupplierResponse.of(supplier))
                .collect(Collectors.toList());
    }

    public SuccessResponse delete(Integer id) {
        validateInformedId(id);
        if(productService.existsBySupplierId(id)){
            throw new ValidationException("You cannot delete this supplier because it's already defined by a product.");
        }
        supplierRepository.deleteById(id);
        return SuccessResponse.create("The supplier was deleted.");

    }

    private void validateInformedId(Integer id) {
        if (isEmpty(id)) {
            throw new ValidationException("The Supplier ID must informed.");
        }
    }
}
