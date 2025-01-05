package SpaceInvaders.Model.Game.RegularGameElements;

import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DestroyableElementTest {

    private DestroyableElement destroyableElement;

    private static Stream<Arguments> testEqualsValues() {
        DestroyableElement d1 = new DestroyableElement(new Position(5, 5), 100);
        DestroyableElement d2 = new DestroyableElement(new Position(0, 0), 100);
        DestroyableElement d3 = new DestroyableElement(new Position(5, 5), 50);
        DestroyableElement d4 = new DestroyableElement(new Position(5, 5), 100);
        return Stream.of(
                Arguments.of(d1, d1, true),
                Arguments.of(d1, d2, false),
                Arguments.of(d1, d3, false),
                Arguments.of(d1, d4, true),
                Arguments.of(d1, new Object(), false)
        );
    }

    @BeforeEach
    public void setUp() {
        destroyableElement = new DestroyableElement(new Position(5, 5), 100);
    }

    @Test
    public void testGetHealth() {
        assertEquals(100, destroyableElement.getHealth());
    }

    @Test
    public void testSetHealth() {
        destroyableElement.setHealth(80);
        assertEquals(80, destroyableElement.getHealth());
    }

    @Test
    public void testDecreaseHealth() {
        destroyableElement.decreaseHealth(30);
        assertEquals(70, destroyableElement.getHealth());
    }

    @ParameterizedTest
    @CsvSource({"0, true", "1, false"})
    public void testIsDestroyed(int health, boolean expected) {
        destroyableElement.setHealth(health);
        assertEquals(expected, destroyableElement.isDestroyed());
    }

    @ParameterizedTest
    @MethodSource("testEqualsValues")
    public void testEquals(DestroyableElement d1, Object d2, boolean expected) {
        assertEquals(expected, d1.equals(d2));
    }

    @ParameterizedTest
    @CsvSource({"0, 0, 0, 1922",           // Origin point
            "10, 10, 10, 2552",       // Positive numbers
            "-10, -10, -10, 1292",    // Negative numbers
    })
    public void testHashCode(int x1, int y1, int health, int expected) {
        Position position1 = new Position(x1, y1);
        DestroyableElement destroyableElement = new DestroyableElement(position1, health);
        assertEquals(expected, destroyableElement.hashCode());
    }
}