package ru.clevertec.test.checkapp.util.impl;

import com.google.gson.Gson;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import ru.clevertec.test.checkapp.handler.JsonSerializeHandler;
import ru.clevertec.test.checkapp.model.ProductModel;

import java.util.ArrayList;
import java.util.List;

class JsonConverterTest {
    private JsonConverter converter;
    @BeforeEach
    void setup() {
        converter = new JsonConverter(new JsonSerializeHandler(),new Gson());
    }

    @Nested
    class Serialize {
        @Nested
        class SerializeObject {
            @Test
            void serializeWithObjectShouldBeCorrectJSON() {
                String expected = "{\"id\":1,\"name\":null,\"offer\":true,\"price\":1000.0}";
                ProductModel product = ProductModel.builder()
                        .id(1)
                        .price(1000)
                        .offer(true)
                        .build();
                String result = converter.serialize(product);
                Assertions.assertThat(result).isEqualTo(expected);
            }
            @Test
            void serializeWithNullObjectShouldReturnJSONNull() {
                String expected = "null";
                String result = converter.serialize((Object) null);
                Assertions.assertThat(result).isEqualTo(expected);
            }
            @Test
            void serializeWithObjectShouldThrowHttpMediaTypeNotAcceptableException() {
                Assertions.assertThatThrownBy(() -> converter.serialize(new Object()))
                        .isInstanceOf(HttpMediaTypeNotAcceptableException.class);
            }
        }
        @Nested
        class SerializeArray {
            @Test
            void serializeWithObjectArrayShouldReturnCorrectEmpty() {
                String expected = "[{\"id\":1,\"name\":null,\"offer\":true,\"price\":1000.0},{\"id\":2,\"name\":null,\"offer\":false,\"price\":2000.0},{\"id\":3,\"name\":null,\"offer\":true,\"price\":3003.0}]";
                ProductModel[] productArray = new ProductModel[] {
                        ProductModel.builder()
                                .id(1)
                                .price(1000)
                                .offer(true)
                                .build(),
                        ProductModel.builder()
                                .id(2)
                                .price(2000)
                                .offer(false)
                                .build(),
                        ProductModel.builder()
                                .id(3)
                                .price(3003)
                                .offer(true)
                                .build()
                };
                String result = converter.serialize(productArray);

                Assertions.assertThat(result).isEqualTo(expected);
            }
            @Test
            void serializeWithNullObjectArrayShouldReturnNull() {
                String expected = "null";
                String result = converter.serialize((Object[]) null);
                Assertions.assertThat(result).isEqualTo(expected);
            }

            @Test
            void serializeWithEmptyArrayShouldReturnEmptyJSONArray() {
                String expected = "[]";
                String result = converter.serialize(new Object[]{});
                Assertions.assertThat(result).isEqualTo(expected);
            }
            @Test
            void serializeWithObjectArrayShouldThrowHttpMediaTypeNotAcceptableException() {
                Assertions.assertThatThrownBy(() -> converter.serialize(new Object[]{new Object(), new Object()}))
                        .isInstanceOf(HttpMediaTypeNotAcceptableException.class);
            }
        }

        @Nested
        class SerializeList {
            @Test
            void serializeWithObjectArrayListShouldReturnCorrectJSON() {
                String expected = "[{\"id\":1,\"name\":null,\"offer\":true,\"price\":1000.0},{\"id\":2,\"name\":null,\"offer\":false,\"price\":2000.0},{\"id\":3,\"name\":null,\"offer\":true,\"price\":3003.0}]";
                List<ProductModel> productArray = List.of(
                        ProductModel.builder()
                                .id(1)
                                .price(1000)
                                .offer(true)
                                .build(),
                        ProductModel.builder()
                                .id(2)
                                .price(2000)
                                .offer(false)
                                .build(),
                        ProductModel.builder()
                                .id(3)
                                .price(3003)
                                .offer(true)
                                .build()
                );
                String result = converter.serialize(productArray);

                Assertions.assertThat(result).isEqualTo(expected);
            }
            @Test
            void serializeWithNullArrayListShouldReturnJSONNull() {
                String expected = "null";
                String result = converter.serialize((List<?>) null);
                Assertions.assertThat(result).isEqualTo(expected);
            }

            @Test
            void serializeWithEmptyArrayListShouldReturnEmptyJSONArray() {
                String expected = "[]";
                String result = converter.serialize(new ArrayList<>());
                Assertions.assertThat(result).isEqualTo(expected);
            }
            @Test
            void serializeWithObjectArrayListShouldHttpMediaTypeNotAcceptableException() {
                Assertions.assertThatThrownBy(() -> converter.serialize(List.of(new Object(), new Object())))
                        .isInstanceOf(HttpMediaTypeNotAcceptableException.class);
            }
        }
    }
    @Nested
    class Deserialize {
        @Test
        void deserializeWithObjectShouldReturnCorrectObject() {
            ProductModel expected = ProductModel.builder()
                    .id(1)
                    .price(1000)
                    .offer(true)
                    .build();
            String json = "{\"id\":1,\"name\":null,\"offer\":true,\"price\":1000.0}";

            ProductModel deserialize = converter.deserialize(json, ProductModel.class);

            Assertions.assertThat(deserialize).isEqualTo(expected);
        }
    }
}