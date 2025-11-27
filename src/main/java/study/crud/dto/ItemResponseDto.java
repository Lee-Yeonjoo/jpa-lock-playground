package study.crud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ItemResponseDto {

    @Getter  //응답dto에도 getter 있어야 Json 직렬화가 가능하다.. -> response entity 써서 그런가?
    @Builder
    @AllArgsConstructor
    public static class CreateDto {
        private Long itemId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class UpdateDto {
        private Long itemId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ReadDto {
        private Long itemId;
        private String name;
        private String code;
        private int quantity;
        private int price;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class DecreaseDto {
        private Long itemId;
        private int quantity;
    }
}
