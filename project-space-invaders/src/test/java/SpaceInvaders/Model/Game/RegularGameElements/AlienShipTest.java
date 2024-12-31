package SpaceInvaders.Model.Game.RegularGameElements;

import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AlienShipTest {

    private AlienShip alienShip;

    @BeforeEach
    void setUp() {
        alienShip = new AlienShip(new Position(5, 5), 100, 50, 1);
    }

    @Test
    void testGetScore() {
        assertEquals(50, alienShip.getScore());
    }

    @Test
    void testGetMovementDirection() {
        assertEquals(1, alienShip.getMovementDirection());
    }

    @Test
    void testGetPosition() {
        assertEquals(new Position(5, 5), alienShip.getPosition());
    }

    @Test
    void testGetHealth() {
        assertEquals(100, alienShip.getHealth());
    }

    @Test
    void testSetHealth() {
        alienShip.setHealth(80);
        assertEquals(80, alienShip.getHealth());
    }
}