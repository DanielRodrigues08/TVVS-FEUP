package SpaceInvaders.Viewer.Menu;

import SpaceInvaders.GUI.GUI;
import SpaceInvaders.Model.Menu.PauseMenu;
import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class PauseMenuViewerTest {
    private PauseMenuViewer viewer;
    private GUI gui;
    private PauseMenu menu;

    @BeforeEach
    void setUp() {
        menu = Mockito.mock(PauseMenu.class);
        viewer = new PauseMenuViewer(menu);
        gui = Mockito.mock(GUI.class);
    }

    @Test
    void testDrawElements() {
        when(menu.getNumberOptions()).thenReturn(2);
        when(menu.getOption(0)).thenReturn("Resume");
        when(menu.getOption(1)).thenReturn("Quit");
        when(menu.isSelected(0)).thenReturn(true);
        when(menu.isSelected(1)).thenReturn(false);

        viewer.drawElements(gui, 1000);

        verify(gui).drawText(new Position(35, 10), "PAUSE MENU", "#006400");
        verify(gui).drawText(new Position(35, 13), "->Resume", "#900020");
        verify(gui).drawText(new Position(35, 16), "Quit", "#fffafa");
    }
}