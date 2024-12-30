/*package SpaceInvaders.Viewer.Game;

import SpaceInvaders.GUI.GUI;
import SpaceInvaders.Model.Game.Arena;
import SpaceInvaders.Model.Game.Collectables.GodModeCollectable;
import SpaceInvaders.Model.Game.RegularGameElements.*;
import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Collections;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GameViewerTest {
    private GameViewer viewer;
    private GUI gui;
    private Arena arena;

    @BeforeEach
    void setUp() {
        arena = Mockito.mock(Arena.class);
        viewer = new GameViewer(arena);
        gui = Mockito.mock(GUI.class);
    }

    @Test
    void testDrawElements() throws Exception {
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

        viewer.draw(gui, 1000);

        verify(gui).clear();
        verify(gui).refresh();
    }


    @Test
    void testChangeCharNull() {
        viewer.drawElements(gui, 1000);
        assert viewer.getAlienCharChoice() == 1;
        assert viewer.getLastCharChange() == 1000;
    }

    @Test
    void testDrawGameHUD() throws IOException {
        Ship ship = Mockito.mock(Ship.class);
        when(arena.getShip()).thenReturn(ship);
        when(ship.getHealth()).thenReturn(100);
        when(arena.getScore()).thenReturn(500);
        when(arena.getRound()).thenReturn(1);

        viewer.draw(gui, 1000);

        verify(gui).drawText(new Position(5, 3), "SCORE = ", "#F8F8FF");
        verify(gui).drawText(new Position(15, 3), "500", "#F8F8FF");
        verify(gui).drawText(new Position(55, 3), "HEALTH = ", "#F8F8FF");
        verify(gui).drawText(new Position(65, 3), "100", "#F8F8FF");
        verify(gui).drawText(new Position(5, 5), "ROUND ", "#F8F8FF");
        verify(gui).drawText(new Position(11, 5), "1", "#F8F8FF");
    }

    @Test
    void testDrawCollectable() throws IOException {
        GodModeCollectable collectable = Mockito.mock(GodModeCollectable.class);
        when(arena.getActiveCollectable()).thenReturn(collectable);

        viewer.draw(gui, 1000);

        verify(gui).clear();
        verify(gui).refresh();
    }
}*/