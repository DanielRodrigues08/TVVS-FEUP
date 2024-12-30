package SpaceInvaders.GUI;

import SpaceInvaders.Model.Position;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GUILanternaTest {

    private GUILanterna gui;
    private Screen screen;

    @BeforeEach
    void setUp() throws IOException {
        screen = Mockito.mock(Screen.class);
        gui = new GUILanterna(screen);
    }

    @Test
    void testGUILanternaValid() throws IOException, URISyntaxException, FontFormatException {
        GUILanterna guiLanterna = new GUILanterna(10, 10);
        assertInstanceOf(Screen.class, guiLanterna.getScreen());
    }

    @Test
    void testGUILanternaInvalid(){
        assertThrows(Exception.class, () -> new GUILanterna(0, 0));
    }

    @Test
    void testDrawElement() {
        var tgMock = mock(TextGraphics.class);
        when(screen.newTextGraphics()).thenReturn(tgMock);
        Position position = new Position(1, 1);

        gui.drawElement(position, 'X', "#FFFFFF");

        verify(tgMock).putString(1, 2, "X");
    }

    @Test
    void testDrawText() {
        var tgMock = mock(TextGraphics.class);
        when(screen.newTextGraphics()).thenReturn(tgMock);
        Position position = new Position(1, 1);

        gui.drawText(position, "Hello", "#FFFFFF");

        verify(tgMock).putString(1, 1, "Hello");
    }

    @Test
    void testGetNextAction() throws IOException {
        KeyStroke keyStroke = mock(KeyStroke.class);
        when(screen.pollInput()).thenReturn(keyStroke);
        KeyStroke result = gui.getNextAction();
        assertNotNull(result);
        verify(screen).pollInput();
    }

    @Test
    void testClear() {
        gui.clear();
        verify(screen).clear();
    }

    @Test
    void testRefresh() throws IOException {
        gui.refresh();
        verify(screen).refresh();
    }

    @Test
    void testClose() throws IOException {
        gui.close();
        verify(screen).close();
    }
}