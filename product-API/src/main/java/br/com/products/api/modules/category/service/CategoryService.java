package br.com.products.api.modules.category.service;

import br.com.products.api.config.exception.ValidationException;
import br.com.products.api.modules.category.dto.CategoryRequest;
import br.com.products.api.modules.category.dto.CategoryResponse;
import br.com.products.api.modules.category.model.Category;
import br.com.products.api.modules.category.repository.CategoryRepository;
import br.com.products.api.modules.supplier.model.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category findById(Integer id){
        return categoryRepository
                .findById(id)
                .orElseThrow( ()-> new ValidationException("There's no supplier for the given ID"));
    }
//    public CategoryResponse save(CategoryRequest request){
//        validateCategoryNameInformed(request);
//        return CategoryResponse.of(request);
//    }

    public CategoryResponse save(CategoryRequest request) {
        validateCategoryNameInformed(request);
        var category = categoryRepository.save(Category.of(request));
        return CategoryResponse.of(category);
    }

    private void validateCategoryNameInformed(CategoryRequest request){
        if( isEmpty (request.getDescription())){
            throw new HttpMessageNotReadableException("The category description was not informed");
        }
    }
}
