package SpaceInvaders.Viewer.Game.RegularElements;

import SpaceInvaders.GUI.GUI;
import SpaceInvaders.Model.Game.RegularGameElements.Projectile;
import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ProjectileViewerTest {
    private ProjectileViewer viewer;
    private GUI gui;
    private Projectile projectile;

    @BeforeEach
    public void setUp() {
        viewer = new ProjectileViewer();
        gui = Mockito.mock(GUI.class);
        projectile = Mockito.mock(Projectile.class);
    }

    @Test
    public void testDraw() {
        Position position = new Position(10, 20);
        Mockito.when(projectile.getPosition()).thenReturn(position);

        viewer.draw(gui, projectile);

        Mockito.verify(gui).drawElement(position, '*', "#5353F1");
    }
}