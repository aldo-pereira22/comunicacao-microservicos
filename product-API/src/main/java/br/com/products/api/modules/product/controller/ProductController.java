package br.com.products.api.modules.product.controller;

import br.com.products.api.modules.category.dto.CategoryRequest;
import br.com.products.api.modules.category.dto.CategoryResponse;
import br.com.products.api.modules.category.service.CategoryService;
import br.com.products.api.modules.product.dto.ProductRequest;
import br.com.products.api.modules.product.dto.ProductResponse;
import br.com.products.api.modules.product.model.Product;
import br.com.products.api.modules.product.service.ProductService;
import br.com.products.api.modules.supplier.dto.SupplierResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ProductResponse save(@RequestBody ProductRequest request){
        return productService.save(request);
    }
    @GetMapping

    public List<ProductResponse> findAll(){
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ProductResponse findById(@PathVariable Integer id){
        return productService.findByIdResponse(id);
    }

    @GetMapping("name/{name}")
    public List<ProductResponse> findByName (@PathVariable String name){
        return productService.findByname(name);
    }

    @GetMapping("supplierId/{id}")
    public List<ProductResponse> findBySupplierId (@PathVariable Integer id){
        return productService.findBySupplierId(id);
    }

    @GetMapping("/{id}")
    public List<ProductResponse> findByNameCategory (@PathVariable Integer id){
        return productService.findByCategoryId(id);
    }
}
