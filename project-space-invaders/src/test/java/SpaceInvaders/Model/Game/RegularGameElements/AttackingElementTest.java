package SpaceInvaders.Model.Game.RegularGameElements;

import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AttackingElementTest {

    private AttackingElement attackingElement;

    private static Stream<Arguments> testEqualsValues() {
        AttackingElement a1 = new AttackingElement(new Position(5, 5), 100, 10);
        AttackingElement a2 = new AttackingElement(new Position(0, 0), 100, 10);
        AttackingElement a3 = new AttackingElement(new Position(5, 5), 10, 10);
        AttackingElement a4 = new AttackingElement(new Position(5, 5), 100, 20);
        AttackingElement a5 = new AttackingElement(new Position(3, 3), 10, 20);
        AttackingElement a6 = new AttackingElement(new Position(5, 5), 100, 10);
        return Stream.of(
                Arguments.of(a1, a1, true),
                Arguments.of(a1, a2, false),
                Arguments.of(a1, a3, false),
                Arguments.of(a1, a4, false),
                Arguments.of(a1, a5, false),
                Arguments.of(a1, a6, true),
                Arguments.of(a1, new Object(), false)
        );
    }

    @BeforeEach
    void setUp() {
        attackingElement = new AttackingElement(new Position(5, 5), 100, 10);
    }

    @Test
    void testGetDamagePerShot() {
        assertEquals(10, attackingElement.getDamagePerShot());
    }

    @Test
    void testSetDamagePerShot() {
        attackingElement.setDamagePerShot(20);
        assertEquals(20, attackingElement.getDamagePerShot());
    }

    @ParameterizedTest
    @MethodSource("testEqualsValues")
    void testEquals(AttackingElement element1, Object element2, boolean expected) {
        assertEquals(expected, element1.equals(element2));
    }

    @Test
    void testHashCode() {
        AttackingElement sameElement = new AttackingElement(new Position(5, 5), 100, 10);
        assertEquals(attackingElement.hashCode(), sameElement.hashCode());
    }
}