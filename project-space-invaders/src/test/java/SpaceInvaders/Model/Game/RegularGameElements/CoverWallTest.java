package SpaceInvaders.Model.Game.RegularGameElements;

import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CoverWallTest {

    private CoverWall coverWall;

    @BeforeEach
    public void setUp() {
        coverWall = new CoverWall(new Position(10, 20), 100);
    }

    @Test
    public void testGetPosition() {
        assertEquals(new Position(10, 20), coverWall.getPosition());
    }

    @Test
    public void testGetHealth() {
        assertEquals(100, coverWall.getHealth());
    }

    @Test
    public void testSetHealth() {
        coverWall.setHealth(80);
        assertEquals(80, coverWall.getHealth());
    }
}