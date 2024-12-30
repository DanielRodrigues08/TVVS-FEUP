package SpaceInvaders.Model;

import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    void testPositionCreation() {
        Position position = new Position(5, 10);
        assertEquals(5, position.getX());
        assertEquals(10, position.getY());
    }

    @Test
    void testPositionEquality() {
        Position position1 = new Position(5, 10);
        Position position2 = new Position(5, 10);

        assertEquals(position1, position2);
    }

    @Test
    void testPositionEqualitySameObject() {
        Position position1 = new Position(5, 10);

        assertEquals(position1, position1);
    }

    @Test
    void testPositionEqualityDifferentClass() {
        Position position1 = new Position(5, 10);
        Object object = new Object();
        assertFalse(position1.equals(object));
    }

    @ParameterizedTest
    @CsvSource({"6, 10, 5, 10", "5, 11, 5, 10", "5, 11, 10, 6"})
    void testPositionInequality(int x1, int y1, int x2, int y2) {
        Position position1 = new Position(x1, y1);
        Position position2 = new Position(x2, y2);

        assertNotEquals(position1, position2);
    }


    @Test
    void testPositionHashCode() {
        Position position1 = new Position(5, 10);
        Position position2 = new Position(5, 10);

        assertEquals(position1.hashCode(), position2.hashCode());
    }

    @Test
    void testPositionInequality() {
        Position position1 = new Position(5, 10);
        Position position2 = new Position(10, 5);

        assertNotEquals(position1, position2);
    }
}