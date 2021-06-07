package project.shop.portfolio.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.shop.portfolio.domain.item.Item;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Review extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;//아이템으로 리뷰 조회

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer; //회원으로 리뷰 조회(자신이 남긴 리뷰 확인)

    private int grade;

    private String text;
    @Builder.Default
    @OneToMany(mappedBy = "review")
    private List<Comments> commentsList = new ArrayList<>();

    //이미지 리뷰??

    public void changeGrade(int grade){
        this.grade = grade;
    }
    public void changeText(String text){
        this.text = text;
    }


    public void deleteReview() {
        this.text ="";
        this.customer = null;
    }
}
