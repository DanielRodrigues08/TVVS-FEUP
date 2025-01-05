package SpaceInvaders.Controller.Game;

import SpaceInvaders.Controller.Sound.SoundManager;
import SpaceInvaders.Model.Game.Arena;
import SpaceInvaders.Model.Game.RegularGameElements.AttackingElement;
import SpaceInvaders.Model.Game.RegularGameElements.Projectile;
import SpaceInvaders.Model.Game.RegularGameElements.Ship;
import SpaceInvaders.Model.Game.RegularGameElements.ShipMode;
import SpaceInvaders.Model.Position;
import SpaceInvaders.Model.Sound.Sound_Options;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ShipControllerTest {

    private static final int WIDTH = 1080;
    private static final int HEIGHT = 1080;
    private Arena arena;
    private ShipController shipController;
    private MockedStatic<SoundManager> soundManagerMock;
    private SoundManager mockInstance;

    public static int[][] valuesShipOutOfBounds() {
        return new int[][]{
                {0, 0},
                {WIDTH - 1, 0},
                {0, HEIGHT - 1},
                {WIDTH - 1, HEIGHT - 1},
                {0, HEIGHT - 2},
                {WIDTH - 1, HEIGHT - 2},
                {WIDTH - 2, HEIGHT - 1},
                {WIDTH - 2, 0}
        };
    }


    private static Stream<Arguments> stepValues() {
        return Stream.of(
                Arguments.of(null, 100, 0, 0, new Position(WIDTH / 2, HEIGHT - 2), 0, 0, 0, ShipMode.NORMAL_MODE),
                Arguments.of(new KeyStroke(KeyType.ArrowLeft), 100, 50, 0, new Position(WIDTH / 2, HEIGHT - 2), 0, 50, 0, ShipMode.NORMAL_MODE),
                Arguments.of(new KeyStroke(KeyType.ArrowLeft), 101, 50, 0, new Position(WIDTH / 2 - 1, HEIGHT - 2), 0, 101, 0, ShipMode.NORMAL_MODE),
                Arguments.of(new KeyStroke(KeyType.ArrowRight), 100, 50, 0, new Position(WIDTH / 2, HEIGHT - 2), 0, 50, 0, ShipMode.NORMAL_MODE),
                Arguments.of(new KeyStroke(KeyType.ArrowRight), 101, 50, 0, new Position(WIDTH / 2 + 1, HEIGHT - 2), 0, 101, 0, ShipMode.NORMAL_MODE),
                Arguments.of(new KeyStroke(KeyType.ArrowUp), 151, 0, 75, new Position(WIDTH / 2, HEIGHT - 2), 1, 0, 151, ShipMode.MACHINE_GUN_MODE),
                Arguments.of(new KeyStroke(KeyType.ArrowUp), 150, 0, 75, new Position(WIDTH / 2, HEIGHT - 2), 0, 0, 75, ShipMode.MACHINE_GUN_MODE),
                Arguments.of(new KeyStroke(KeyType.ArrowUp), 601, 0, 300, new Position(WIDTH / 2, HEIGHT - 2), 1, 0, 601, ShipMode.NORMAL_MODE),
                Arguments.of(new KeyStroke(KeyType.ArrowUp), 600, 0, 300, new Position(WIDTH / 2, HEIGHT - 2), 0, 0, 300, ShipMode.NORMAL_MODE)
        );
    }

    @BeforeEach
    public void setup() {
        arena = new Arena(WIDTH, HEIGHT);
        arena.setProjectiles(new ArrayList<>());
        arena.setShip(new Ship(new Position(WIDTH / 2, HEIGHT - 2), 100, 10));
        shipController = new ShipController(arena);

        soundManagerMock = mockStatic(SoundManager.class);
        mockInstance = mock(SoundManager.class);
        soundManagerMock.when(SoundManager::getInstance).thenReturn(mockInstance);
    }

    @AfterEach
    public void tearDown() {
        soundManagerMock.close();
    }

    @Test
    public void setMovementTimeTest() {
        int expectedMovementTime = 100;

        shipController.setMovementTime(expectedMovementTime);
        assertEquals(expectedMovementTime, shipController.getMovementTime());
    }

    @Test
    public void setShootingTimeTest() {
        int expectedShootingTime = 100;

        shipController.setShootingTime(expectedShootingTime);
        assertEquals(expectedShootingTime, shipController.getShootingTime());
    }

    @ParameterizedTest
    @MethodSource(value = "valuesShipOutOfBounds")
    public void moveShipOutOfBoundsTest(int[] data) {
        assertFalse(shipController.canMoveShip(new Position(data[0], data[1])));
    }

    @Test
    public void moveShipInBoundsTest() {
        assertTrue(shipController.canMoveShip(new Position(WIDTH - 2, HEIGHT - 2)));
    }

    @Test
    public void moveShipLeftOutBoundsTest() {
        arena.getShip().setPosition(new Position(1, HEIGHT - 2));
        Position expectedPosition = new Position(1, HEIGHT - 2);

        shipController.moveLeft();

        assertEquals(expectedPosition, arena.getShip().getPosition());
    }

    @Test
    public void moveShipLeftInBoundsTest() {
        arena.getShip().setPosition(new Position(WIDTH - 1, HEIGHT - 2));
        Position expectedPosition = new Position(WIDTH - 2, HEIGHT - 2);

        shipController.moveLeft();

        assertEquals(expectedPosition, arena.getShip().getPosition());
    }

    @Test
    public void moveShipRightOutBoundsTest() {
        arena.getShip().setPosition(new Position(WIDTH - 1, HEIGHT - 2));
        Position expectedPosition = new Position(WIDTH - 1, HEIGHT - 2);

        shipController.moveRight();

        assertEquals(expectedPosition, arena.getShip().getPosition());
    }

    @Test
    public void moveShipRightInBoundsTest() {
        arena.getShip().setPosition(new Position(1, HEIGHT - 2));
        Position expectedPosition = new Position(2, HEIGHT - 2);

        shipController.moveRight();

        assertEquals(expectedPosition, arena.getShip().getPosition());
    }

    @Test
    public void shootProjectileTest() {
        int initialProjectileCount = arena.getProjectiles().size();
        int expectedProjectileCount = initialProjectileCount + 1;
        Position expectedProjectilePosition = new Position(WIDTH / 2, HEIGHT - 2);


        shipController.shootProjectile();

        assertEquals(expectedProjectileCount, arena.getProjectiles().size());
        assertEquals(expectedProjectilePosition, arena.getProjectiles().get(0).getPosition());

        verify(mockInstance).playSound(Sound_Options.LASER);
    }

    @Test
    public void hitByProjectileTest() {
        int damage = 10;

        var projectileMock = mock(Projectile.class);
        var attackingElementMock = mock(AttackingElement.class);
        when(attackingElementMock.getDamagePerShot()).thenReturn(damage);
        when(projectileMock.getElement()).thenReturn(attackingElementMock);


        int expectedHealth = arena.getShip().getHealth() - damage;

        shipController.hitByProjectile(projectileMock);

        assertEquals(expectedHealth, arena.getShip().getHealth());
    }

    @ParameterizedTest
    @MethodSource("stepValues")
    public void stepTest(KeyStroke key, long time, long movementTime, long shootingTime, Position expectedPosition, int expectedProjectiles, long expectedMovementTime, long expectedShootingTime, ShipMode shipMode) throws IOException {
        arena.getShip().setShipMode(shipMode);
        shipController.setMovementTime(movementTime);
        shipController.setShootingTime(shootingTime);

        shipController.step(null, key, time);

        assertEquals(expectedPosition, arena.getShip().getPosition());
        assertEquals(expectedProjectiles, arena.getProjectiles().size());
        assertEquals(expectedMovementTime, shipController.getMovementTime());
        assertEquals(expectedShootingTime, shipController.getShootingTime());
    }
}