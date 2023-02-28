package br.com.products.api.modules.product.service;

import br.com.products.api.config.exception.ValidationException;
import br.com.products.api.modules.category.dto.CategoryRequest;
import br.com.products.api.modules.category.dto.CategoryResponse;
import br.com.products.api.modules.category.model.Category;
import br.com.products.api.modules.category.service.CategoryService;
import br.com.products.api.modules.product.dto.ProductRequest;
import br.com.products.api.modules.product.dto.ProductResponse;
import br.com.products.api.modules.product.model.Product;
import br.com.products.api.modules.product.repository.ProductRepository;
import br.com.products.api.modules.supplier.model.Supplier;
import br.com.products.api.modules.supplier.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class ProductService {
    private static final Integer ZERO = 0;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private CategoryService categoryService;


    @Autowired

    public ProductResponse save(ProductRequest request){
        validateProductNotInformed(request);
        validateCategoryAndSupplierInformed(request);
        var category = categoryService.findById(request.getCategoryId());
        var supplier = supplierService.findById(request.getSupplierId());
        var product = productRepository.save(Product.of(request,supplier, category));
        return ProductResponse.of(product);

    }


    private void validateProductNotInformed(ProductRequest request){
        if(isEmpty(request.getName())){
            throw new ValidationException("The category name was not informed!");
        }
        if(isEmpty(request.getQuantityAvailable())){
            throw new ValidationException("The product quantity was not informed");
        }
        if(request.getQuantityAvailable() <= 0){
            throw new ValidationException("The quantity should be more than 0");
        }
    }


    private void validateCategoryAndSupplierInformed(ProductRequest request){
        if(isEmpty(request.getCategoryId())){
            throw new ValidationException("The category name was not informed!");
        }
        if(isEmpty(request.getSupplierId())){
            throw new ValidationException("The category name was not informed!");
        }
    }
}
