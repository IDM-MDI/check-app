package ru.clevertec.test.checkapp.handler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.clevertec.test.checkapp.entity.Product;
import ru.clevertec.test.checkapp.model.ProductModel;

import java.math.BigDecimal;
import java.util.List;

import static ru.clevertec.test.checkapp.builder.impl.ProductBuilder.aProduct;

class JsonSerializeHandlerTest {
    private static final ProductModel MODEL = aProduct()
            .withId(1L)
            .withName("test")
            .withPrice(new BigDecimal(1000))
            .withOnOffer(true)
            .buildToModel();
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

            String result = handler.convertToPrimitive(MODEL);

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

            String result = handler.convertToText(MODEL);

            Assertions.assertThat(result).isEqualTo(expected);
        }
    }
}