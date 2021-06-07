package project.shop.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@AllArgsConstructor
public class UploadResultDTO {
    //실제 파일과 관련된 모든 정보를 가짐
    private String fileName;
    private String uuid;
    private String folderPath;
    public String getImageURL(){
        //나중에 전체 경로가 필요한 경우를 대비해서 메소드로 제공
        try{
            return URLEncoder.encode(folderPath+"/"+uuid+"_"+fileName,"UTF-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return "";
    }
    public String getThumbnailURL(){
        try{
            return URLEncoder.encode(folderPath+"/s_"+uuid+"_"+fileName,"UTF-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return "";
    }

}
