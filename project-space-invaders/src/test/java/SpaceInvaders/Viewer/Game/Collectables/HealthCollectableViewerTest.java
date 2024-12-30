package SpaceInvaders.Viewer.Game.Collectables;

import SpaceInvaders.GUI.GUI;
import SpaceInvaders.Model.Game.Collectables.HealthCollectable;
import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class HealthCollectableViewerTest {
    private HealthCollectableViewer viewer;
    private GUI gui;
    private HealthCollectable collectable;

    @BeforeEach
    void setUp() {
        viewer = new HealthCollectableViewer();
        gui = Mockito.mock(GUI.class);
        collectable = Mockito.mock(HealthCollectable.class);
    }

    @Test
    void testDraw() {
        Position position = new Position(10, 20);
        Mockito.when(collectable.getPosition()).thenReturn(position);

        viewer.draw(gui, collectable);

        Mockito.verify(gui).drawElement(position, '\u00c1', "#ff0000");
    }
}