package project.shop.portfolio.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.shop.portfolio.domain.Category;
import project.shop.portfolio.dto.CategoryDTO;
import project.shop.portfolio.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> getCategoryList() {
        return categoryRepository.findAll();
    }

    public List<Category> getParentCategories() {
        List<Category> parentCategories  = categoryRepository.findAllByParentIsNull();
        for (Category parentCategory : parentCategories) {
            List<Category> child = parentCategory.getChild();
            for (Category category : child) {
                category.getName();
            }
        }
        return parentCategories;
    }

    public List<CategoryDTO> getCategoryDTOList() {
        List<Category> parentCategories  = categoryRepository.findAllByParentIsNull();
        List<CategoryDTO> categoryDTOList = new ArrayList<>();

        for (Category parentCategory : parentCategories) {

            CategoryDTO parentDTO = CategoryDTO.builder().categoryName(parentCategory.getName()).build();

            List<Category> child = parentCategory.getChild();

            for (Category category : child) {
                parentDTO.getChildCategoryNames().add(category.getName());
            }
            categoryDTOList.add(parentDTO);
        }

         return categoryDTOList;
    }

    public Category getCategoryByName(String category2) {
        Optional<Category> categoryByName = categoryRepository.findCategoryByName(category2);
        if (categoryByName.isPresent()){
            return categoryByName.get();
        }
        else return null;
    }


}
