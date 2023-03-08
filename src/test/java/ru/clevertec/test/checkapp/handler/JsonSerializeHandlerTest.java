package ru.clevertec.test.checkapp.handler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.clevertec.test.checkapp.model.ProductModel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonSerializeHandlerTest {
    private JsonSerializeHandler handler;
    @BeforeEach
    void setup() {
        handler = new JsonSerializeHandler();
    }

    @Nested
    class JoinToString {
        @Test
        void joinToStringShouldReturnCorrectString() {
            String expected = "hello,my name,is Test";

            String result = handler.joinToString(List.of("hello", "my name", "is Test"), Object::toString);

            Assertions.assertThat(result).isEqualTo(expected);
        }

        @Test
        void joinToStringShouldThrowStringIndexOutOfBoundsException() {
            Assertions.assertThatThrownBy(() -> handler.joinToString(List.of(), Object::toString))
                    .isInstanceOf(StringIndexOutOfBoundsException.class);
        }
    }
    @Nested
    class ConvertToPrimitive {
        @Test
        void convertToPrimitiveShouldReturnCorrectString() {
            String expected = "1";

            String result = handler.convertToPrimitive(1);

            Assertions.assertThat(result).isEqualTo(expected);
        }

        @Test
        void convertToPrimitiveShouldReturnObjectString() {
            String expected = "ProductModel(id=1, name=test, offer=true, price=1000.0)";

            String result = handler.convertToPrimitive(
                    ProductModel.builder()
                            .id(1)
                            .name("test")
                            .price(1000)
                            .offer(true)
                            .build()
            );

            Assertions.assertThat(result).isEqualTo(expected);
        }
    }
    @Nested
    class ConvertToText {
        @Test
        void convertToTextShouldReturnCorrectString() {
            String expected = "\"text\"";


            String result = handler.convertToPrimitive("text");

            Assertions.assertThat(result).isEqualTo(expected);
        }

        @Test
        void convertToTextShouldReturnObjectString() {
            String expected = "\"ProductModel(id=1, name=test, offer=true, price=1000.0)\"";

            String result = handler.convertToText(
                    ProductModel.builder()
                            .id(1)
                            .name("test")
                            .price(1000)
                            .offer(true)
                            .build()
            );

            Assertions.assertThat(result).isEqualTo(expected);
        }
    }
}