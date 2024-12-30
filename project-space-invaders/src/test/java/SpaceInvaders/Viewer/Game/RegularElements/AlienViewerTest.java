package SpaceInvaders.Viewer.Game.RegularElements;

import SpaceInvaders.GUI.GUI;
import SpaceInvaders.Model.Game.RegularGameElements.Alien;
import SpaceInvaders.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.times;

public class AlienViewerTest {
    private GUI gui;
    private Alien alien;

    @BeforeEach
    void setUp() {
        gui = Mockito.mock(GUI.class);
        alien = Mockito.mock(Alien.class);
    }

    @Test
    void testDrawWithCharChoice0() {
        AlienViewer viewer = new AlienViewer(0);
        Position position = new Position(10, 20);
        Mockito.when(alien.getPosition()).thenReturn(position);
        Mockito.when(alien.getType()).thenReturn(1);

        viewer.draw(gui, alien);

        Mockito.verify(gui).drawElement(position, '\u00cc', "#EBDF64");
    }

    @Test
    void testDrawWithCharChoice1() {
        AlienViewer viewer = new AlienViewer(1);
        Position position = new Position(10, 20);
        Mockito.when(alien.getPosition()).thenReturn(position);
        Mockito.when(alien.getType()).thenReturn(1);

        viewer.draw(gui, alien);

        Mockito.verify(gui).drawElement(position, '\u00cd', "#EBDF64");
    }

    @Test
    void testDrawWithCharChoice2() {
        AlienViewer viewer = new AlienViewer(2);
        Position position = new Position(10, 20);
        Mockito.when(alien.getPosition()).thenReturn(position);
        Mockito.when(alien.getType()).thenReturn(1);

        viewer.draw(gui, alien);

        Mockito.verify(gui, times(0)).drawElement(position, '\u00cd', "#EBDF64");
    }
}