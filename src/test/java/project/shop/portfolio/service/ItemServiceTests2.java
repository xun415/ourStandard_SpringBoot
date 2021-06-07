package project.shop.portfolio.service;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import project.shop.portfolio.domain.Category;
import project.shop.portfolio.domain.item.Item;
import project.shop.portfolio.dto.CategoryDTO;
import project.shop.portfolio.dto.ItemManageDTO;
import project.shop.portfolio.dto.PageRequestDTO;
import project.shop.portfolio.dto.PageResultDTO;

import java.util.List;

@SpringBootTest
//@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@Transactional
public class ItemServiceTests2 {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ItemService itemService;
    @Test
    public void testList() throws Exception {
        //given
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();
        PageResultDTO<ItemManageDTO, Item> list = itemService.getList(pageRequestDTO);
        System.out.println("list.isNext() = " + list.isNext());
        System.out.println("list.isPrev() = " + list.isPrev());
        System.out.println("list.getTotalPage() = " + list.getTotalPage());

        for (ItemManageDTO itemManageDTO : list.getDtoList()) {
            System.out.println("itemManageDTO = " + itemManageDTO);
        }
        //when

        //then

    }

    @Test
    public void testConditionList() throws Exception {
        //given
        //카테고리 검색
//        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).category("바지").build();
        //상품명 검색
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).category("반팔").type("n").keyword("크루").build();
        //브랜드검색
        //PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).type("b").keyword("브랜드2").build();
        
        PageResultDTO<ItemManageDTO, Item> list = itemService.getList(pageRequestDTO);
        System.out.println("list.isNext() = " + list.isNext());
        System.out.println("list.isPrev() = " + list.isPrev());
        System.out.println("list.getTotalPage() = " + list.getTotalPage());

        for (ItemManageDTO itemManageDTO : list.getDtoList()) {
            System.out.println("itemManageDTO = " + itemManageDTO);
        }
        //when

        //then

    }

    @Test
    public void getCategoryList() throws Exception {
//        //given
//        List<Category> parentCategories = categoryService.getParentCategories();
//
//        for (Category parentCategory : parentCategories) {
//            parentCategory.getName();
//            System.out.println("parentCategory.getName() = " + parentCategory.getName());
//            List<Category> childList = parentCategory.getChild();
//            for (Category category : childList) {
//                System.out.println("category.getName() = " + category.getName());
//            }
//            System.out.println("--------------------------------------------");
//        }
//        List<Category> categoryList = categoryService.getCategoryList();
//        for (Category category : categoryList) {
//            System.out.println("category.getName() = " + category.getName());
//        }
        List<CategoryDTO> categoryDTOList = categoryService.getCategoryDTOList();
        System.out.println("ItemServiceTests2.getCategoryList");
        for (CategoryDTO categoryDTO : categoryDTOList) {
            System.out.println("categoryDTO.getCategoryName() = " + categoryDTO.getCategoryName());
            for (String childCategoryName : categoryDTO.getChildCategoryNames()) {
                System.out.println("childCategoryName = " + childCategoryName);
            }
        }


        //when

        //then

    }
}
