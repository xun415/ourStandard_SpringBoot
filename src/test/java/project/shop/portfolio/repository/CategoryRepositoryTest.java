//package project.shop.portfolio.repository;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//import project.shop.portfolio.domain.Category;
//import project.shop.portfolio.domain.item.Item;
//
//import java.util.Optional;
//
//@SpringBootTest
//@RunWith(SpringRunner.class)
//@Transactional
//public class CategoryRepositoryTest {
//    @Autowired
//    private CategoryRepository categoryRepository;
//    @Test
//    public void getCategoryItems() throws Exception {
//        //given
//
//        Optional<Category> findCategory = categoryRepository.findCategoryByName("니트");
//        if(findCategory.isPresent()){
//            Category category = findCategory.get();
//            for (Item categoryItem : category.getItems()) {
//                System.out.println("categoryItem = " + categoryItem.getName());
//                System.out.println("categoryItem.getBrand() = " + categoryItem.getBrand());
//            }
//        }
//        //when
//
//        //then
//
//    }
//}
