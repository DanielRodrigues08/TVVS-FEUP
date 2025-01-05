package SpaceInvaders.Model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PositionTest {

    private static Stream<Arguments> positionEqualityTestCases() {
        Position samePosition = new Position(5, 10);
        return Stream.of(
                // format: x1, y1, object2, expectedResult, testCase
                Arguments.of(5, 10, new Position(5, 10), true),
                Arguments.of(5, 10, samePosition, true),
                Arguments.of(5, 10, new Object(), false),
                Arguments.of(6, 10, new Position(5, 10), false),
                Arguments.of(5, 11, new Position(5, 10), false),
                Arguments.of(5, 10, null, false)
        );
    }

    @Test
    public void testPositionCreation() {
        Position position = new Position(5, 10);
        assertEquals(5, position.getX());
        assertEquals(10, position.getY());
    }

    @ParameterizedTest
    @CsvSource({"0, 0, 961",           // Origin point
            "10, 10, 1281",       // Positive numbers
            "-10, -10, 641",    // Negative numbers
    })
    public void testPositionHashCode(int x1, int y1, int expected) {
        Position position1 = new Position(x1, y1);
        assertEquals(expected, position1.hashCode());
    }

    @ParameterizedTest
    @MethodSource("positionEqualityTestCases")
    public void testPositionEquality(int x1, int y1, Object obj2, boolean expected) {
        Position position1 = new Position(x1, y1);

        assertEquals(expected, position1.equals(obj2));
    }
}