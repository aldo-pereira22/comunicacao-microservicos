package br.com.products.api.modules.category.dto;

import br.com.products.api.modules.category.model.Category;
import org.springframework.beans.BeanUtils;

public class CategoryResponse {
    private Integer id;
    private String description;

//    public static CategoryResponse of(CategoryRequest request){
//        var response = new CategoryResponse();
//        BeanUtils.copyProperties(request, response);
//        return response;
//    }

    public static CategoryResponse of(Category category) {
        var response = new CategoryResponse();
        BeanUtils.copyProperties(category,response);
        return response;
    }
}
