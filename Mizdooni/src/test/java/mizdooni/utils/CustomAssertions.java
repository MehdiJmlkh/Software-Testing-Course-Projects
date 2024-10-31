package mizdooni.utils;

import mizdooni.response.ResponseException;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomAssertions {
    public static void assertEquals(ResponseException expected, ResponseException actual) {
        Assertions.assertEquals(expected.getStatus(), actual.getStatus());
        Assertions.assertEquals(expected.getMessage(), actual.getMessage());
        Assertions.assertEquals(expected.getError(), actual.getError());
    }

}
