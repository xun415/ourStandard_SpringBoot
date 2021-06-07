package project.shop.portfolio.domain.item;

import lombok.*;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "item")
public class ItemImage {
    @Id
    @GeneratedValue
    @Column(name = "item_image_id")
    private Long id;

    private String uuid;

    private String imgName;

    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    public String getThumbnailFileName(){
        String folderPath = this.path.substring(18);
        String substring = this.getImgName().substring(path.length());
        try{
//            System.out.println("경로 = " + folderPath+"s_"+substring);
            return URLEncoder.encode(folderPath+"s_"+substring,"UTF-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return "";
    }
    public String getFileNameWithoutUploadPath(){
        String folderPath = this.path.substring(18);
        String substring = this.getImgName().substring(path.length());
        try{
//            System.out.println("경로 = " + folderPath+substring);
            return URLEncoder.encode(folderPath+substring,"UTF-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return "";
    }
    public String getThumbnailURLWithoutPath(){
        
        try{
            return URLEncoder.encode("s_"+uuid+"_"+imgName,"UTF-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return "";
    }

    public String getThumbnailURL(){
        try{
            return URLEncoder.encode(path+"/s_"+uuid+"_"+imgName,"UTF-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return "";
    }
}
