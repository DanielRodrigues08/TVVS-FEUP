package SpaceInvaders.Model.Game.RegularGameElements;

import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AlienShipTest {

    private AlienShip alienShip;

    @BeforeEach
    public void setUp() {
        alienShip = new AlienShip(new Position(5, 5), 100, 50, 1);
    }

    @Test
    public void testGetScore() {
        assertEquals(50, alienShip.getScore());
    }

    @Test
    public void testGetMovementDirection() {
        assertEquals(1, alienShip.getMovementDirection());
    }

    @Test
    public void testGetPosition() {
        assertEquals(new Position(5, 5), alienShip.getPosition());
    }

    @Test
    public void testGetHealth() {
        assertEquals(100, alienShip.getHealth());
    }

    @Test
    public void testSetHealth() {
        alienShip.setHealth(80);
        assertEquals(80, alienShip.getHealth());
    }
}