package SpaceInvaders.Controller.Game;

import SpaceInvaders.Controller.Sound.SoundManager;
import SpaceInvaders.Game;
import SpaceInvaders.Model.Game.Arena;
import SpaceInvaders.Model.Game.ArenaModifier;
import SpaceInvaders.Model.Game.RegularGameElements.Alien;
import SpaceInvaders.Model.Game.RegularGameElements.Projectile;
import SpaceInvaders.Model.Game.RegularGameElements.Ship;
import SpaceInvaders.Model.Position;
import SpaceInvaders.Model.Sound.Sound_Options;
import com.googlecode.lanterna.input.KeyStroke;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AlienControllerTest {
    private static final int ARENA_WIDTH = 74;
    private static final int ARENA_HEIGHT = 50;
    @Mock
    private Arena arena;
    private AlienController controller;

    private static Stream<Arguments> testMovementDirectionTransitions() {
        return Stream.of(
                Arguments.of(MovementDirection.LEFT, false, MovementDirection.DOWN),
                Arguments.of(MovementDirection.LEFT, true, MovementDirection.LEFT),
                Arguments.of(MovementDirection.RIGHT, false, MovementDirection.LEFT),
                Arguments.of(MovementDirection.RIGHT, true, MovementDirection.RIGHT),
                Arguments.of(MovementDirection.DOWN, false, MovementDirection.RIGHT)
        );
    }

    private static Stream<Arguments> valuesTestCanMoveAlien() {
        return Stream.of(
                Arguments.of(new Position(3, 0), MovementDirection.LEFT, false),
                Arguments.of(new Position(4, 0), MovementDirection.LEFT, true),
                Arguments.of(new Position(70, 0), MovementDirection.RIGHT, false),
                Arguments.of(new Position(69, 0), MovementDirection.RIGHT, true),
                Arguments.of(new Position(ARENA_WIDTH - 1, 0), MovementDirection.DOWN, true)
        );
    }

    private static Stream<Arguments> valuesTestCanMoveAliens() {
        Alien a1 = mock(Alien.class);
        Alien a2 = mock(Alien.class);

        when(a1.getPosition()).thenReturn(new Position(3, 0));
        when(a2.getPosition()).thenReturn(new Position(4, 0));

        return Stream.of(
                Arguments.of(List.of(a1, a2), false),
                Arguments.of(List.of(a2), true)
        );
    }

    private static Stream<Arguments> valuesTestMoveAlien() {
        return Stream.of(
                Arguments.of(new Position(3, 0), MovementDirection.LEFT, new Position(2, 0)),
                Arguments.of(new Position(4, 0), MovementDirection.RIGHT, new Position(5, 0)),
                Arguments.of(new Position(3, 0), MovementDirection.DOWN, new Position(3, 1))
        );
    }

    private static Stream<Arguments> valuesTestShootProjectile() {
        Alien alien1 = mock(Alien.class);
        Alien alien2 = mock(Alien.class);

        return Stream.of(
                Arguments.of(List.of(alien1), 1),
                Arguments.of(List.of(alien1, alien2), 1),
                Arguments.of(List.of(), 0)
        );
    }

    private static Stream<Arguments> testStepValues() {
        return Stream.of(
                Arguments.of(900L, 100L, 1000L, 0, 0, 100L, 1000L),
                Arguments.of(901L, 100L, 1000L, 1, 0, 901L, 1000L),
                Arguments.of(400L, 10L, 100L, 0, 0, 10L, 100L),
                Arguments.of(401L, 10L, 100L, 0, 1, 10L, 401L)
        );
    }

    @BeforeEach
    void setUp() {
        when(arena.getWidth()).thenReturn(ARENA_WIDTH);
        when(arena.getHeight()).thenReturn(ARENA_HEIGHT);
        controller = new AlienController(arena);
    }

    @ParameterizedTest
    @MethodSource("valuesTestCanMoveAlien")
    void testCanMoveAlien(Position position, MovementDirection direction, boolean expected) {
        Alien alien = mock(Alien.class);
        when(alien.getPosition()).thenReturn(position);
        controller.setMovementDirection(direction);
        assertEquals(expected, controller.canMoveAlien(alien));
    }

    @ParameterizedTest
    @MethodSource("valuesTestCanMoveAliens")
    void testCanMoveAliens(List<Alien> aliens, boolean expected) {
        controller.setMovementDirection(MovementDirection.LEFT);
        when(arena.getAliens()).thenReturn(aliens);
        assertEquals(expected, controller.canMoveAliens());
    }

    @ParameterizedTest
    @MethodSource("valuesTestMoveAlien")
    void testMoveAlien(Position position, MovementDirection direction, Position expected) {
        Alien alien = new Alien(position, 0, 0, 0, null, 0);
        controller.setMovementDirection(direction);
        controller.moveAlien(alien);
        assertEquals(expected, alien.getPosition());
    }

    @Test
    void testMoveAliens() {
        Alien alien1 = new Alien(new Position(3, 0), 0, 0, 0, null, 0);
        Alien alien2 = new Alien(new Position(4, 0), 0, 0, 0, null, 0);
        when(arena.getAliens()).thenReturn(List.of(alien1, alien2));
        controller.setMovementDirection(MovementDirection.RIGHT);

        controller.moveAliens();

        assertEquals(new Position(4, 0), alien1.getPosition());
        assertEquals(new Position(5, 0), alien2.getPosition());
    }

    @ParameterizedTest
    @MethodSource("valuesTestShootProjectile")
    void testShootProjectile(List<Alien> attackingAliens, int numProjectiles) {
        Field arenaModifierField = assertDoesNotThrow(() -> GameController.class.getDeclaredField("arenaModifier"));
        arenaModifierField.setAccessible(true);

        ArenaModifier arenaModifier = mock(ArenaModifier.class);
        assertDoesNotThrow(() -> arenaModifierField.set(controller, arenaModifier));

        when(arena.getAttackingAliens()).thenReturn(attackingAliens);

        controller.shootProjectile();
        verify(controller.getArenaModifier(), times(numProjectiles)).addProjectile(any());
    }

    @Test
    void testHitByProjectile() {
        Alien alien = mock(Alien.class);
        Projectile projectile = mock(Projectile.class);
        Ship ship = mock(Ship.class);
        when(projectile.getElement()).thenReturn(ship);
        when(ship.getDamagePerShot()).thenReturn(1);
        when(alien.getScore()).thenReturn(1);

        controller.hitByProjectile(alien, projectile);

        verify(alien).decreaseHealth(1);
        verify(arena).increaseScore(1);
    }

    @Test
    void testRemoveDestroyedAliens() {
        Field arenaModifierField = assertDoesNotThrow(() -> GameController.class.getDeclaredField("arenaModifier"));
        arenaModifierField.setAccessible(true);

        ArenaModifier arenaModifier = mock(ArenaModifier.class);
        assertDoesNotThrow(() -> arenaModifierField.set(controller, arenaModifier));

        MockedStatic<SoundManager> soundManager = mockStatic(SoundManager.class);
        SoundManager mockSoundManager = mock(SoundManager.class);
        soundManager.when(SoundManager::getInstance).thenReturn(mockSoundManager);

        Alien alien1 = mock(Alien.class);
        Alien alien2 = mock(Alien.class);
        when(alien1.isDestroyed()).thenReturn(true);
        when(alien2.isDestroyed()).thenReturn(false);

        List<Alien> aliens = List.of(alien1, alien2);
        when(arena.getAliens()).thenReturn(aliens);

        controller.removeDestroyedAliens();

        verify(mockSoundManager).playSound(Sound_Options.DESTRUCTION);
        verify(arenaModifier).removeAlien(alien1);
        verify(arenaModifier, never()).removeAlien(alien2);

        soundManager.close();
    }

    @ParameterizedTest
    @CsvSource({"5, 100", "6, 50"})
    void testMovementCooldown(int round, long expected) {
        when(arena.getRound()).thenReturn(round);
        assertEquals(expected, controller.movementCoolDown());
    }

    @ParameterizedTest
    @CsvSource({"7, 200", "8, 100"})
    void testShootingCooldown(int round, long expected) {
        when(arena.getRound()).thenReturn(round);
        assertEquals(expected, controller.shootingCoolDown());
    }

    @ParameterizedTest
    @MethodSource("testStepValues")
    void testStep(long time, long lastShotTime, long lastMovementTime,
                  int numShootExpected, int numMovementExpected, long shootTimeExpected, long movementTimeExpected) {

        var controllerSpy = spy(controller);

        controllerSpy.setLastMovementTime(lastMovementTime);
        controllerSpy.setLastShotTime(lastShotTime);

        when(arena.getRound()).thenReturn(1);

        controllerSpy.step(mock(Game.class), mock(KeyStroke.class), time);


        verify(controllerSpy, times(numShootExpected)).shootProjectile();
        verify(controllerSpy, times(numMovementExpected)).moveAliens();
        assertEquals(shootTimeExpected, controllerSpy.getLastShotTime());
        assertEquals(movementTimeExpected, controllerSpy.getLastMovementTime());
    }


    @ParameterizedTest
    @MethodSource("testMovementDirectionTransitions")
    void testUpdateMovementDirection(MovementDirection initial, boolean canMove,
                                     MovementDirection expected) {
        var alienControllerSpy = spy(controller);
        doReturn(canMove).when(alienControllerSpy).canMoveAliens();

        alienControllerSpy.setMovementDirection(initial);
        alienControllerSpy.updateMovementDirection();

        assertEquals(expected, alienControllerSpy.getMovementDirection());
    }

    @Test
    void testSetGetLastMovementTime() {
        long expected = 100;
        controller.setLastMovementTime(expected);
        assertEquals(expected, controller.getLastMovementTime());
    }

    @Test
    void testSetGetLastShotTime() {
        long expected = 100;
        controller.setLastShotTime(expected);
        assertEquals(expected, controller.getLastShotTime());
    }
}