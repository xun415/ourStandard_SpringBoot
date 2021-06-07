package project.shop.portfolio.service;

import com.fasterxml.jackson.databind.util.ArrayBuilders;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.shop.portfolio.domain.Category;
import project.shop.portfolio.domain.Customer;
import project.shop.portfolio.domain.Review;
import project.shop.portfolio.domain.item.Item;
import project.shop.portfolio.domain.item.ItemDetail;
import project.shop.portfolio.domain.item.ItemImage;
import project.shop.portfolio.domain.item.QItem;
import project.shop.portfolio.dto.*;
import project.shop.portfolio.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemImageRepository itemImageRepository;
    private final ItemDetailRepository itemDetailRepository;
    private final ReviewRepository reviewRepository;
    private final CommentsRepository commentsRepository;
    private final CategoryRepository categoryRepository;

    private BooleanBuilder getSearch(PageRequestDTO requestDTO){
        String type = requestDTO.getType();
        String keyword = requestDTO.getKeyword();
        String category = requestDTO.getCategory();

        System.out.println("검색조건 = " + requestDTO.getType()+", "+requestDTO.getKeyword()+", "+requestDTO.getCategory());
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QItem qItem = QItem.item;

        BooleanExpression expression = qItem.id.gt(0L);//id가 0보다 큼

        booleanBuilder.and(expression);

        if( (type ==null || type.trim().length()==0) & (category ==null ||category.length()==0)){
            System.out.println("검색조건 없어서 반환");
            return booleanBuilder;
            
        }
        //검색 조건 작성
        BooleanBuilder conditionBuilder = new BooleanBuilder();
        if(type !=null && type.trim().length()>0){
            System.out.println("type와 키워드로 검색");
            if(type.contains("n")){//상품명
                System.out.println("상품명 검색");
                conditionBuilder.or(qItem.name.contains(keyword));
            }
            if(type.contains("b")){//브랜드
                System.out.println("브랜드 검색");
                conditionBuilder.or(qItem.brand.name.contains(keyword));
            }
        }
        if (category!=null && category.length()>0){
            System.out.println("카테고리 검색  ");
            Optional<Category> categoryByName = categoryRepository.findCategoryByName(category);
            if (categoryByName.isPresent()){
                Category category1 = categoryByName.get();
                conditionBuilder.or(qItem.categories.contains(category1));
            }
        }
        //모든 조건 포함
        booleanBuilder.and(conditionBuilder);

        return booleanBuilder;
    }


    public ItemManageDTO entityToDto(Item item){
        ItemManageDTO dto = ItemManageDTO.builder()
                .id(String.valueOf(item.getId()))
                .name(item.getName())
                .category1(item.getCategories().get(0).getParent().getName())
                .category2(item.getCategories().get(0).getName())
                .price(item.getPrice())
                .build();
        //썸네일 이미지 넣어주기
        String thumbnailURL = item.getItemImageList().get(0).getThumbnailFileName();
        dto.setS_fileName(thumbnailURL);
        return dto;
    }
    public PageResultDTO<ItemManageDTO,Item> getList(PageRequestDTO requestDTO){
        Pageable pageable = requestDTO.getPageable(Sort.by("id").descending()); //Sort.by("item_id").descending()

        BooleanBuilder booleanBuilder = getSearch(requestDTO);//검색조건 처리


//        Page<Item> result = itemRepository.findAll(booleanBuilder,pageable);//querydsl사용

        Page<Item> result = itemRepository.findAll(booleanBuilder, pageable);

        Function<Item,ItemManageDTO> fn =(entity ->
            entityToDto(entity));
        return new PageResultDTO<>(result,fn);

    }


    public Long registerItem(Item item, Category category, ItemImage... itemImage){
        Optional<Category> categoryByName = categoryRepository.findCategoryByName(category.getName());
        if (categoryByName.isPresent()){
            Category category1 = categoryByName.get();
            category1.getItems().add(item);
        }

        Item savedItem = itemRepository.save(item);
        //이미지 추가
        for (ItemImage image : itemImage) {
            ItemImage savedItemImage = itemImageRepository.save(image);

        }
        return savedItem.getId();
    }
    public void modifyItem(Item item, Category category, ItemImage... itemImage){
        Optional<Item> byId = itemRepository.findById(item.getId());
        Item foundItem = byId.get();
        //name, category, price
        List<Category> categories = foundItem.getCategories();
        categories.remove(0);
        categories.add(category);
        foundItem.modifyItem(item.getName(),item.getPrice(),category);
    }
    public void deleteItem(Item item){
        Optional<Item> byId = itemRepository.findById(item.getId());
        Item foundItem = byId.get();
        itemImageRepository.deleteByItemId(item.getId());

        Optional<Category> byId1 = categoryRepository.findById(foundItem.getCategories().get(0).getId());
        Category category = byId1.get();
        category.getItems().remove(foundItem);

        itemImageRepository.deleteByItemId(item.getId());

        itemRepository.delete(foundItem);

    }
    public Item getItemById(Long id){
        Optional<Item> byId = itemRepository.findById(id);
        return byId.get();

    }
    public Optional<List<ItemDetail>> getItemOptionsByItemId(Long itemId){
        return itemDetailRepository.findItemDetailByItemId(itemId);
    }

    public Long addItemOption(ItemDetail itemDetail){
        ItemDetail saveItemDetail = itemDetailRepository.save(itemDetail);
        return  saveItemDetail.getId();
    }
    public void modifyItemOption(Long id,String size,String color,int quantity){
        Optional<ItemDetail> result = itemDetailRepository.findById(id);
        if (result.isPresent()){
            ItemDetail itemDetail = result.get();
            itemDetail.modifyOption(size,color,quantity);
        }
    }
    public void addItemOptionQuantity(Long id, int quantity){
        Optional<ItemDetail> result = itemDetailRepository.findById(id);
        if (result.isPresent()){
            ItemDetail itemDetail = result.get();
            itemDetail.addStock(quantity);
        }
    }
    public void deleteOption(Long id){
        itemDetailRepository.deleteById(id);
    }


    public void registerItemImage(ItemImage itemImage) {
        itemImageRepository.save(itemImage);
    }

    public StoreItemDTO getStoreItemDTOList(Long itemId) {
        //StoreItemDTO : parentId optionId price quantity size LongImg Img
        List<StoreItemDTO> storeItemDTOList = new ArrayList<>();
        //아이템 조회
        Optional<Item> byId = itemRepository.findById(itemId);
        Item item = byId.get();

        //옵션 조회
        Optional<List<ItemDetail>> itemDetailByItemId = itemDetailRepository.findItemDetailByItemId(itemId);
        List<ItemDetail> itemDetails = itemDetailByItemId.get();

        StoreItemDTO storeItemDTO = StoreItemDTO.builder()
                .parentId(String.valueOf(item.getId()))
                .price(item.getPrice())
                .name(item.getName())
                .Img(item.getItemImageList().get(0).getFileNameWithoutUploadPath())
                .LongImg(item.getItemImageList().get(1).getFileNameWithoutUploadPath())
                .build();



        //옵션 추가
        for (ItemDetail itemDetail : itemDetails) {
            ItemOptionDTO itemOptionDTO = ItemOptionDTO.builder()
                    .id(String.valueOf(itemDetail.getId()))
                    .parentId(storeItemDTO.getParentId())
                    .size(itemDetail.getSize())
                    .color(itemDetail.getColor())
                    .stockQuantity(itemDetail.getStockQuantity())
                    .build();
            //추가
            storeItemDTO.getOptionDTOList().add(itemOptionDTO);
        }

        storeItemDTOList.add(storeItemDTO);
        return storeItemDTO;
    }

    public ItemDetail getItemOptionByItemDetailId(Long id) {
          return itemDetailRepository.findById(id).get();
    }

    public List<Review> getReviewListByItemId(Long itemId) {
        Optional<List<Review>> allByItemId = reviewRepository.findAllByItemId(itemId);
        if(allByItemId.isPresent()){
            List<Review> reviewList = allByItemId.get();
            return  reviewList;
        }
        return null;
    }
}
