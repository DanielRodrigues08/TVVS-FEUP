package SpaceInvaders.Viewer.Menu;

import SpaceInvaders.GUI.GUI;
import SpaceInvaders.Model.Menu.Leaderboard;
import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Mockito.*;

public class LeaderboardViewerTest {
    private LeaderboardViewer viewer;
    private GUI gui;
    private Leaderboard leaderboard;

    @BeforeEach
    void setUp() {
        leaderboard = Mockito.mock(Leaderboard.class);
        viewer = new LeaderboardViewer(leaderboard);
        gui = Mockito.mock(GUI.class);
    }

    @Test
    void testDrawElements() {
        when(leaderboard.getText()).thenReturn(List.of("Player1 - 1000", "Player2 - 900", "Player3 - 800"));

        viewer.drawElements(gui, 1000);

        verify(gui).drawText(new Position(35, 10), "LEADERBOARD", "#006400");
        verify(gui).drawText(new Position(35, 14), "1 - Player1 - 1000", "#fffafa");
        verify(gui).drawText(new Position(35, 15), "2 - Player2 - 900", "#fffafa");
        verify(gui).drawText(new Position(35, 16), "3 - Player3 - 800", "#fffafa");
    }

    @Test
    void testDrawFileText() {
        when(leaderboard.getText()).thenReturn(List.of("Player1 - 1000", "Player2 - 900", "Player3 - 800"));

        viewer.drawFileText(gui);

        verify(gui).drawText(new Position(35, 14), "1 - Player1 - 1000", "#fffafa");
        verify(gui).drawText(new Position(35, 15), "2 - Player2 - 900", "#fffafa");
        verify(gui).drawText(new Position(35, 16), "3 - Player3 - 800", "#fffafa");
    }
}