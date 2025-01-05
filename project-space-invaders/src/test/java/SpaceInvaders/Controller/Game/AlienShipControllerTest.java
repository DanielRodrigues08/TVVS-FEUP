package SpaceInvaders.Controller.Game;

import SpaceInvaders.Controller.Sound.SoundManager;
import SpaceInvaders.Model.Game.Arena;
import SpaceInvaders.Model.Game.RegularGameElements.Alien;
import SpaceInvaders.Model.Game.RegularGameElements.AlienShip;
import SpaceInvaders.Model.Game.RegularGameElements.Projectile;
import SpaceInvaders.Model.Position;
import SpaceInvaders.Model.Sound.Sound_Options;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AlienShipControllerTest {

    private static final int WIDTH = 10;
    private static final int HEIGHT = 10;
    private Arena arenaSpy;
    private AlienShipController alienShipController;
    private MockedStatic<SoundManager> soundManagerMock;
    private SoundManager mockInstance;

    static public Stream<Arguments> canMoveAlienShipValues() {
        return Stream.of(
                Arguments.of(new Position(1, 1), false),
                Arguments.of(new Position(2, 1), true),
                Arguments.of(new Position(WIDTH - 2, 1), false),
                Arguments.of(new Position(WIDTH - 3, 1), true)
        );
    }

    private static Stream<Arguments> stepTestProvider() {
        AlienShip mockAlienShip = mock(AlienShip.class);

        return Stream.of(
                // Generate case
                Arguments.of(
                        60001L,              // time
                        10000L,             // lastAppearance
                        0L,                 // lastMovementTime
                        null,               // alienShip
                        1,                  // expectedGenerateCalls
                        0,                  // expectedMoveCalls
                        60001L,             // expectedLastAppearance
                        0L                  // expectedLastMovementTime
                ),
                // Move case
                Arguments.of(
                        10101L,             // time
                        0L,                 // lastAppearance
                        10000L,             // lastMovementTime
                        mockAlienShip,      // alienShip
                        0,                  // expectedGenerateCalls
                        1,                  // expectedMoveCalls
                        0L,                 // expectedLastAppearance
                        10101L              // expectedLastMovementTime
                ),
                // No action case
                Arguments.of(
                        10100L,             // time
                        0L,                 // lastAppearance
                        10000L,             // lastMovementTime
                        mockAlienShip,      // alienShip
                        0,                  // expectedGenerateCalls
                        0,                  // expectedMoveCalls
                        0L,                 // expectedLastAppearance
                        10000L              // expectedLastMovementTime
                ),
                // Null alien ship case
                Arguments.of(
                        10100L,             // time
                        0L,                 // lastAppearance
                        0L,                 // lastMovementTime
                        null,               // alienShip
                        0,                  // expectedGenerateCalls
                        0,                  // expectedMoveCalls
                        0L,                 // expectedLastAppearance
                        0L                  // expectedLastMovementTime
                ),
                Arguments.of(
                        6000L,             // time
                        1000L,                 // lastAppearance
                        0L,                 // lastMovementTime
                        null,               // alienShip
                        0,                  // expectedGenerateCalls
                        0,                  // expectedMoveCalls
                        1000L,                 // expectedLastAppearance
                        0L                  // expectedLastMovementTime
                ),
                Arguments.of(
                        50000L,             // time
                        0L,                 // lastAppearance
                        0L,                 // lastMovementTime
                        null,              // alienShip
                        0,                 // expectedGenerateCalls
                        0,                 // expectedMoveCalls
                        0L,                // expectedLastAppearance
                        0L                 // expectedLastMovementTime
                ),
                // Just above boundary
                Arguments.of(
                        50001L,            // time
                        0L,                // lastAppearance
                        0L,                // lastMovementTime
                        null,              // alienShip
                        1,                 // expectedGenerateCalls
                        0,                 // expectedMoveCalls
                        50001L,            // expectedLastAppearance
                        0L                 // expectedLastMovementTime
                ),
                // Testing addition scenario
                Arguments.of(
                        100000L,           // time
                        49999L,            // lastAppearance
                        0L,                // lastMovementTime
                        null,              // alienShip
                        1,                 // expectedGenerateCalls
                        0,                 // expectedMoveCalls
                        100000L,           // expectedLastAppearance
                        0L                 // expectedLastMovementTime
                ),
                // Move existing ship
                Arguments.of(
                        10101L,            // time
                        0L,                // lastAppearance
                        10000L,            // lastMovementTime
                        mockAlienShip,     // alienShip
                        0,                 // expectedGenerateCalls
                        1,                 // expectedMoveCalls
                        0L,                // expectedLastAppearance
                        10101L             // expectedLastMovementTime
                ),
                Arguments.of(
                        60000L,             // time
                        5000L,             // lastAppearance (60000 - 5000 = 55000 > 50000)
                        0L,                // lastMovementTime
                        null,              // alienShip
                        1,                 // expectedGenerateCalls (should generate)
                        0,                 // expectedMoveCalls
                        60000L,            // expectedLastAppearance
                        0L                 // expectedLastMovementTime
                ),
                // Addition operation test case (would pass if addition used instead of subtraction)
                Arguments.of(
                        5000L,             // time
                        60000L,            // lastAppearance (5000 + 60000 > 50000 but 5000 - 60000 < 50000)
                        0L,                // lastMovementTime
                        null,              // alienShip
                        0,                 // expectedGenerateCalls (should NOT generate)
                        0,                 // expectedMoveCalls
                        60000L,            // expectedLastAppearance
                        0L                 // expectedLastMovementTime
                ),
                // Operation order test
                Arguments.of(
                        100000L,           // time
                        40000L,            // lastAppearance (100000 - 40000 = 60000 > 50000)
                        0L,                // lastMovementTime
                        null,              // alienShip
                        1,                 // expectedGenerateCalls
                        0,                 // expectedMoveCalls
                        100000L,           // expectedLastAppearance
                        0L                 // expectedLastMovementTime
                ),
                // Negative time difference
                Arguments.of(
                        40000L,            // time
                        90000L,            // lastAppearance (40000 - 90000 = -50000 < 50000)
                        0L,                // lastMovementTime
                        null,              // alienShip
                        0,                 // expectedGenerateCalls
                        0,                 // expectedMoveCalls
                        90000L,            // expectedLastAppearance
                        0L                 // expectedLastMovementTime
                )
        );
    }

    @BeforeEach
    public void setUp() {
        Arena arena = new Arena(WIDTH, HEIGHT);
        arenaSpy = spy(arena);
        alienShipController = new AlienShipController(arenaSpy);
        soundManagerMock = mockStatic(SoundManager.class);
        mockInstance = mock(SoundManager.class);
        soundManagerMock.when(SoundManager::getInstance).thenReturn(mockInstance);
    }

    @AfterEach
    public void tearDown() {
        soundManagerMock.close();
    }

    @Test
    public void testGenerateAlienShip() {
        arenaSpy.setAlienShip(null);

        alienShipController.generateAlienShip();

        assertEquals(AlienShip.class, arenaSpy.getAlienShip().getClass());
        verify(mockInstance, times(1)).playSound(Sound_Options.ALIEN_SHIP_HIGH);
        verify(mockInstance, times(1)).playSound(Sound_Options.ALIEN_SHIP_LOW);
    }

    @ParameterizedTest
    @MethodSource("canMoveAlienShipValues")
    public void testCanMoveAlienShip(Position position, boolean expected) {
        AlienShip alienShipMock = mock(AlienShip.class);
        doReturn(alienShipMock).when(arenaSpy).getAlienShip();
        when(alienShipMock.getPosition()).thenReturn(position);

        boolean result = alienShipController.canMoveAlienShip();

        assertEquals(expected, result);
    }

    @Test
    public void testRemoveAlienShipTestNotDestroyed() {
        var alienShip = mock(AlienShip.class);
        when(alienShip.isDestroyed()).thenReturn(false);
        arenaSpy.setAlienShip(alienShip);

        alienShipController.removeAlienShip();

        assertNotNull(arenaSpy.getAlienShip());
    }

    @Test
    public void testRemoveNullAlienShip() {
        alienShipController.removeAlienShip();

        assertNull(arenaSpy.getAlienShip());
    }

    @Test
    public void testRemoveAlienShipTestDestroyed() {
        var alienShip = mock(AlienShip.class);
        when(alienShip.isDestroyed()).thenReturn(true);
        arenaSpy.setAlienShip(alienShip);

        alienShipController.removeAlienShip();

        assertNull(arenaSpy.getAlienShip());
        verify(mockInstance, times(1)).playSound(Sound_Options.DESTRUCTION);
        verify(mockInstance, times(1)).stopSound(Sound_Options.ALIEN_SHIP_HIGH);
        verify(mockInstance, times(1)).stopSound(Sound_Options.ALIEN_SHIP_LOW);
    }

    @Test
    public void testMoveShipTrue() {
        Position initPosition = new Position(2, 1);
        int movementDirection = 1;
        Position expectedPosition = new Position(initPosition.getX() + movementDirection, initPosition.getY());
        AlienShip alienShip = new AlienShip(initPosition, 0, 0, movementDirection);
        arenaSpy.setAlienShip(alienShip);
        var alienShipControllerSpy = spy(alienShipController);
        doReturn(true).when(alienShipControllerSpy).canMoveAlienShip();

        alienShipController.moveAlienShip();

        assertEquals(expectedPosition, alienShip.getPosition());
    }

    @Test
    public void testMoveShipFalse() {
        var alienShip = mock(AlienShip.class);
        when(alienShip.getPosition()).thenReturn(new Position(1, 1));

        arenaSpy.setAlienShip(alienShip);
        var alienShipControllerSpy = spy(alienShipController);
        doReturn(false).when(alienShipControllerSpy).canMoveAlienShip();

        alienShipController.moveAlienShip();

        assertNull(arenaSpy.getAlienShip());
        verify(mockInstance, times(1)).stopSound(Sound_Options.ALIEN_SHIP_HIGH);
        verify(mockInstance, times(1)).stopSound(Sound_Options.ALIEN_SHIP_LOW);
    }

    @ParameterizedTest
    @CsvSource({"1, 1, 0, 1", "2, 1, 1, 0"})
    public void testHitByProjectile(int initialHealth, int damage, int expectedHealth, int expectedScore) {
        AlienShip alienShip = new AlienShip(new Position(1, 1), initialHealth, 1, 1);
        arenaSpy.setAlienShip(alienShip);
        arenaSpy.increaseScore(-arenaSpy.getScore());

        var projectile = mock(Projectile.class);
        var alien = mock(Alien.class);
        when(projectile.getElement()).thenReturn(alien);
        when(alien.getDamagePerShot()).thenReturn(damage);

        alienShipController.hitByProjectile(alienShip, projectile);

        assertEquals(expectedHealth, alienShip.getHealth());
        assertEquals(expectedScore, arenaSpy.getScore());
    }

    @ParameterizedTest
    @MethodSource("stepTestProvider")
    void testStep(long time, long lastAppearance, long lastMovementTime,
                  AlienShip alienShip, int expectedGenerateCalls,
                  int expectedMoveCalls, long expectedLastAppearance,
                  long expectedLastMovementTime) throws IOException {

        var alienShipControllerSpy = spy(alienShipController);
        doNothing().when(alienShipControllerSpy).moveAlienShip();

        alienShipControllerSpy.setLastAppearance(lastAppearance);
        alienShipControllerSpy.setLastMovementTime(lastMovementTime);
        arenaSpy.setAlienShip(alienShip);

        alienShipControllerSpy.step(null, null, time);

        verify(alienShipControllerSpy, times(expectedGenerateCalls)).generateAlienShip();
        verify(alienShipControllerSpy, times(expectedMoveCalls)).moveAlienShip();
        assertEquals(expectedLastAppearance, alienShipControllerSpy.getLastAppearance());
        assertEquals(expectedLastMovementTime, alienShipControllerSpy.getLastMovementTime());
    }
}