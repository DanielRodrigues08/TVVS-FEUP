package SpaceInvaders.Model.Game.Collectables;

import SpaceInvaders.Model.Game.RegularGameElements.Ship;
import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class HealthCollectableTest {

    private HealthCollectable healthCollectable;
    private Ship ship;

    @Mock
    private Position position;

    @BeforeEach
    public void setUp() {
        ship = new Ship(position, 100, 0);
        healthCollectable = new HealthCollectable(position, ship);
    }

    @Test
    public void testExecute() {
        int expectedHealth = 100;
        ship.decreaseHealth(50);

        healthCollectable.execute();

        assertEquals(expectedHealth, ship.getHealth());
    }
}