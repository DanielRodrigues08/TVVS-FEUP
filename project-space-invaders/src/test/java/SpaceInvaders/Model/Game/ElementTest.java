package SpaceInvaders.Model.Game;

import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ElementTest {

    static Stream<Arguments> provideEqualsTestCases() {
        Element e1 = new Element(new Position(1, 2)) {
        };
        Element e2 = new Element(new Position(1, 2)) {
        };
        Element e3 = new Element(new Position(3, 4)) {
        };
        Object o = new Object();
        return Stream.of(
                Arguments.of(e1, e1, true),
                Arguments.of(e1, e2, true),
                Arguments.of(e1, e3, false),
                Arguments.of(e1, o, false)
        );
    }

    @Test
    public void testGetPosition() {
        Position position = new Position(1, 2);
        Element element = new Element(position) {
        };
        assertEquals(position, element.getPosition());
    }

    @Test
    public void testSetPosition() {
        Position position = new Position(1, 2);
        Element element = new Element(position) {
        };
        Position newPosition = new Position(3, 4);
        element.setPosition(newPosition);
        assertEquals(newPosition, element.getPosition());
    }

    @ParameterizedTest
    @MethodSource("provideEqualsTestCases")
    public void testEquals(Element o1, Object o2, boolean expected) {
        assertEquals(expected, o1.equals(o2));
    }

    @Test
    public void testHashCode() {
        Position position = new Position(1, 2);
        Element element = new Element(position) {
        };
        assertEquals(position.hashCode(), element.hashCode());
    }
}