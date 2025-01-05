package SpaceInvaders.Viewer.Menu;

import SpaceInvaders.GUI.GUI;
import SpaceInvaders.Model.Menu.GameOverMenu;
import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class GameOverMenuViewerTest {
    private GameOverMenuViewer viewer;
    private GUI gui;
    private GameOverMenu menu;

    @BeforeEach
    public void setUp() {
        menu = Mockito.mock(GameOverMenu.class);
        viewer = new GameOverMenuViewer(menu);
        gui = Mockito.mock(GUI.class);
    }

    @Test
    public void testDrawElements() {
        when(menu.getScore()).thenReturn(1000);
        when(menu.getUsername()).thenReturn("Player1");
        when(menu.getNumberOptions()).thenReturn(2);
        when(menu.getOption(0)).thenReturn("Option1");
        when(menu.getOption(1)).thenReturn("Option2");
        when(menu.isSelected(0)).thenReturn(true);
        when(menu.isSelected(1)).thenReturn(false);

        viewer.drawElements(gui, 1000);

        verify(gui).drawText(new Position(35, 10), "GAME OVER", "#006400");
        verify(gui).drawText(new Position(5, 4), "SCORE: 1000", "#fffafa");
        verify(gui).drawText(new Position(5, 5), "PLAYER NAME: Player1", "#fffafa");
        verify(gui).drawText(new Position(35, 13), "->Option1", "#900020");
        verify(gui).drawText(new Position(35, 16), "Option2", "#fffafa");
    }
}