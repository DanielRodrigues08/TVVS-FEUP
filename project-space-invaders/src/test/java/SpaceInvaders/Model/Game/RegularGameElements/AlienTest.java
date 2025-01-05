package SpaceInvaders.Model.Game.RegularGameElements;

import SpaceInvaders.Model.Game.Element;
import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AlienTest {

    private Alien alien;

    @BeforeEach
    public void setUp() {
        alien = new Alien(new Position(1, 1), 1, 1, 1, AlienState.PASSIVE, 0);
    }

    @ParameterizedTest
    @CsvSource({"3, 0, 2, 2"})
    public void testAlien(int Type, int expectedType) {
        Alien alien1 = new Alien(new Position(1, 1), 1, 1, 1, AlienState.PASSIVE, Type);
        assertEquals(expectedType, alien1.getType());
    }

    @ParameterizedTest
    @CsvSource({"NORMAL_MODE, 1",
            "SCORE_2X, 2",
            "SCORE_3X, 3",
            "SCORE_4X, 4",
            "SCORE_5X, 5",
            "SCORE_10X, 10"})
    public void testGetScore(AlienMode alienMode, int expectedScore) {
        alien.setAlienMode(alienMode);
        assertEquals(expectedScore, alien.getScore());
    }

    @Test
    public void testIncreaseScore() {
        alien.increaseScore(2);
        assertEquals(2, alien.getScore());
    }

    @Test
    public void testAlienState() {
        alien.setAlienState(AlienState.ATTACKING);
        assertEquals(AlienState.ATTACKING, alien.getAlienState());
    }

    @Test
    public void testAlienMode() {
        alien.setAlienMode(AlienMode.SCORE_2X);
        assertEquals(AlienMode.SCORE_2X, alien.getAlienMode());
    }

    @Test
    public void testScore() {
        alien.setScore(2);
        assertEquals(2, alien.getScore());
    }

    @Test
    public void testGetType() {
        int expected = 3;
        Field field = assertDoesNotThrow(() -> Alien.class.getDeclaredField("Type"));
        field.setAccessible(true);
        assertDoesNotThrow(() -> field.set(alien, expected));
        assertEquals(expected, alien.getType());
    }
}