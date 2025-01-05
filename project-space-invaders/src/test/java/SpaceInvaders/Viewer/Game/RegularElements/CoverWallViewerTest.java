package SpaceInvaders.Viewer.Game.RegularElements;

import SpaceInvaders.GUI.GUI;
import SpaceInvaders.Model.Game.RegularGameElements.CoverWall;
import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CoverWallViewerTest {
    private CoverWallViewer viewer;
    private GUI gui;
    private CoverWall coverWall;

    @BeforeEach
    public void setUp() {
        viewer = new CoverWallViewer();
        gui = Mockito.mock(GUI.class);
        coverWall = Mockito.mock(CoverWall.class);
    }

    @Test
    public void testDrawWithHealthAbove75() {
        Position position = new Position(10, 20);
        Mockito.when(coverWall.getPosition()).thenReturn(position);
        Mockito.when(coverWall.getHealth()).thenReturn(76);

        viewer.draw(gui, coverWall);

        Mockito.verify(gui).drawElement(position, '\u00d2', "#F83B3A");
    }

    @Test
    public void testDrawWithHealthAbove50() {
        Position position = new Position(10, 20);
        Mockito.when(coverWall.getPosition()).thenReturn(position);
        Mockito.when(coverWall.getHealth()).thenReturn(75);

        viewer.draw(gui, coverWall);

        Mockito.verify(gui).drawElement(position, '\u00d3', "#F83B3A");
    }

    @Test
    public void testDrawWithHealthAbove25() {
        Position position = new Position(10, 20);
        Mockito.when(coverWall.getPosition()).thenReturn(position);
        Mockito.when(coverWall.getHealth()).thenReturn(50);

        viewer.draw(gui, coverWall);

        Mockito.verify(gui).drawElement(position, '\u00d5', "#F83B3A");
    }

    @Test
    public void testDrawWithHealthAbove0() {
        Position position = new Position(10, 20);
        Mockito.when(coverWall.getPosition()).thenReturn(position);
        Mockito.when(coverWall.getHealth()).thenReturn(25);

        viewer.draw(gui, coverWall);

        Mockito.verify(gui).drawElement(position, '\u00d4', "#F83B3A");
    }

    @Test
    public void testDrawWithHealth0() {
        Position position = new Position(10, 20);
        Mockito.when(coverWall.getPosition()).thenReturn(position);
        Mockito.when(coverWall.getHealth()).thenReturn(0);

        viewer.draw(gui, coverWall);

        Mockito.verify(gui).drawElement(position, '\u00d2', "#F83B3A");
    }
}