package SpaceInvaders.Viewer.Game.Collectables;

import SpaceInvaders.GUI.GUI;
import SpaceInvaders.Model.Game.Collectables.ScoreCollectable;
import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ScoreCollectableViewerTest {
    private ScoreCollectableViewer viewer;
    private GUI gui;
    private ScoreCollectable collectable;

    @BeforeEach
    void setUp() {
        viewer = new ScoreCollectableViewer();
        gui = Mockito.mock(GUI.class);
        collectable = Mockito.mock(ScoreCollectable.class);
    }

    @Test
    void testDraw() {
        Position position = new Position(10, 20);
        Mockito.when(collectable.getPosition()).thenReturn(position);

        viewer.draw(gui, collectable);

        Mockito.verify(gui).drawElement(position, '$', "#009000");
    }
}