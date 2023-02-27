package br.com.products.api.modules.produto.model;


import br.com.products.api.modules.produto.dto.CategoryRequest;
import br.com.products.api.modules.produto.dto.CategoryResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;

@Data
@Entity
@Table(name = "CATEGORY")
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

//    public static Category of(CategoryRequest request){
//        var category = new Category();
//        BeanUtils.copyProperties(request, category);
//        return category;
//    }

    public static Category of(CategoryRequest request) {
        var category = new Category();
        BeanUtils.copyProperties(request, category);
        return category;
    }
}
