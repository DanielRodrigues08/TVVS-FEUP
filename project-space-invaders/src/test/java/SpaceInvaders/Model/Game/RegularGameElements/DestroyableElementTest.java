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
    void setUp() {
        destroyableElement = new DestroyableElement(new Position(5, 5), 100);
    }

    @Test
    void testGetHealth() {
        assertEquals(100, destroyableElement.getHealth());
    }

    @Test
    void testSetHealth() {
        destroyableElement.setHealth(80);
        assertEquals(80, destroyableElement.getHealth());
    }

    @Test
    void testDecreaseHealth() {
        destroyableElement.decreaseHealth(30);
        assertEquals(70, destroyableElement.getHealth());
    }

    @ParameterizedTest
    @CsvSource({"0, true", "1, false"})
    void testIsDestroyed(int health, boolean expected) {
        destroyableElement.setHealth(health);
        assertEquals(expected, destroyableElement.isDestroyed());
    }

    @ParameterizedTest
    @MethodSource("testEqualsValues")
    void testEquals(DestroyableElement d1, Object d2, boolean expected) {
        assertEquals(expected, d1.equals(d2));
    }

    @Test
    void testHashCode() {
        DestroyableElement sameElement = new DestroyableElement(new Position(5, 5), 100);
        assertEquals(destroyableElement.hashCode(), sameElement.hashCode());
    }
}