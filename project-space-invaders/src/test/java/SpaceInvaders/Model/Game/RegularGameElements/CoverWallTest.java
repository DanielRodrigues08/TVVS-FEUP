package SpaceInvaders.Model.Game.RegularGameElements;

import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CoverWallTest {

    private CoverWall coverWall;

    @BeforeEach
    void setUp() {
        coverWall = new CoverWall(new Position(10, 20), 100);
    }

    @Test
    void testGetPosition() {
        assertEquals(new Position(10, 20), coverWall.getPosition());
    }

    @Test
    void testGetHealth() {
        assertEquals(100, coverWall.getHealth());
    }

    @Test
    void testSetHealth() {
        coverWall.setHealth(80);
        assertEquals(80, coverWall.getHealth());
    }
}