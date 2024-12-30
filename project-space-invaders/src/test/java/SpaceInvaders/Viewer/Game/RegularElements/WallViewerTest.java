package SpaceInvaders.Viewer.Game.RegularElements;

import SpaceInvaders.GUI.GUI;
import SpaceInvaders.Model.Game.RegularGameElements.Wall;
import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class WallViewerTest {
    private WallViewer viewer;
    private GUI gui;
    private Wall wall;

    @BeforeEach
    void setUp() {
        viewer = new WallViewer();
        gui = Mockito.mock(GUI.class);
        wall = Mockito.mock(Wall.class);
    }

    @Test
    void testDraw() {
        Position position = new Position(10, 20);
        Mockito.when(wall.getPosition()).thenReturn(position);

        viewer.draw(gui, wall);

        Mockito.verify(gui).drawElement(position, '\u00d2', "#000080");
    }
}