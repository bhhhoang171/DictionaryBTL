import java.awt.Dimension;
import com.sun.deploy.panel.JSmartTextArea;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.Color;

public class TranslateApi extends JFrame {
    private JTextPane translateBox;
    private JSmartTextArea definition;
    private JScrollPane scroll;
    private JScrollPane scroll1;
    private final JLabel en = new JLabel("English");
    private final JLabel vi = new JLabel("Vietnamese");
    private final JButton translateButton = new JButton("Translate");

    public TranslateApi() {
        super("Translate API");
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setLayout(null);
        this.setPreferredSize(new Dimension(600, 400));
        this.setLocation(300, 50);
        this.setUndecorated(true);
        this.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        this.getContentPane().setBackground(Color.decode("#43adc4"));
        this.setVisible(false);
    }

    private void createTranslateBox() {
        en.setFont(new java.awt.Font("Arial", 0, 18));
        en.setBounds(5, 10, 150, 30);
        this.add(en);
        translateBox = new JTextPane();
        translateBox.setFont(new java.awt.Font("Arial", 0, 16));
        scroll = new JScrollPane(translateBox);
        scroll.setBounds(0, 50, 280, 300);
        this.add(scroll);
    }

    private void createDefinitionBox() {
        vi.setFont(new java.awt.Font("Arial", 0, 18));
        vi.setBounds(305, 10, 150, 30);
        this.add(vi);
        definition = new JSmartTextArea();
        definition.setFont(new java.awt.Font("Arial", 0, 16));
        scroll1 = new JScrollPane(definition);
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
        this.pack();
    }

    private String sentenceTranslator(String sentence) {
        try {
            if (sentence.isEmpty()) {
                return "";
            }
            String link = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=vi&dt=t&q=";
            ArrayList<String> s = new ArrayList<String>();
            int d = 0;
            for (int i = 0; i < sentence.length(); ++i) {
                char c = sentence.charAt(i);
                if(c == '.' || c == '?' || c == '!') {
                    s.add(sentence.substring(d, i + 1).trim());
                    d = i + 1;
                }
            }
            s.add(sentence.substring(d, sentence.length()).trim());
            String res = "";
            for (String a : s) {
                URL url = new URL(link + URLEncoder.encode(a, "UTF8"));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                Scanner resScanner = new Scanner(connection.getInputStream(), "UTF8");
                String b = resScanner.nextLine();
                //System.out.println(b);
                b = b.substring(3, b.length() - 12);
                for (int i = 0; i < b.length(); i++) {
                    if (b.charAt(i) == ',') {
                        b = b.substring(0, i) + b.substring(i + 1);
                    }
                }
                int quoteCount = 0;
                for (int i = 0; i < b.length(); i++) {
                    if (b.charAt(i) == '"') {
                        quoteCount++;
                    }
                    if (quoteCount == 2) {
                        b = b.substring(1, i);
                    }
                }
                res += " " + b;
            }
            return res;
        } catch (MalformedURLException e) {
            JOptionPane.showMessageDialog(this, e);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e);
        }
        return "";
    }

    public void runTranslateApi() {
        createTranslateBox();
        createDefinitionBox();
        creatTranslateButton();
        this.setVisible(true);
    }
}
