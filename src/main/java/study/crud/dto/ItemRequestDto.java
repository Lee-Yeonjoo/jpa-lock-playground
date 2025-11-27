package study.crud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ItemRequestDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CreateDto {
        private String name;
        private String code;
        private int quantity;
        private int price;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class UpdateDto {
        private String name;
        private int quantity;
        private int price;
    }
}
