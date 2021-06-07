package project.shop.portfolio.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import project.shop.portfolio.domain.Brand;
import project.shop.portfolio.domain.Category;
import project.shop.portfolio.domain.Customer;
import project.shop.portfolio.domain.Review;
import project.shop.portfolio.domain.item.Item;
import project.shop.portfolio.domain.item.ItemDetail;
import project.shop.portfolio.domain.item.ItemImage;
import project.shop.portfolio.dto.*;
import project.shop.portfolio.security.dto.CustomerAuthDTO;
import project.shop.portfolio.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@Log4j2
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {
    

    @Value("${project.shop.upload.path}")//application.yml의 변수.파일이 저장될 루트 파일경로
    private String uploadPath;
    private final ItemService itemService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final CustomerService customerService;
    private final ReviewService reviewService;



    //아이템 삭제 요청 옵션있을시 불가
    @PostMapping("/{itemId}/delete")
    @PreAuthorize("hasRole('ROLE_BrandManager')")
    public String itemDelete(@PathVariable("itemId")String id){
        Long itemId = Long.valueOf(id);
        Item itemById = itemService.getItemById(itemId);

        itemService.deleteItem(itemById); //카테고리삭제, 이미지 삭제 후 삭제

        return  "redirect:/item/items";
    }



    //아이템 수정 요청 url: /item/{id}/modify (id=${dto.id})
    @GetMapping("/{itemId}/modify")
    @PreAuthorize("hasRole('ROLE_BrandManager')")
    public String itemModifyForm(@PathVariable("itemId")String id,Model model){
        //상품명, 카테고리1, 카테고리2, 썸네일, 롱이미지, 가격
        Long itemId = Long.valueOf(id);
        Item item = itemService.getItemById(itemId);

        List<Category> categories = item.getCategories();
        Category category = categories.get(0);
        Category parent = category.getParent();

        ItemManageDTO itemManageDTO = ItemManageDTO.builder()
                .id(id)
                .category1(parent.getName())
                .category2(category.getName())
                .price(item.getPrice())
                .name(item.getName())
                .build();
        model.addAttribute("itemDTO",itemManageDTO);
        //카테고리 변경을 위해 카테고리도 넣어점
        List<CategoryDTO> categoryDTOList = categoryService.getCategoryDTOList();
        model.addAttribute("categoryList",categoryDTOList);

        return "/seller/productModify";
    }


    //아이템 옵션보기 요청 url  : /item/{id}/option (id=${dto.id})

    @GetMapping("/{itemId}/option") //아이템 옵션 보기 페이지
    @PreAuthorize("hasRole('ROLE_BrandManager')")
    public String itemOption(@PathVariable("itemId")Long id,HttpServletResponse response, Model model){
        response.setContentType("text/html; charset=UTF-8");
        ResponseEntity<byte[]> result = null;
        //id,상품명 상품이미지(썸네일), 카테고리1, 카테고리 2, 가격
        //아이템 정보 가져오기
        Long itemId = Long.valueOf(id);
        Item item = itemService.getItemById(itemId);

            ItemManageDTO itemDTO = ItemManageDTO.builder()
                    .id(String.valueOf(item.getId()))
                    .name(item.getName())
                    .category1(item.getCategories().get(0).getParent().getName())
                    .category2(item.getCategories().get(0).getName())
                    .price(item.getPrice())
                    .build();
            //썸네일 이미지 넣어주기
            //s_"+uuid+"_"+imgName,
            String thumbnailURL = item.getItemImageList().get(0).getThumbnailFileName();
        itemDTO.setS_fileName(thumbnailURL);

        //옵션들도 있으면 넣어주기
        Optional<List<ItemDetail>> itemOptionsByItemId = itemService.getItemOptionsByItemId(id);
        if (itemOptionsByItemId.isPresent()){
            List<ItemOptionDTO> optionDTOList = new ArrayList<>();
            List<ItemDetail> itemOptions = itemOptionsByItemId.get();
            for (ItemDetail itemOption : itemOptions) {
                ItemOptionDTO itemOptionDTO = ItemOptionDTO.builder()
                        .id(String.valueOf(itemOption.getId()))
                        .parentId(String.valueOf(item.getId()))
                        .color(itemOption.getColor())
                        .size(itemOption.getSize())
                        .stockQuantity(itemOption.getStockQuantity())
                        .build();
                optionDTOList.add(itemOptionDTO);
            }

            model.addAttribute("itemOptions",optionDTOList);
        }


        model.addAttribute("itemDTO",itemDTO);


        return "seller/itemOptionRegistration";
    }

    //아이템옵션 수정 요청
    //http://localhost:8080/item/{itemId}/option/{itemOptionId}8/modify?p_Id=8&p_Color=white&p_Size=L&p_Stack=21
    //"@{/item/{itemId}/option/{optionId}/modify(itemId=${dto.parentId},optionId=${dto.id})}"
    @PostMapping("/{itemId}/option/{optionId}/modify")
    @PreAuthorize("hasRole('ROLE_BrandManager')")
    public String modifyOption(@PathVariable("itemId")String itemId,
                                @PathVariable("optionId") String optionId,
                                @RequestParam("p_Color")String color,
                               @RequestParam("p_Size")String size,
                               @RequestParam("p_Stack")String quantity){
        //옵션 조회한 후 수정
//        System.out.println("itemId = " + itemId);
//        System.out.println("optionId = " + optionId);
//        System.out.println("color = " + color);
//        System.out.println("size = " + size);
//        System.out.println("quantity = " + quantity);
        //부모 아이디로 리다이렉트트

        itemService.modifyItemOption(Long.valueOf(optionId),size,color,Integer.valueOf(quantity));

       return "redirect:/item/"+itemId+"/option";
    }
    @PostMapping("/{itemId}/option/new")//아이템 옵션 추가
    @PreAuthorize("hasRole('ROLE_BrandManager')")
    public String addOption(@PathVariable("itemId") String id,
                            @RequestParam("p_Color")String color,
                            @RequestParam("p_Size")String size,
                            @RequestParam("p_Stack")String quantity,
                            Model model){
        Item itemById = itemService.getItemById(Long.valueOf(id));
        ItemDetail itemOption = ItemDetail.builder()
                .parentItem(itemById)
                .size(size)
                .stockQuantity(Integer.valueOf(quantity))
                .color(color)
                .build();
        itemService.addItemOption(itemOption);

        return "redirect:/item/"+id+"/option";
    }
    @PostMapping("/{itemId}/option/{optionId}/delete")//옵션삭제
    ///item/option/"+optionId+"/delete
    @PreAuthorize("hasRole('ROLE_BrandManager')")
    public String deleteOption(@PathVariable("optionId")String id,
                                @PathVariable("itemId")String itemId){
        log.info("옵션삭제. itemId= "+itemId);
        log.info("옵션삭제. 옵션id= "+id);
        itemService.deleteOption(Long.valueOf(id));


        return "redirect:/item/"+itemId+"/option";

    }

    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName,String size){
        log.info("/display 요청 fileName="+fileName);
        ResponseEntity<byte[]> result = null;

        try{
            String srcFileName = URLDecoder.decode(fileName,"UTF-8");

            log.info("fileName :"+srcFileName);

            File file = new File(uploadPath + File.separator + srcFileName);
            if(size!=null &&size.equals("1")){
                file = new File(file.getParent(),file.getName().substring(2));
            }
            log.info("file: "+ file);

            HttpHeaders header = new HttpHeaders();

            //MIME타입 처리
            //파일의 확장자에 따라서 브라우저에 전송하는 MIME타입이 달라져야하는 문제 해결
            header.add("Content-Type", Files.probeContentType(file.toPath()));
            //파일 데이터 처리
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file),header,HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @GetMapping("/items")
    @PreAuthorize("hasRole('ROLE_BrandManager')")
    public String itemList(Model model, HttpServletResponse response, @AuthenticationPrincipal CustomerAuthDTO customerAuthDTO){
        response.setContentType("text/html; charset=UTF-8");
        ResponseEntity<byte[]> result = null;
        //id,상품명 상품이미지(썸네일), 카테고리1, 카테고리 2, 가격
        //브랜드 정보 가져오기
        String userId = customerAuthDTO.getUserId();
        Customer byUserid = customerService.findByUserid(userId);
        Brand brand = byUserid.getBrand();
        List<Item> itemList = brand.getItemList();
        List<ItemManageDTO> itemDTOList = new ArrayList<>();
        for (Item item : itemList) {
            ItemManageDTO itemManageDTO = ItemManageDTO.builder()
                    .id(String.valueOf(item.getId()))
                    .name(item.getName())
                    .category1(item.getCategories().get(0).getParent().getName())
                    .category2(item.getCategories().get(0).getName())
                    .price(item.getPrice())
                    .build();
            //썸네일 이미지 넣어주기
            //s_"+uuid+"_"+imgName,
            String thumbnailURL = item.getItemImageList().get(0).getThumbnailFileName();
            itemManageDTO.setS_fileName(thumbnailURL);

            itemDTOList.add(itemManageDTO);
        }
        model.addAttribute("itemDTOList",itemDTOList);
        return "seller/productManage";
    }
    

    @GetMapping("/register")
    @PreAuthorize("hasRole('ROLE_BrandManager')")
    public String registerForm(Model model){

        List<CategoryDTO> categoryList =  categoryService.getCategoryDTOList();
        //Category.name =부모카테고리네임. Category.childCategoryNames.get(i) = 자식 카테고리 이름들
        model.addAttribute("categoryList",categoryList);


        return "/seller/itemRegistration";
    }
    @PostMapping("/{itemId}/modify")
    @PreAuthorize("hasRole('ROLE_BrandManager')")
    public String itemModify(@AuthenticationPrincipal CustomerAuthDTO customerAuthDTO,
                         @RequestParam("pp_Category1")String category1,
                         @RequestParam("pp_Category2")String category2,
                         @RequestParam("pp_Price")String price,
                         @RequestParam("pp_Name")String name,
                         @RequestParam("m_Img")MultipartFile[] uploadFiles,
                         @PathVariable("itemId")String modifyId){

        //아이템조회
        Item itemById = itemService.getItemById(Long.valueOf(modifyId));
        //수정할 카테고리
        Category category = categoryService.getCategoryByName(category2);
        //아이템정보 수정
        itemById.modifyItem(name,Integer.valueOf(price),category);
        List<ItemImage> originalItemImageList = itemById.getItemImageList();

        int index =0;
        List<ItemImage> itemImageList = new ArrayList<>();
        for (MultipartFile uploadFile : uploadFiles) {

//            //이미지파일만 업로드 가능
//            if(uploadFile.getContentType().startsWith("image")==false){
//                log.warn("this file is not image type");
//                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//            }

            //실제 파일 이름 IE나 Edge는 전체 경로가 들어오므로. 크롬은 파일이름
            String originalName = uploadFile.getOriginalFilename();
            String fileName = originalName;//.substring(originalName.lastIndexOf("\\"+1));

            log.info("fileName:  "+fileName);
            //날짜 폴더 생성
            String folderPath = makeFolder();

            //UUID
            String uuid = UUID.randomUUID().toString();
            //저장할 때 파일 이름 중간에 "_"를 이용해서 구분
            String saveName = uploadPath + File.separator+folderPath + File.separator + uuid +"_"+ fileName;
            //String saveName = uploadPath+File.separator+fileName;
            Path savePath = Paths.get(saveName);

            try{
                //원본파일 저장
                uploadFile.transferTo(savePath);
                //섬네일 생성(섬네일 파일 이름은 이름을 s_로 시작하도록한다
                //롱이미지는 섬네일 필요 없음
                if (index==1){
                    String thumbnailSaveName = uploadPath +File.separator+folderPath+File.separator
                            +"s_"+uuid+"_"+fileName;
                    File thumbnailFile = new File(thumbnailSaveName);
                    //섬네일 생성
                    Thumbnailator.createThumbnail(savePath.toFile(),thumbnailFile,100,100);
                }
                //변경 감지 활용하여 itemImage 바꾸기
                ItemImage originalItemImage = originalItemImageList.get(index);
                originalItemImage.setImgName(saveName);
                originalItemImage.setPath(uploadPath + File.separator+folderPath + File.separator);
                originalItemImage.setUuid(uuid);
                index++;
            }catch(IOException e){
                e.printStackTrace();
            }
        }



        return "redirect:/item/items";
    }
    @PostMapping("/register")
    @PreAuthorize("hasRole('ROLE_BrandManager')")
    public String register(@AuthenticationPrincipal CustomerAuthDTO customerAuthDTO,
                            @RequestParam("pp_Category1")String category1,
                           @RequestParam("pp_Category2")String category2,
                           @RequestParam("pp_Price")String price,
                           @RequestParam("pp_Name")String name,
                           @RequestParam("m_Img")MultipartFile[] uploadFiles){
        //상품 카테고리, 상품 이름, 상품 가격, 대표이미지(+썸네일 처리), 롱이미지
//        System.out.println("category1 = " + category1);
//        System.out.println("category2 = " + category2);
//        System.out.println("price = " + price);
//        System.out.println("name = " + name);
//        System.out.println("uploadFiles = " + uploadFiles[0].getOriginalFilename());
//        System.out.println("uploadFiles = " + uploadFiles[1].getOriginalFilename());

        //아이템등록
        Item item = Item.builder()
                .name(name)
                .price(Integer.valueOf(price)).build();


        //카테고리 목록에 아이템 정보 넣기

        Category category = categoryService.getCategoryByName(category2);
        category.getItems().add(item);
        item.getCategories().add(category);

        //브랜드 엔티티 가져오기
        String userId = customerAuthDTO.getUserId();
        Customer brandManager = customerService.findByUserid(userId);
        Brand brand = brandManager.getBrand();


        //브랜드 목록에 아이템 넣기, 아이템에 브랜드 추가
        brand.getItemList().add(item);
        item.setBrand(brand);

        //아이템 사진 등록
        int index =0;
        List<ItemImage> itemImageList = new ArrayList<>();
        for (MultipartFile uploadFile : uploadFiles) {
            index++;
//            //이미지파일만 업로드 가능
//            if(uploadFile.getContentType().startsWith("image")==false){
//                log.warn("this file is not image type");
//                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//            }

            //실제 파일 이름 IE나 Edge는 전체 경로가 들어오므로. 크롬은 파일이름
            String originalName = uploadFile.getOriginalFilename();
            String fileName = originalName;//.substring(originalName.lastIndexOf("\\"+1));

            log.info("fileName:  "+fileName);
            //날짜 폴더 생성
            String folderPath = makeFolder();

            //UUID
            String uuid = UUID.randomUUID().toString();
            //저장할 때 파일 이름 중간에 "_"를 이용해서 구분
            String saveName = uploadPath + File.separator+folderPath + File.separator + uuid +"_"+ fileName;
            //String saveName = uploadPath+File.separator+fileName;
            Path savePath = Paths.get(saveName);

            try{
                //원본파일 저장
                uploadFile.transferTo(savePath);
                //섬네일 생성(섬네일 파일 이름은 이름을 s_로 시작하도록한다
                //롱이미지는 섬네일 필요 없음
                if (index==1){
                    String thumbnailSaveName = uploadPath +File.separator+folderPath+File.separator
                            +"s_"+uuid+"_"+fileName;
                    File thumbnailFile = new File(thumbnailSaveName);
                    //섬네일 생성
                    Thumbnailator.createThumbnail(savePath.toFile(),thumbnailFile,100,100);
                }
                ItemImage itemImage = ItemImage.builder()
                        .item(item)
                        .imgName(saveName)
                        .path(uploadPath + File.separator+folderPath + File.separator)
                        .uuid(uuid)
                        .build();
                itemImageList.add(itemImage);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
//        return new ResponseEntity<>(resultDTOList,HttpStatus.OK);
        itemService.registerItem(item,category,itemImageList.get(0),itemImageList.get(1)); //카테고리 넣어주면 알아서 리스트에 추가, 아이템은 저장, 아이템 이미지도 저장


        return "redirect:/item/items";
    }

        private String makeFolder(){
        String str = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String folderPath = str.replace("/", File.separator);

        //make floder --------------
        File uploadPathFolder = new File(uploadPath, folderPath);

        if(uploadPathFolder.exists() ==false){

            uploadPathFolder.mkdirs();
        }
        return folderPath;
    }

}
