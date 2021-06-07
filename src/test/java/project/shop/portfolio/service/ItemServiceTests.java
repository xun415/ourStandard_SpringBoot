//package project.shop.portfolio.service;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//import project.shop.portfolio.domain.Category;
//import project.shop.portfolio.domain.item.Item;
//import project.shop.portfolio.domain.item.ItemDetail;
//import project.shop.portfolio.domain.item.ItemImage;
//import project.shop.portfolio.repository.BrandRepository;
//import project.shop.portfolio.repository.CategoryRepository;
//import project.shop.portfolio.repository.ItemDetailRepository;
//import project.shop.portfolio.repository.ItemRepository;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//@SpringBootTest
//@RunWith(SpringRunner.class)
//@Transactional
//public class ItemServiceTests {
//    @Autowired
//    private ItemService itemService;
//    @Autowired
//    private BrandRepository brandRepository;
//    @Autowired
//    private CategoryRepository categoryRepository;
//    @Autowired
//    private ItemRepository itemRepository;
//    @Autowired
//    private ItemDetailRepository itemDetailRepository;
//    @Test
//    public void 상품등록() throws Exception {
////        Category category = new Category();
////        category.setName("상의");
////        category.setParent(null);
////        category.setChild(null);
////        Long parentCategoryId = categoryRepository.save(category).getId();
////
////
////        Category childCategory = new Category();
////        childCategory.setName("티셔츠");
////        Optional<Category> byId = categoryRepository.findById(parentCategoryId);
////        if (byId.isPresent()){
////            Category parent = byId.get();
////            parent.addChildCategory(childCategory);
////            childCategory.setParent(parent);
////        }
////        childCategory.addChildCategory(childCategory);
////        Category childCategoryForItem = categoryRepository.save(childCategory);
////        List<Category> categoryList = new ArrayList<>();
////        categoryList.add(childCategory);
//        List<Category> categoryList = new ArrayList<>();
//        Optional<Category> categoryByName = categoryRepository.findCategoryByName("티셔츠");
//        if (categoryByName.isPresent()){
//            Category category = categoryByName.get();
//            categoryList.add(category);
//
//        }
//
//        Item item = Item.builder()
//                .name("어커버 티셔츠")
//                .price(10000)
//                .brand(null)
//                .categories(categoryList)
//                .build();
//
////        childCategoryForItem.getItems().add(item);
//
//
//        ItemImage itemImage = ItemImage.builder()
//                .item(item)
//                .imgName("어커버티셔츠 사진")
//                .path("경로")
//                .uuid(UUID.randomUUID().toString())
//                .build();
//        itemService.registerItem(item,categoryList.get(0),itemImage);
//
//        //given
//
//        //when
//
//        //then
//
//    }
//
//    @Test
//    public void insetItemOption() throws Exception {
//        //given
//        Optional<Item> byId = itemRepository.findById(28L);
//        Item item = byId.get();
//
//        ItemDetail itemDetail = ItemDetail.builder()
//                .stockQuantity(20)
//                .size("L")
//                .parentItem(item)
//                .color("red")
//                .build();
//        Long itemOptionId = itemService.addItemOption(itemDetail);
//
//        Optional<ItemDetail> result = itemDetailRepository.findById(itemOptionId);
//
//        ItemDetail itemDetail1 = result.get();
//        System.out.println("itemDetail1.getSize() = " + itemDetail1.getSize());
//        System.out.println("itemDetail1.getColor() = " + itemDetail1.getColor());
//        System.out.println("itemDetail1.getStockQuantity() = " + itemDetail1.getStockQuantity());
//
//
//        //when
//
//        //then
//
//    }
//
//    @Test
//    public void getCategoryList() throws Exception {
//        //given
//
//        List<Category> all = categoryRepository.findAll();
//        for (Category category : all) {
//            System.out.println("category = " + category);
//        }
//
//        //when
//
//        //then
//
//    }
//}
