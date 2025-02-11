package SpaceInvaders.Viewer.Game;

import SpaceInvaders.GUI.GUI;
import SpaceInvaders.Model.Game.Arena;
import SpaceInvaders.Model.Game.Collectables.*;
import SpaceInvaders.Model.Game.RegularGameElements.*;
import SpaceInvaders.Model.Position;
import SpaceInvaders.Viewer.Game.Collectables.*;
import SpaceInvaders.Viewer.Game.RegularElements.ElementViewer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameViewerTest {
    private GameViewer viewer;
    @Mock
    private GUI gui;
    @Mock
    private Arena arena;

    private static Stream<Arguments> collectableViewerTestValues() {
        return Stream.of(
                Arguments.of(
                        mock(DamageCollectable.class),
                        DamageCollectableViewer.class,
                        DamageCollectable.class,
                        1
                ),
                Arguments.of(
                        mock(GodModeCollectable.class),
                        GodModeCollectableViewer.class,
                        GodModeCollectable.class,
                        1
                ),
                Arguments.of(
                        mock(HealthCollectable.class),
                        HealthCollectableViewer.class,
                        HealthCollectable.class,
                        1
                ),
                Arguments.of(
                        mock(ScoreCollectable.class),
                        ScoreCollectableViewer.class,
                        ScoreCollectable.class,
                        1
                ),
                Arguments.of(
                        mock(MachineGunModeCollectable.class),
                        MachineGunCollectableViewer.class,
                        MachineGunModeCollectable.class,
                        1
                )
        );
    }

    @BeforeEach
    public void setUp() {
        viewer = new GameViewer(arena);
    }

    @Test
    public void testDrawElements() throws Exception {
        Alien alien = Mockito.mock(Alien.class);
        CoverWall coverWall = Mockito.mock(CoverWall.class);
        Wall wall = Mockito.mock(Wall.class);
        Ship ship = Mockito.mock(Ship.class);
        Projectile projectile = Mockito.mock(Projectile.class);
        AlienShip alienShip = Mockito.mock(AlienShip.class);
        GodModeCollectable collectable = Mockito.mock(GodModeCollectable.class);

        when(arena.getAliens()).thenReturn(Collections.singletonList(alien));
        when(arena.getCoverWalls()).thenReturn(Collections.singletonList(coverWall));
        when(arena.getWalls()).thenReturn(Collections.singletonList(wall));
        when(arena.getShip()).thenReturn(ship);
        when(arena.getProjectiles()).thenReturn(Collections.singletonList(projectile));
        when(arena.getAlienShip()).thenReturn(alienShip);
        when(arena.getActiveCollectable()).thenReturn(collectable);

        viewer.draw(gui, 300);
        assertEquals(0, viewer.getAlienCharChoice());
        assertEquals(0, viewer.getLastCharChange());

        viewer.draw(gui, 301);
        assertEquals(1, viewer.getAlienCharChoice());
        assertEquals(301, viewer.getLastCharChange());

        viewer.draw(gui, 602);
        assertEquals(0, viewer.getAlienCharChoice());
        assertEquals(602, viewer.getLastCharChange());

        verify(gui, times(3)).clear();
        verify(gui, times(3)).refresh();
    }

    @ParameterizedTest
    @CsvSource({
            "NORMAL_MODE, 0, NORMAL_MODE, 0",
            "GOD_MODE, 1, NORMAL_MODE, 0",
            "NORMAL_MODE, 0, SCORE_2X, 1",
            "GOD_MODE, 1, SCORE_2X, 1"
    })
    public void testDrawGameHUD(ShipMode shipMode, int expectedShip, AlienMode alienMode, int expectedAlien) {
        when(arena.getScore()).thenReturn(500);
        when(arena.getRound()).thenReturn(1);

        Ship ship = Mockito.mock(Ship.class);
        when(ship.getShipMode()).thenReturn(shipMode);
        when(ship.getHealth()).thenReturn(100);
        when(arena.getShip()).thenReturn(ship);

        Alien alien = Mockito.mock(Alien.class);
        when(alien.getAlienMode()).thenReturn(alienMode);
        when(arena.getAliens()).thenReturn(Collections.singletonList(alien));

        assertDoesNotThrow(() -> viewer.draw(gui, 1000));

        verify(gui).drawText(new Position(5, 3), "SCORE = ", "#F8F8FF");
        verify(gui).drawText(new Position(15, 3), "500", "#F8F8FF");
        verify(gui).drawText(new Position(55, 3), "HEALTH = ", "#F8F8FF");
        verify(gui).drawText(new Position(65, 3), "100", "#F8F8FF");
        verify(gui).drawText(new Position(5, 5), "ROUND ", "#F8F8FF");
        verify(gui).drawText(new Position(11, 5), "1", "#F8F8FF");
        verify(gui, times(expectedShip)).drawText(new Position(55, 5), String.valueOf(ship.getShipMode()), "#F8F8FF");
        verify(gui, times(expectedAlien)).drawText(new Position(55, 5), String.valueOf(arena.getAliens().get(0).getAlienMode()), "#F8F8FF");
    }

    @ParameterizedTest
    @MethodSource("collectableViewerTestValues")
    public void testCollectableViewerCreation(
            Collectable collectable,
            Class<? extends ElementViewer> viewerClass,
            Class<? extends Collectable> collectableClass,
            int expectedConstructors
    ) {

        Ship ship = Mockito.mock(Ship.class);
        when(ship.getHealth()).thenReturn(100);
        when(arena.getShip()).thenReturn(ship);
        when(arena.getActiveCollectable()).thenReturn(collectable);

        MockedConstruction<? extends ElementViewer> mockedViewer =
                mockConstruction(viewerClass);
        assertDoesNotThrow(() -> viewer.draw(gui, 1000));

        assertEquals(expectedConstructors, mockedViewer.constructed().size());
        ElementViewer constructedViewer = mockedViewer.constructed().getFirst();
        verify(constructedViewer).draw(gui, collectableClass.cast(collectable));
        mockedViewer.close();
    }

    @Test
    public void testDrawCollectableUnknown() {
        Ship ship = Mockito.mock(Ship.class);
        when(ship.getHealth()).thenReturn(100);
        when(arena.getShip()).thenReturn(ship);
        when(arena.getActiveCollectable()).thenReturn(new UnknownCollectable());
        MockedConstruction<UnknownCollectableViewer> mockedViewer =
                mockConstruction(UnknownCollectableViewer.class);
        assertDoesNotThrow(() -> viewer.draw(gui, 1000));
        assertEquals(0, mockedViewer.constructed().size());
        mockedViewer.close();
    }


    private static class UnknownCollectable extends Collectable {
        public UnknownCollectable() {
            super(new Position(0, 0), null);
        }

        @Override
        public void execute() {

        }
    }

    private static class UnknownCollectableViewer implements ElementViewer<UnknownCollectable> {
        @Override
        public void draw(GUI gui, UnknownCollectable collectable) {

        }
    }
}