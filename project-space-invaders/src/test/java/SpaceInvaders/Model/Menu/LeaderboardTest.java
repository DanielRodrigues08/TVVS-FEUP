package SpaceInvaders.Model.Menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class LeaderboardTest {

    private Leaderboard leaderboard;

    @BeforeEach
    public void setUp() throws IOException {
        leaderboard = new Leaderboard();
    }

    @Test
    public void testLeaderboardFilePath() {
        assertDoesNotThrow(() -> new Leaderboard());
    }

    @Test
    public void testSortByScore() {
        leaderboard.text = Arrays.asList("Player1 100", "Player2 200", "Player3 150");
        leaderboard.sortByScore();
        assertEquals(Arrays.asList("Player2 200", "Player3 150", "Player1 100"), leaderboard.text);
    }

    @Test
    public void testSortByScoreWithSameScores() {
        leaderboard.text = Arrays.asList("Player1 100", "Player2 100", "Player3 100");
        leaderboard.sortByScore();
        assertEquals(Arrays.asList("Player1 100", "Player2 100", "Player3 100"), leaderboard.text);
    }

    @Test
    public void testSortByScoreWithEmptyList() {
        leaderboard.text = Arrays.asList();
        leaderboard.sortByScore();
        assertTrue(leaderboard.text.isEmpty());
    }

    @Test
    public void testGetText() {
        assertTrue(leaderboard.getText().contains("Pedro 3000"));
    }
}