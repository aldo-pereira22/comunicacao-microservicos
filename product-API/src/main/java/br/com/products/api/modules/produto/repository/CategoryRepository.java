package br.com.products.api.modules.produto.repository;

import br.com.products.api.modules.produto.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
}
