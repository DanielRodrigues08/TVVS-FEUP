package SpaceInvaders.Controller.Game;

import SpaceInvaders.Controller.Sound.SoundManager;
import SpaceInvaders.Model.Game.Arena;
import SpaceInvaders.Model.Game.RegularGameElements.Alien;
import SpaceInvaders.Model.Game.RegularGameElements.AlienShip;
import SpaceInvaders.Model.Game.RegularGameElements.Projectile;
import SpaceInvaders.Model.Position;
import SpaceInvaders.Model.Sound.Sound_Options;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AlienShipControllerTest {

    private static final int WIDTH = 10;
    private static final int HEIGHT = 10;
    private Arena arenaSpy;
    private AlienShipController alienShipController;

    static public Stream<Arguments> canMoveAlienShipValues() {
        return Stream.of(
                Arguments.of(new Position(1, 1), false),
                Arguments.of(new Position(2, 1), true),
                Arguments.of(new Position(WIDTH - 2, 1), false),
                Arguments.of(new Position(WIDTH - 3, 1), true)
        );
    }

    @BeforeEach
    public void setUp() {
        Arena arena = new Arena(WIDTH, HEIGHT);
        arenaSpy = spy(arena);
        alienShipController = new AlienShipController(arenaSpy);
    }

    @Test
    public void generateAlienShipTest() {
        var soundManager = Mockito.mock(SoundManager.class);
        SoundManager.setInstance(soundManager);
        arenaSpy.setAlienShip(null);

        alienShipController.generateAlienShip();

        assertEquals(AlienShip.class, arenaSpy.getAlienShip().getClass());
        verify(soundManager, times(1)).playSound(Sound_Options.ALIEN_SHIP_HIGH);
        verify(soundManager, times(1)).playSound(Sound_Options.ALIEN_SHIP_LOW);

    }

    @ParameterizedTest
    @MethodSource("canMoveAlienShipValues")
    public void canMoveAlienShipTest(Position position, boolean expected) {
        AlienShip alienShipMock = mock(AlienShip.class);
        doReturn(alienShipMock).when(arenaSpy).getAlienShip();
        when(alienShipMock.getPosition()).thenReturn(position);

        boolean result = alienShipController.canMoveAlienShip();

        assertEquals(expected, result);
    }

    @Test
    public void removeAlienShipTestNotDestroyedTest() {
        var alienShip = mock(AlienShip.class);
        when(alienShip.isDestroyed()).thenReturn(false);
        arenaSpy.setAlienShip(alienShip);

        alienShipController.removeAlienShip();

        assertNotNull(arenaSpy.getAlienShip());
    }

    @Test
    public void removeNullAlienShipTest() {
        alienShipController.removeAlienShip();

        assertNull(arenaSpy.getAlienShip());
    }

    @Test
    public void removeAlienShipTestDestroyedTest() {
        var soundManager = Mockito.mock(SoundManager.class);
        SoundManager.setInstance(soundManager);

        var alienShip = mock(AlienShip.class);
        when(alienShip.isDestroyed()).thenReturn(true);
        arenaSpy.setAlienShip(alienShip);

        alienShipController.removeAlienShip();

        assertNull(arenaSpy.getAlienShip());
        verify(soundManager, times(1)).playSound(Sound_Options.DESTRUCTION);
        verify(soundManager, times(1)).stopSound(Sound_Options.ALIEN_SHIP_HIGH);
        verify(soundManager, times(1)).stopSound(Sound_Options.ALIEN_SHIP_LOW);
    }


    @Test
    public void moveShipTrueTest() {
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
    public void moveShipFalseTest() {
        var soundManager = Mockito.mock(SoundManager.class);
        SoundManager.setInstance(soundManager);

        var alienShip = mock(AlienShip.class);
        when(alienShip.getPosition()).thenReturn(new Position(1, 1));

        arenaSpy.setAlienShip(alienShip);
        var alienShipControllerSpy = spy(alienShipController);
        doReturn(false).when(alienShipControllerSpy).canMoveAlienShip();

        alienShipController.moveAlienShip();

        assertNull(arenaSpy.getAlienShip());
        verify(soundManager, times(1)).stopSound(Sound_Options.ALIEN_SHIP_HIGH);
        verify(soundManager, times(1)).stopSound(Sound_Options.ALIEN_SHIP_LOW);
    }

    @Test
    public void hitByProjectileNotDestroyedTest() {
        int initialHealth = 2;
        int damage = 1;
        int expectedHealth = initialHealth - damage;

        AlienShip alienShip = new AlienShip(new Position(1, 1), initialHealth, 1, 1);
        arenaSpy.setAlienShip(alienShip);

        var projectile = mock(Projectile.class);
        var alien = mock(Alien.class);
        when(projectile.getElement()).thenReturn(alien);
        when(alien.getDamagePerShot()).thenReturn(damage);

        alienShipController.hitByProjectile(alienShip, projectile);

        assertEquals(expectedHealth, alienShip.getHealth());
    }

    /*@Test
    public void hitByProjectileDestroyedTest() {
        var soundManager = Mockito.mock(SoundManager.class);
        SoundManager.setInstance(soundManager);

        int initialHealth = 2;
        int damage = 1;

        AlienShip alienShip = new AlienShip(new Position(1, 1), initialHealth, 1, 1);
        arenaSpy.setAlienShip(alienShip);

        var projectile = mock(Projectile.class);
        var alien = mock(Alien.class);
        when(projectile.getElement()).thenReturn(alien);
        when(alien.getDamagePerShot()).thenReturn(damage);

        alienShipController.hitByProjectile(alienShip, projectile);

        assertTrue(alienShip.isDestroyed());
    }*/

    @Test
    public void stepGenerateTest() throws IOException {
        int time = 60001;
        var alienShipControllerSpy = spy(alienShipController);
        alienShipControllerSpy.setLastAppearance(10000);

        alienShipControllerSpy.step(null, null, time);

        verify(alienShipControllerSpy, times(1)).generateAlienShip();
        verify(alienShipControllerSpy, times(0)).moveAlienShip();
        assertEquals(time, alienShipControllerSpy.getLastAppearance());
        assertEquals(0, alienShipControllerSpy.getLastMovementTime());
    }

    @Test
    public void stepMoveTest() throws IOException {
        int time = 10101;
        var alienShipControllerSpy = spy(alienShipController);
        doNothing().when(alienShipControllerSpy).moveAlienShip();
        var alienShip = mock(AlienShip.class);
        arenaSpy.setAlienShip(alienShip);
        alienShipControllerSpy.setLastMovementTime(10000);

        alienShipControllerSpy.step(null, null, time);

        verify(alienShipControllerSpy, times(1)).moveAlienShip();
        verify(alienShipControllerSpy, times(0)).generateAlienShip();
        assertEquals(time, alienShipControllerSpy.getLastMovementTime());
        assertEquals(0, alienShipControllerSpy.getLastAppearance());
    }

    @Test
    public void stepNullTest() throws IOException {
        int time = 10100;
        var alienShipControllerSpy = spy(alienShipController);
        doNothing().when(alienShipControllerSpy).moveAlienShip();
        var alienShip = mock(AlienShip.class);
        arenaSpy.setAlienShip(alienShip);
        alienShipControllerSpy.setLastMovementTime(10000);

        alienShipControllerSpy.step(null, null, time);

        verify(alienShipControllerSpy, times(0)).generateAlienShip();
        verify(alienShipControllerSpy, times(0)).moveAlienShip();
        assertEquals(10000, alienShipControllerSpy.getLastMovementTime());
        assertEquals(0, alienShipControllerSpy.getLastAppearance());
    }

    @Test
    public void stepAlienShipNullTest() throws IOException {
        int time = 10100;
        var alienShipControllerSpy = spy(alienShipController);

        alienShipControllerSpy.step(null, null, time);

        verify(alienShipControllerSpy, times(0)).generateAlienShip();
        verify(alienShipControllerSpy, times(0)).moveAlienShip();
        assertEquals(0, alienShipControllerSpy.getLastMovementTime());
        assertEquals(0, alienShipControllerSpy.getLastAppearance());
    }
}