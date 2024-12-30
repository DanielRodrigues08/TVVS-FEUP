package SpaceInvaders.Model.Game.Collectables;

import SpaceInvaders.Model.Game.RegularGameElements.Ship;
import SpaceInvaders.Model.Game.RegularGameElements.ShipMode;
import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class DamageCollectableTest {

    private Ship ship;
    @Mock
    private Position position;

    @BeforeEach
    void setUp() {
        ship = new Ship(position, 0, 0);
    }

    @ParameterizedTest
    @CsvSource({
            "2, DAMAGE_2X",
            "3, DAMAGE_3X",
            "4, DAMAGE_4X",
            "5, DAMAGE_5X",
            "10, DAMAGE_10X",
            "11, NORMAL_MODE"
    })
    void testConstructor(int multiplier, ShipMode shipMode) {
        DamageCollectable damageCollectable = new DamageCollectable(position, ship, multiplier);

        damageCollectable.execute();

        assertEquals(multiplier, damageCollectable.getMultiplier());
        assertEquals(shipMode, damageCollectable.getAttackingElement().getShipMode());
    }


}