package SpaceInvaders.Viewer.Game.Collectables;

import SpaceInvaders.GUI.GUI;
import SpaceInvaders.Model.Game.Collectables.DamageCollectable;
import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class DamageCollectableViewerTest {
    private DamageCollectableViewer viewer;
    private GUI gui;
    private DamageCollectable collectable;

    @BeforeEach
    public void setUp() {
        viewer = new DamageCollectableViewer();
        gui = Mockito.mock(GUI.class);
        collectable = Mockito.mock(DamageCollectable.class);
    }

    @Test
    public void testDraw() {
        Position position = new Position(10, 20);
        Mockito.when(collectable.getPosition()).thenReturn(position);

        viewer.draw(gui, collectable);

        Mockito.verify(gui).drawElement(position, '\u00C8', "#FF4500");
    }
}