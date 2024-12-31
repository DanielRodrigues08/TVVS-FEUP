package SpaceInvaders.Model.Game.RegularGameElements;

import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShipTest {

    private Ship ship;

    @BeforeEach
    void setUp() {
        ship = new Ship(new Position(10, 20), 100, 10);
    }

    @Test
    void testGetShipMode() {
        assertEquals(ShipMode.NORMAL_MODE, ship.getShipMode());
    }

    @Test
    void testSetShipMode() {
        ship.setShipMode(ShipMode.DAMAGE_2X);
        assertEquals(ShipMode.DAMAGE_2X, ship.getShipMode());
    }

    @Test
    void testGetMaxHealth() {
        assertEquals(100, ship.getMaxHealth());
    }

    @ParameterizedTest
    @CsvSource({
            "NORMAL_MODE, 10",
            "DAMAGE_2X, 20",
            "DAMAGE_3X, 30",
            "DAMAGE_4X, 40",
            "DAMAGE_5X, 50",
            "DAMAGE_10X, 100",
            "MACHINE_GUN_MODE, 10",
    })
    void testGetDamagePerShot(ShipMode shipMode, int expected) {
        ship.setShipMode(shipMode);
        assertEquals(expected, ship.getDamagePerShot());
    }

    @ParameterizedTest
    @CsvSource({
            "NORMAL_MODE, 90",
            "GOD_MODE, 100",
    })
    void testDecreaseHealth(ShipMode shipMode, int expected) {
        ship.setShipMode(shipMode);
        ship.decreaseHealth(10);
        assertEquals(expected, ship.getHealth());
    }

    @Test
    void testIncreaseDamage() {
        ship.increaseDamage(2);
        assertEquals(20, ship.getDamagePerShot());
    }

    @Test
    void testRestoreHealth() {
        ship.decreaseHealth(50);
        ship.restoreHealth();
        assertEquals(100, ship.getHealth());
    }

    @Test
    void testGetPosition() {
        assertEquals(new Position(10, 20), ship.getPosition());
    }
}