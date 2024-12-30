package SpaceInvaders.Viewer.Game.Collectables;

import SpaceInvaders.GUI.GUI;
import SpaceInvaders.Model.Game.Collectables.GodModeCollectable;
import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class GodModeCollectableViewerTest {
    private GodModeCollectableViewer viewer;
    private GUI gui;
    private GodModeCollectable collectable;

    @BeforeEach
    void setUp() {
        viewer = new GodModeCollectableViewer();
        gui = Mockito.mock(GUI.class);
        collectable = Mockito.mock(GodModeCollectable.class);
    }

    @Test
    void testDraw() {
        Position position = new Position(10, 20);
        Mockito.when(collectable.getPosition()).thenReturn(position);

        viewer.draw(gui, collectable);

        Mockito.verify(gui).drawElement(position, '\u00C7', "#FFFF00");
    }
}