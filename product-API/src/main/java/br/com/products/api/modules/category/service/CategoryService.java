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

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryResponse findByIdResponse(Integer id){
        return  CategoryResponse.of(findById(id));
    }
    public List<CategoryResponse> findByDescription(String description){
        if(isEmpty(description)){
            throw new ValidationException("The Category descripiton must informed ");
        }
        return categoryRepository
                .findByDescriptionIgnoreCaseContaining(description)
                .stream()
                .map(category -> CategoryResponse.of(category))
                .collect(Collectors.toList());
    }


        public Category findById(Integer id){
            return categoryRepository
                    .findById(id)
                    .orElseThrow(() -> new ValidationException("Thers's no category for the given ID."));
        }

    public List<CategoryResponse> findAll(){
        return categoryRepository
                .findAll()
                .stream()
                .map(category -> CategoryResponse.of(category))
                .collect(Collectors.toList());
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
