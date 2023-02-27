package br.com.products.api.modules.produto.service;

import br.com.products.api.config.exception.ValidationException;
import br.com.products.api.modules.produto.dto.CategoryRequest;
import br.com.products.api.modules.produto.dto.CategoryResponse;
import br.com.products.api.modules.produto.model.Category;
import br.com.products.api.modules.produto.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

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
