package SpaceInvaders.Model.Game.RegularGameElements;

import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WallTest {

    private Wall wall;

    @BeforeEach
    public void setUp() {
        wall = new Wall(new Position(10, 20));
    }

    @Test
    public void testGetPosition() {
        assertEquals(new Position(10, 20), wall.getPosition());
    }
}