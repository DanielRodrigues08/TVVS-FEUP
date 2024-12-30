package SpaceInvaders.Viewer.Game.RegularElements;

import SpaceInvaders.GUI.GUI;
import SpaceInvaders.Model.Game.RegularGameElements.AlienShip;
import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class AlienShipViewerTest {
    private AlienShipViewer viewer;
    private GUI gui;
    private AlienShip alienShip;

    @BeforeEach
    void setUp() {
        viewer = new AlienShipViewer();
        gui = Mockito.mock(GUI.class);
        alienShip = Mockito.mock(AlienShip.class);
    }

    @Test
    void testDraw() {
        Position position = new Position(10, 20);
        Mockito.when(alienShip.getPosition()).thenReturn(position);

        viewer.draw(gui, alienShip);

        Mockito.verify(gui).drawElement(position, '\u00e0', "#DC143C");
    }
}