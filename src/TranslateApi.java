import com.sun.deploy.panel.JSmartTextArea;
import javazoom.jl.decoder.JavaLayerException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Scanner;

public class TranslateApi extends JFrame {
    private JTextPane translateBox;
    private JSmartTextArea definition;
    private final JLabel en = new JLabel("<html><font face='Arial' size=\"+2\">English</font></html>");
    private final JLabel vi = new JLabel("<html><font face='Arial' size=\"+2\">Vietnamese</font></html>");
    private final JButton translateButton = new JButton("Translate");

    public TranslateApi() {
        super("Translate API");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(null);
        this.setPreferredSize(new Dimension(600, 400));
        this.setUndecorated(true);
        this.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        this.pack();
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(Color.decode("#43adc4"));
        this.setVisible(false);
    }

    private void createTranslateBox() {
        en.setBounds(5, 10, 200, 30);
        this.add(en);
        translateBox = new JTextPane();
        translateBox.setFont(new java.awt.Font("Arial", 0, 16));
        JScrollPane scroll = new JScrollPane(translateBox);
        scroll.setBounds(0, 50, 280, 300);
        this.add(scroll);
    }

    private void createDefinitionBox() {
        vi.setBounds(305, 10, 200, 30);
        this.add(vi);
        definition = new JSmartTextArea();
        definition.setFont(new java.awt.Font("Arial", Font.PLAIN, 16));
        JScrollPane scroll1 = new JScrollPane(definition);
        scroll1.setBounds(300, 50, 280, 300);
        this.add(scroll1);
    }

    private void creatTranslateButton() {
        translateButton.setBounds(180, 10, 100, 30);
        translateButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String sentence = translateBox.getText();
                definition.setText(sentenceTranslator(sentence));
            }
        });
        this.add(translateButton);
    }

    private String sentenceTranslator(String sentence) {
        try {
            if (sentence.isEmpty()) {
                return "";
            }
            String link = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=vi&dt=t&q=";
            ArrayList<String> s = new ArrayList<>();
            int d = 0;
            sentence = sentence.replaceAll("\"", "'");
            for (int i = 0; i < sentence.length(); ++i) {
                char c = sentence.charAt(i);
                if (c == '.' || c == '?' || c == '!') {
                    s.add(sentence.substring(d, i + 1).trim());
                    d = i + 1;
                }
            }
            s.add(sentence.substring(d, sentence.length()).trim());
            StringBuilder res = new StringBuilder();
            for (String a : s) {
                if(a.isEmpty()) {
                    break;
                }
                URL url = new URL(link + URLEncoder.encode(a, "UTF8"));
                URLConnection connection =  url.openConnection();
                Scanner sc = new Scanner(connection.getInputStream(), "UTF8");
                String b = sc.nextLine();
                b = b.substring(3, b.length() - 12);
                int quoteCount = 0;
                for (int i = 0; i < b.length(); i++) {
                    if (b.charAt(i) == '"') {
                        quoteCount++;
                    }
                    if (quoteCount == 2) {
                        b = b.substring(1, i);
                    }
                }
                res.append(" ").append(b);
            }
            return res.toString();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e);
        }
        return "";
    }

    void createAudioSystem() {
        final BufferedImage audioImg;
        final JLabel enLabel = new JLabel("EN");
        enLabel.setBounds(475, 10, 20, 30);
        try {
            audioImg = ImageIO.read(new File("icon\\audio.png"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e);
            return;
        }
        final ImageIcon audioIcon = new ImageIcon(audioImg.getScaledInstance(25, 25, Image.SCALE_SMOOTH));
        final JButton enButton = new JButton(audioIcon);
        enButton.setToolTipText("Listen");
        enButton.setBounds(495, 10, 30, 30);
        PronunciationAPI p = new PronunciationAPI();
        enButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    InputStream inp = p.getAudio(translateBox.getText(),"en-uk");
                    p.play(inp);
                } catch (IOException | JavaLayerException ioException) {
                    ioException.printStackTrace();
                }
            }

        });
        this.add(enButton);
        this.add(enLabel);

        final JLabel viLabel = new JLabel("VI");
        viLabel.setBounds(533, 10, 17, 30);
        final JButton viButton = new JButton(audioIcon);
        viButton.setToolTipText("Listen");
        viButton.setBounds(550, 10, 30, 30);
        viButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    InputStream inp = p.getAudio(definition.getText(),"vi");
                    p.play(inp);
                } catch (IOException | JavaLayerException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        this.add(viButton);
        this.add(viLabel);
        this.pack();
    }

    public void runTranslateApi() {
        createTranslateBox();
        createDefinitionBox();
        creatTranslateButton();
        createAudioSystem();
        this.setVisible(true);
    }
}
