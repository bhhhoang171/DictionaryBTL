import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class PronunciationAPI {
    private static PronunciationAPI audio;

    public PronunciationAPI() {
    }

    /**
     * Initialize PronunciationAPI.
     *
     * @return PronunciationAPI
     */
    public synchronized static PronunciationAPI getInstance() {
        if (audio == null) {
            audio = new PronunciationAPI();
        }
        return audio;
    }

    /**
     * Get audio from API.
     *
     * @param text           Text need to tranform to audio
     * @param languageOutput Language output
     * @return InputStream
     * @throws IOException
     */
    public InputStream getAudio(String text, String languageOutput) throws IOException {
        URL url = new URL("http://translate.google.com/translate_tts?ie=UTF-8&tl=" + languageOutput + "&client=tw-ob&q="
                + text.replace(" ", "%20"));
        URLConnection urlConn = url.openConnection();
        urlConn.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        InputStream audioSrc = urlConn.getInputStream();
        return new BufferedInputStream(audioSrc);
    }

    /**
     * Play audio.
     *
     * @param sound
     * @throws JavaLayerException
     */
    public void play(InputStream sound) throws JavaLayerException {
        new Player(sound).play();
    }

}