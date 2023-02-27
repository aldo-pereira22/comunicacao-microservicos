package br.com.products.api.modules.produto.repository;

import br.com.products.api.modules.produto.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
