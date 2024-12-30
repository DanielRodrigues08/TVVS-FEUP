package SpaceInvaders.Viewer.Game.Collectables;

import SpaceInvaders.GUI.GUI;
import SpaceInvaders.Model.Game.Collectables.MachineGunModeCollectable;
import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class MachineGunCollectableViewerTest {
    private MachineGunCollectableViewer viewer;
    private GUI gui;
    private MachineGunModeCollectable collectable;

    @BeforeEach
    void setUp() {
        viewer = new MachineGunCollectableViewer();
        gui = Mockito.mock(GUI.class);
        collectable = Mockito.mock(MachineGunModeCollectable.class);
    }

    @Test
    void testDraw() {
        Position position = new Position(10, 20);
        Mockito.when(collectable.getPosition()).thenReturn(position);

        viewer.draw(gui, collectable);

        Mockito.verify(gui).drawElement(position, '\u00c9', "#B0E0E6");
    }
}