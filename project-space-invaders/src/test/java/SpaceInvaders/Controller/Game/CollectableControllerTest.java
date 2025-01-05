package SpaceInvaders.Controller.Game;

import SpaceInvaders.Controller.Sound.SoundManager;
import SpaceInvaders.Model.Game.Arena;
import SpaceInvaders.Model.Game.Collectables.Collectable;
import SpaceInvaders.Model.Game.Collectables.DamageCollectable;
import SpaceInvaders.Model.Game.RegularGameElements.*;
import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CollectableControllerTest {

    private Arena arenaSpy;
    private CollectableController collectableController;
    private MockedStatic<SoundManager> soundManagerMock;
    private SoundManager mockInstance;

    private static Stream<Arguments> stepTestProvider() {
        Alien normalAlien = mock(Alien.class);
        when(normalAlien.getAlienMode()).thenReturn(AlienMode.NORMAL_MODE);

        Alien powerAlien = mock(Alien.class);
        when(powerAlien.getAlienMode()).thenReturn(AlienMode.SCORE_2X);

        Ship normalShip = mock(Ship.class);
        when(normalShip.getShipMode()).thenReturn(ShipMode.NORMAL_MODE);

        Ship powerShip = mock(Ship.class);
        when(powerShip.getShipMode()).thenReturn(ShipMode.MACHINE_GUN_MODE);

        return Stream.of(
                Arguments.of(40001, 20000, 0L, null, List.of(), normalShip,
                        40001, 0, 1, 0, 0),

                Arguments.of(40000, 20000, 0L, null, List.of(), normalShip,
                        20000, 0, 0, 0, 0),

                Arguments.of(1000, 0L, 849, new DamageCollectable(null, null, 1), List.of(),
                        normalShip, 0, 1000, 0, 1, 0),

                Arguments.of(1000, 0L, 850, new DamageCollectable(null, null, 1), List.of(),
                        normalShip, 0, 850, 0, 0, 0),

                Arguments.of(39901, 20000, 0L, null,
                        List.of(powerAlien), powerShip, 20000, 0, 0, 0, 1),

                Arguments.of(39900, 20000, 0L, null,
                        List.of(normalAlien), normalShip, 20000, 0, 0, 0, 0),

                Arguments.of(39900, 20000, 0L, null,
                        List.of(powerAlien), normalShip, 20000, 0, 0, 0, 0),

                Arguments.of(39900, 20000, 0L, null,
                        List.of(normalAlien), powerShip, 20000, 0, 0, 0, 0),

                Arguments.of(39901, 20000, 0L, null,
                        List.of(normalAlien), normalShip, 20000, 0, 0, 0, 0)
        );
    }

    @BeforeEach
    public void setUp() {
        Arena arena = new Arena(10, 10);
        arenaSpy = spy(arena);
        collectableController = new CollectableController(arenaSpy);
        soundManagerMock = mockStatic(SoundManager.class);
        mockInstance = mock(SoundManager.class);
        soundManagerMock.when(SoundManager::getInstance).thenReturn(mockInstance);
    }

    @AfterEach
    public void tearDown() {
        soundManagerMock.close();
    }

    @Test
    public void generateCollectableTest() {
        arenaSpy.setActiveCollectable(null);
        doReturn(List.of(1)).when(arenaSpy).getFreeArenaColumns();

        collectableController.generateCollectable();

        assertNotNull(arenaSpy.getActiveCollectable());
    }

    @Test
    public void testMoveCollectable() {
        Position initialPosition = new Position(10, 10);
        Position expectedPosition = new Position(10, 11);
        Collectable<Ship> collectable = new DamageCollectable(initialPosition, null, 1);
        doReturn(collectable).when(arenaSpy).getActiveCollectable();

        collectableController.moveCollectable();

        assertEquals(expectedPosition, arenaSpy.getActiveCollectable().getPosition());
    }

    @Test
    public void endCollectableEffectTest() {
        arenaSpy.setShip(new Ship(null, 0, 0));
        arenaSpy.getShip().setShipMode(ShipMode.MACHINE_GUN_MODE);
        doReturn(List.of(new Alien(null, 0, 0, 0, AlienState.ATTACKING, 0))).when(arenaSpy).getAliens();
        arenaSpy.getAliens().getFirst().setAlienMode(AlienMode.SCORE_2X);
        doReturn(List.of(1)).when(arenaSpy).getFreeArenaColumns();


        collectableController.endCollectableEffect();

        assertEquals(ShipMode.NORMAL_MODE, arenaSpy.getShip().getShipMode());
        assertEquals(AlienMode.NORMAL_MODE, arenaSpy.getAliens().getFirst().getAlienMode());
    }

    @ParameterizedTest
    @MethodSource("stepTestProvider")
    public void stepTest(long time, long generateCollectableTime, long movementTime, Collectable collectable, List<Alien> aliens, Ship ship, long expectedGenerateCollectableTime, long expectedMovementTime, int expectedNumGenerate, int expectedNumMovement, int expectedNumEnd) throws IOException {
        var collectableControllerSpy = spy(collectableController);

        Field field = assertDoesNotThrow(() -> collectableControllerSpy.getClass().getDeclaredField("movementTime"));
        field.setAccessible(true);

        doNothing().when(collectableControllerSpy).generateCollectable();
        doNothing().when(collectableControllerSpy).moveCollectable();
        doNothing().when(collectableControllerSpy).endCollectableEffect();

        arenaSpy.setShip(ship);
        arenaSpy.setAliens(aliens);
        arenaSpy.setActiveCollectable(collectable);
        collectableControllerSpy.setGenerateCollectableTime(generateCollectableTime);
        collectableControllerSpy.setMovementTime(movementTime);

        collectableControllerSpy.step(null, null, time);

        assertEquals(expectedGenerateCollectableTime, collectableControllerSpy.getGenerateCollectableTime());
        long movementTime1 = assertDoesNotThrow(() -> field.getLong(collectableControllerSpy));
        assertEquals(expectedMovementTime, movementTime1);
        Mockito.verify(collectableControllerSpy, Mockito.times(expectedNumGenerate)).generateCollectable();
        Mockito.verify(collectableControllerSpy, Mockito.times(expectedNumMovement)).moveCollectable();
        Mockito.verify(collectableControllerSpy, Mockito.times(expectedNumEnd)).endCollectableEffect();
    }
}