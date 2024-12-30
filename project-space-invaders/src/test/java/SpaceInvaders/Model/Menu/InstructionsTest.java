package SpaceInvaders.Model.Menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InstructionsTest {


    @Test
    void testInstructionsFilePath() {
        assertDoesNotThrow(() -> new Instructions());
    }

    @Test
    void testGetText() throws IOException {
        Instructions instructions = new Instructions();
        List<String> text = instructions.getText();
        assertTrue(text.contains("How to Play:"));
    }
}