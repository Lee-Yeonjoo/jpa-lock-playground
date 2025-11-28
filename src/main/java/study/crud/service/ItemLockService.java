package study.crud.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.crud.dto.ItemResponseDto;
import study.crud.repository.ItemRepository;

@Service
@RequiredArgsConstructor
public class ItemLockService {

    private final ItemService itemService;
    private final ItemRepository itemRepository;

    /**
     * 네임드 락 설정 -> 비즈니스 로직과 트랜잭션을 분리해야함
     */
    @Transactional
    public ItemResponseDto.DecreaseDto decreaseWithLock(Long itemId, int amount) throws Exception {
        try {
            itemRepository.getLock(itemId.toString());  //락 설정
            return itemService.decrease(itemId, amount);
        } finally {  //finally문에 리턴문 있으면 예외 덮어서 무시된다. 주의
            itemRepository.releaseLock(itemId.toString());  //락을 반드시 해제
        }
    }
}
