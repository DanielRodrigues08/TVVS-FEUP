package SpaceInvaders.Viewer.Game.RegularElements;

import SpaceInvaders.GUI.GUI;
import SpaceInvaders.Model.Game.RegularGameElements.Ship;
import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ShipViewerTest {
    private ShipViewer viewer;
    private GUI gui;
    private Ship ship;

    @BeforeEach
    public void setUp() {
        viewer = new ShipViewer();
        gui = Mockito.mock(GUI.class);
        ship = Mockito.mock(Ship.class);
    }

    @Test
    public void testDraw() {
        Position position = new Position(10, 20);
        Mockito.when(ship.getPosition()).thenReturn(position);

        viewer.draw(gui, ship);

        Mockito.verify(gui).drawElement(position, '\u00c0', "#42E9F4");
    }
}