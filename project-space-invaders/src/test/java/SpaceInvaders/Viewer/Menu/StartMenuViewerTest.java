package SpaceInvaders.Viewer.Menu;

import SpaceInvaders.GUI.GUI;
import SpaceInvaders.Model.Menu.StartMenu;
import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class StartMenuViewerTest {
    private StartMenuViewer viewer;
    private GUI gui;
    private StartMenu menu;

    @BeforeEach
    public void setUp() {
        menu = Mockito.mock(StartMenu.class);
        viewer = new StartMenuViewer(menu);
        gui = Mockito.mock(GUI.class);
    }

    @Test
    public void testDrawElements() {
        when(menu.getNumberOptions()).thenReturn(2);
        when(menu.getOption(0)).thenReturn("Start Game");
        when(menu.getOption(1)).thenReturn("Exit");
        when(menu.isSelected(0)).thenReturn(true);
        when(menu.isSelected(1)).thenReturn(false);

        viewer.drawElements(gui, 1000);

        verify(gui).drawText(new Position(35, 10), "START MENU", "#006400");
        verify(gui).drawText(new Position(35, 13), "->Start Game", "#900020");
        verify(gui).drawText(new Position(35, 16), "Exit", "#fffafa");
    }

    @Test
    public void testGetReferenceX(){
        int expectedX = 35;
        assertEquals(expectedX, viewer.getReference_x());
    }

    @Test
    public void testGetReferenceY(){
        int expectedY = 13;
        assertEquals(expectedY, viewer.getReference_y());
    }
}