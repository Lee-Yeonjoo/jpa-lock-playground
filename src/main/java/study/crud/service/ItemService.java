package study.crud.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.StaleObjectStateException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.crud.domain.Item;
import study.crud.dto.ItemRequestDto;
import study.crud.dto.ItemResponseDto;
import study.crud.repository.ItemRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)  //뭔 차이인지 알아보기 -> readOnly여서 변경 작업이 제한됨
public class ItemService {

    private final ItemRepository itemRepository;

    /**
     * 상품 등록
     */
    @Transactional
    public ItemResponseDto.CreateDto create(Item item) {
        //상품 코드가 중복되면 안됨
        if (itemRepository.existsByCode(item.getCode())) {
            //예외 응답

        }

        Item newItem = itemRepository.save(item);
        //여기서 api 외부 호출 과정이 있다면, save에서 에러가 났을 때, api 호출 과정이 실행 되는가?

        return ItemResponseDto.CreateDto.builder()
                .itemId(newItem.getId())
                .build();
    }

    /**
     * 상품 수정
     */
    @Transactional  //이걸 추가해야 set가능
    public ItemResponseDto.UpdateDto update(Long itemId, ItemRequestDto.UpdateDto request) {
        Item item = itemRepository.findById(itemId).orElseThrow();  //예외 처리
        item.updateItem(request.getName(), request.getQuantity(), request.getPrice());
        return ItemResponseDto.UpdateDto.builder()
                .itemId(item.getId())
                .build();
    }

    /**
     * 상품 조회
     */
    public ItemResponseDto.ReadDto read(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow();  //예외 처리
        return ItemResponseDto.ReadDto.builder()
                .itemId(item.getId())
                .name(item.getName())
                .code(item.getCode())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .build();
    }

    /**
     * 상품 목록 조회
     */
    public Page<ItemResponseDto.ReadDto> readList(int size) {
        PageRequest pageRequest = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Item> items = itemRepository.findAll(pageRequest);

        return items.map(item -> ItemResponseDto.ReadDto.builder()
                .itemId(item.getId())
                .name(item.getName())
                .code(item.getCode())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .build());
    }

    /**
     * 재고 감소
     */
    //@Retryable이 @Transactional보다 위에 와야 트랜잭션이 매번 새로 생성됨
    @Retryable(
            value = ObjectOptimisticLockingFailureException.class,
            maxAttempts = 5,
            backoff = @Backoff(delay = 50)
    )
    @Transactional  //synchronized랑 같이 쓰면 동기화x
    public ItemResponseDto.DecreaseDto decrease(Long itemId, int amount) throws Exception {
                Item item = itemRepository.findByIdWithOptimisticLock(itemId).orElseThrow();  //예외처리
                item.decreaseQuantity(amount);
                return ItemResponseDto.DecreaseDto.builder()
                        .itemId(item.getId())
                        .quantity(item.getQuantity())
                        .build();
    }
}
