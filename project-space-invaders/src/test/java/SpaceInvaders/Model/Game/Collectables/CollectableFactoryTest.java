package SpaceInvaders.Model.Game.Collectables;

import SpaceInvaders.Model.Position;
import SpaceInvaders.Model.Game.RegularGameElements.Alien;
import SpaceInvaders.Model.Game.RegularGameElements.Ship;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CollectableFactoryTest {

    private Position position;
    private Ship ship;
    private List<Alien> aliens;

    @BeforeEach
    void setUp() {
        position = mock(Position.class);
        ship = mock(Ship.class);
        aliens = List.of(mock(Alien.class), mock(Alien.class));
    }

    @Test
    void testCreateHealthCollectable() {
        CollectableFactory<Ship> factory = new CollectableFactory<>(position, CollectableType.HEALTH, 0, ship);
        Collectable collectable = factory.createCollectable();
        assertInstanceOf(HealthCollectable.class, collectable);
    }

    @Test
    void testCreateDamageCollectable() {
        CollectableFactory<Ship> factory = new CollectableFactory<>(position, CollectableType.DAMAGE, 2, ship);
        Collectable collectable = factory.createCollectable();
        assertInstanceOf(DamageCollectable.class, collectable);
        assertEquals(2, ((DamageCollectable) collectable).getMultiplier());
    }

    @Test
    void testCreateScoreCollectable() {
        CollectableFactory<List<Alien>> factory = new CollectableFactory<>(position, CollectableType.SCORE, 3, aliens);
        Collectable collectable = factory.createCollectable();
        assertTrue(collectable instanceof ScoreCollectable);
        assertEquals(3, ((ScoreCollectable) collectable).getMultiplier());
    }

    @Test
    void testCreateMachineGunModeCollectable() {
        CollectableFactory<Ship> factory = new CollectableFactory<>(position, CollectableType.MACHINE_GUN_MODE, 0, ship);
        Collectable collectable = factory.createCollectable();
        assertInstanceOf(MachineGunModeCollectable.class, collectable);
    }

    @Test
    void testCreateGodModeCollectable() {
        CollectableFactory<Ship> factory = new CollectableFactory<>(position, CollectableType.GOD_MODE, 0, ship);
        Collectable collectable = factory.createCollectable();
        assertInstanceOf(GodModeCollectable.class, collectable);
    }
}