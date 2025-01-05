package SpaceInvaders.Model.Sound;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SoundTest {
    private static final String TEST_FILENAME = "test.wav";
    @Mock
    private Clip mockClip;
    @Mock
    private AudioInputStream mockAudioStream;
    @Mock
    private Path mockPath;
    @Mock
    private File mockFile;
    private Sound sound;
    private MockedStatic<AudioSystem> audioSystem;
    private MockedStatic<Paths> paths;

    @BeforeEach
    public void setUp() {
        audioSystem = mockStatic(AudioSystem.class);
        paths = mockStatic(Paths.class);

        paths.when(() -> Paths.get(TEST_FILENAME)).thenReturn(mockPath);
        when(mockPath.toFile()).thenReturn(mockFile);
        audioSystem.when(() -> AudioSystem.getAudioInputStream(any(File.class)))
                .thenReturn(mockAudioStream);
        audioSystem.when(() -> AudioSystem.getClip())
                .thenReturn(mockClip);

        sound = new Sound(TEST_FILENAME);

    }

    @Test
    public void testPlay() {
        when(mockClip.isRunning()).thenReturn(false);

        sound.play();

        verify(mockClip).setFramePosition(0);
        verify(mockClip).start();
    }

    @Test
    public void testPlayInterruptedException() {
        when(mockClip.isRunning()).thenReturn(true);
        Thread.currentThread().interrupt();

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> sound.play());

        assertInstanceOf(InterruptedException.class, exception.getCause());
        assertFalse(Thread.interrupted());
    }

    @Test
    public void testPlayWhileRunning() {
        when(mockClip.isRunning()).thenReturn(true);

        sound.play();

        verify(mockClip).stop();
        verify(mockClip).setFramePosition(0);
        verify(mockClip).start();
    }

    @Test
    public void testPlayContinuously() {
        sound.playContinuously();

        verify(mockClip).setFramePosition(0);
        verify(mockClip).start();
        verify(mockClip).loop(Clip.LOOP_CONTINUOUSLY);
    }

    @Test
    public void testStop() {
        sound.stop();
        verify(mockClip).stop();
    }

    @Test
    public void testResumePlaying() {
        sound.resumePlaying();

        verify(mockClip).start();
        verify(mockClip).loop(Clip.LOOP_CONTINUOUSLY);
    }

    @Test
    public void testIsPlaying() {
        when(mockClip.isRunning()).thenReturn(true);
        assertTrue(sound.isPlaying());

        when(mockClip.isRunning()).thenReturn(false);
        assertFalse(sound.isPlaying());
    }

    @Test
    public void testGetterSetter() {
        Clip newClip = mock(Clip.class);
        sound.setSound(newClip);
        assertEquals(newClip, sound.getSound());
    }

    @Test
    public void testConstructorError() {
        audioSystem.when(() -> AudioSystem.getAudioInputStream(any(File.class)))
                .thenThrow(new UnsupportedAudioFileException());

        assertThrows(RuntimeException.class, () -> new Sound(TEST_FILENAME));
    }

    @AfterEach
    public void tearDown() {
        audioSystem.close();
        paths.close();
    }
}