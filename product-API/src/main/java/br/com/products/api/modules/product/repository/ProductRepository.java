package br.com.products.api.modules.product.repository;

import br.com.products.api.modules.category.model.Category;
import br.com.products.api.modules.product.model.Product;
import br.com.products.api.modules.supplier.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByNameIgnoreCaseContaining(String name);
    List<Product> findByCategoryId(Integer id);
    List<Product> findBySupplierId(Integer id);
    Boolean existsBySupplierId(Integer id);
    Boolean existsByCategoryId(Integer id);
}
