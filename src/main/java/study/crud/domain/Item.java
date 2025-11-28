package study.crud.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String code;

    private int quantity;

    private int price;

    //낙관적 락은 버전 필드 필요
    @Version
    private Long version;

    public void updateItem(String name, int quantity, int price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }


    //synchronized -> JVM 내부에서만 유효. 즉, 단일 서버, 단일 JVM에서는 효과o
    //실제 운영 환경에서는 서버(or컨테이너)가 여러대 -> synchronized 효과x
    public void decreaseQuantity(int amount) throws Exception {
        //음수 체크
        if (quantity < amount) {
            //예외 처리 - 음수 불가
            throw new Exception("재고가 없습니다.");
        }
        this.quantity -= amount;
        log.info("Thread={}, Before={}, After={}", Thread.currentThread().getName(), quantity + amount, quantity);
    }
}
