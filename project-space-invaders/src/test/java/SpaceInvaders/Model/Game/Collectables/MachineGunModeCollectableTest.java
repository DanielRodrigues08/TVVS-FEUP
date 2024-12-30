package SpaceInvaders.Model.Game.Collectables;

import SpaceInvaders.Model.Game.RegularGameElements.Ship;
import SpaceInvaders.Model.Game.RegularGameElements.ShipMode;
import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class MachineGunModeCollectableTest {

    private MachineGunModeCollectable machineGunModeCollectable;
    private Ship ship;

    @Mock
    private Position position;

    @BeforeEach
    void setUp() {
        ship = new Ship(position, 0, 0);
        machineGunModeCollectable = new MachineGunModeCollectable(position, ship);
    }

    @Test
    void testExecute() {
        machineGunModeCollectable.execute();

        assertEquals(ShipMode.MACHINE_GUN_MODE, ship.getShipMode());
    }
}