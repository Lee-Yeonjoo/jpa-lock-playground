package study.crud.controller;

import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.hibernate.StaleObjectStateException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;
import study.crud.domain.Item;
import study.crud.dto.ItemRequestDto;
import study.crud.dto.ItemResponseDto;
import study.crud.service.ItemService;

import java.util.Optional;

@RestController  //차이 공부
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    /**
     * 상품 등록
     */
    @PostMapping
    public ResponseEntity<ItemResponseDto.CreateDto> create(@RequestBody ItemRequestDto.CreateDto request) {
        return ResponseEntity.of(Optional.of(itemService.create(Item.builder()
                .name(request.getName())
                .code(request.getCode())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .build())));
    }

    /**
     * 상품 수정
     */
    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemResponseDto.UpdateDto> update(@PathVariable("itemId") Long itemId, @RequestBody ItemRequestDto.UpdateDto request) {
        return ResponseEntity.of(Optional.of(itemService.update(itemId, request)));
    }

    /**
     * 상품 조회
     */
    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponseDto.ReadDto> read(@PathVariable("itemId") Long itemId) {
        return ResponseEntity.of(Optional.of(itemService.read(itemId)));
    }

    /**
     * 상품 목록 조회
     */
    @GetMapping("/list")
    public ResponseEntity<Page<ItemResponseDto.ReadDto>> readList(@RequestParam int size) {
        return ResponseEntity.of(Optional.of(itemService.readList(size)));
    }

    /**
     * 재고 감소 -> 동시성 해결해보기
     */
    @PatchMapping("/{itemId}/quantity")
    public ResponseEntity<ItemResponseDto.DecreaseDto> decreaseQuantity(@PathVariable("itemId") Long itemId, @RequestParam int amount) throws Exception {
        return ResponseEntity.of(Optional.of(itemService.decrease(itemId, amount)));
    }

}
