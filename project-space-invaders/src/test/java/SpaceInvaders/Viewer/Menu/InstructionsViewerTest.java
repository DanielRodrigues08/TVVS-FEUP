package SpaceInvaders.Viewer.Menu;

import SpaceInvaders.GUI.GUI;
import SpaceInvaders.Model.Menu.Instructions;
import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;

public class InstructionsViewerTest {
    private InstructionsViewer viewer;
    private GUI gui;
    private Instructions instructions;

    @BeforeEach
    public void setUp() {
        instructions = Mockito.mock(Instructions.class);
        viewer = new InstructionsViewer(instructions);
        gui = Mockito.mock(GUI.class);
    }

    @Test
    public void testDrawElements() throws IOException {
        when(instructions.getText()).thenReturn(List.of("Some instruction text"));

        viewer.drawElements(gui, 1000);

        verify(gui).drawText(new Position(1, 1), "INSTRUCTIONS", "#006400");
        verify(gui).drawText(new Position(1, 3), "Some instruction text", "#fffafa");
    }

    @Test
    public void testDrawFileText() throws IOException {
        when(instructions.getText()).thenReturn(List.of("Some instruction text"));

        viewer.drawFileText(gui);

        verify(gui).drawText(new Position(1, 3), "Some instruction text", "#fffafa");
    }
}