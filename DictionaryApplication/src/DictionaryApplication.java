import javazoom.jl.decoder.JavaLayerException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class DictionaryApplication extends JFrame {
    private final Dictionary dictionary = new Dictionary();
    private final DictionaryManagement DM = new DictionaryManagement();
    private final JTextField SearchingBox = new JTextField();
    private final JList<String> SearchingResults = new JList<>();
    private final JEditorPane Definition = new JEditorPane();
    private final JTextPane Filename = new JTextPane();
    private Style style;
    private final JLabel Search = new JLabel("<html><font face='Arial' size=\"+2\">Search</font></html>");
    private final JLabel Result = new JLabel("<html><font face='Arial' size=\"+2\">Searching results</font></html>");
    private final JLabel WordsExplain = new JLabel("<html><font face='Arial' size=\"+2\">Definition</font></html>");
    private JPanel main;
    private final DefaultListModel<String> model = new DefaultListModel<>();
    private final java.awt.Font font = new java.awt.Font("Arial", Font.PLAIN, 20);

    /**
     * Intialize Dictionary application.
     */
    public DictionaryApplication() {
        super("Dictionary");
        final Image icon = Toolkit.getDefaultToolkit().getImage(".\\icon\\dict_icon.png");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(null);
        this.setPreferredSize(new Dimension(1200, 900));
        this.setUndecorated(true);
        this.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        this.setIconImage(icon);
        this.getContentPane().setBackground(Color.decode("#43adc4"));
        this.pack();
        this.setLocationRelativeTo(null);
        DM.insertFromFile(dictionary);
        Definition.setContentType("text/html");
        Definition.setEditable(false);
        Definition.setText(information());
        this.setVisible(true);
    }

    /**
     * Create search box and handle event.
     */
    private void createSearchingBox() {
        Filename.setBounds(470, 100, 600, 60);
        Filename.setFont(new java.awt.Font("Arial", Font.PLAIN, 30));
        style = Filename.addStyle("", null);
        StyleConstants.setForeground(style, Color.BLUE);
        Filename.setEditable(false);
        this.add(Filename);
        this.add(Search);
        SearchingBox.setBounds(0, 30, 380, 35);
        SearchingBox.setFont(font);
        Search.setBounds(10, 3, 100, 25);
        SearchingBox.setText("Enter word here");
        SearchingBox.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                String s = SearchingBox.getText();
                if (s.equals("Enter word here")) {
                    SearchingBox.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (SearchingBox.getText().isEmpty()) {
                    SearchingBox.setText("Enter word here");
                }
            }
        });
        this.add(SearchingBox);
    }

    /**
     * Create searching result box + search button and handle event.
     */
    private void createSearchingResults() {
        Result.setBounds(10, 66, 300, 30);
        this.add(Result);
        final BufferedImage img;
        try {
            img = ImageIO.read(new File(".\\icon\\search.png"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e);
            return;
        }
        final ImageIcon searchIcon = new ImageIcon(img.getScaledInstance(25, 25, Image.SCALE_SMOOTH));
        final JButton searchButton = new JButton(searchIcon);
        searchButton.setToolTipText("Search words");
        searchButton.setBounds(380, 30, 70, 35);
        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                model.clear();
                String find_word = SearchingBox.getText();
                Trie T = dictionary.getTrieWord().find(find_word);
                if (T != null) {
                    ArrayList<Word> find_words = T.getListOfWord();
                    for (Word w : find_words) {
                        model.addElement(w.getWord_target());
                    }
                }
            }
        });
        this.add(searchButton);
        SearchingBox.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    model.clear();
                    String find_word = SearchingBox.getText();
                    Trie T = dictionary.getTrieWord().find(find_word);
                    if (T != null) {
                        ArrayList<Word> find_words = T.getListOfWord();
                        for (Word w : find_words) {
                            model.addElement(w.getWord_target());
                        }
                    }
                }
            }
        });
        SearchingResults.setFont(font);
        SearchingResults.setModel(model);
        JScrollPane scroll = new JScrollPane(SearchingResults);
        scroll.setBounds(0, 100, 450, 768);
        this.add(scroll);
    }

    /**
     * Create definition box and handle event.
     */
    private void createDefinition() {
        WordsExplain.setBounds(480, 68, 300, 25);
        this.add(WordsExplain);
        SearchingResults.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        SearchingResults.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selected_index = SearchingResults.getSelectedIndex();
                Trie T = dictionary.getTrieWord().find(SearchingBox.getText());
                if (T != null && selected_index >= 0) {
                    ArrayList<Word> find_words = T.getListOfWord();
                    Word selected_word = find_words.get(selected_index);
                    Definition.setText(selected_word.getWord_explain());
                    try {
                        Filename.getStyledDocument().remove(0, Filename.getStyledDocument().getLength());
                        Filename.getStyledDocument().insertString(0, selected_word.getWord_target(), style);
                    } catch (BadLocationException badLocationException) {
                        badLocationException.printStackTrace();
                    }
                }
            }
        });
        SearchingResults.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    int selected_index = SearchingResults.getSelectedIndex();
                    Trie T = dictionary.getTrieWord().find(SearchingBox.getText());
                    if (T != null && selected_index >= 0) {
                        ArrayList<Word> find_words = T.getListOfWord();
                        Word selected_word = find_words.get(selected_index);
                        Definition.setText(selected_word.getWord_explain());
                        try {
                            Filename.getStyledDocument().remove(0, Filename.getStyledDocument().getLength());
                            Filename.getStyledDocument().insertString(0, selected_word.getWord_target(), style);
                        } catch (BadLocationException badLocationException) {
                            badLocationException.printStackTrace();
                        }
                    }
                }
            }
        });
        JScrollPane scroll1 = new JScrollPane(Definition);
        scroll1.setBounds(470, 160, 600, 708);
        this.add(scroll1);
    }

    /**
     * Create add button and handle event.
     */
    private void createAddButton() {
        final BufferedImage img;
        try {
            img = ImageIO.read(new File(".\\icon\\add.png"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e);
            return;
        }
        ImageIcon addIcon = new ImageIcon(img.getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        final JButton addButton = new JButton(addIcon);
        addButton.setToolTipText("Add a word");
        addButton.setBounds(1110, 350, 50, 50);
        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String target;
                try {
                    target = JOptionPane.showInputDialog("Please insert a word you want to add").trim();
                } catch (Exception er) {
                    return;
                }
                while (target.isEmpty()) {
                    JOptionPane.showMessageDialog(getParent(), "Please insert a word");
                    target = JOptionPane.showInputDialog("Please insert a word you want to add").trim();
                }
                for (int i = 0; i < target.length(); ++i) {
                    char a = target.charAt(i);
                    if (a >= 'a' && a <= 'z') {
                        continue;
                    } else if (a >= 'A' && a <= 'Z') {
                        continue;
                    } else if (a >= '0' && a <= '9') {
                        continue;
                    } else if (a == ' ' || a == '.' || a == '-' || a == '\'' || a == '(' || a == ')') {
                        continue;
                    } else if (a == 'â' || a == 'ê' || a == 'é' || a == 'è' || a == 'ô' || a == 'ö' || a == 'à'
                            || a == 'û' || a == 'ã' || a == 'ə') {
                        continue;
                    } else {
                        JOptionPane.showMessageDialog(getParent(), "This word is not accepted!");
                        return;
                    }
                }
                if (DM.dictionaryLookup(dictionary, target)) {
                    JOptionPane.showMessageDialog(getParent(), "This word is already in dictionary");
                    return;
                }
                JFrame jFrame = new JFrame("Input");
                jFrame.setLayout(null);
                jFrame.setPreferredSize(new Dimension(600, 400));
                jFrame.pack();
                jFrame.setLocationRelativeTo(null);
                jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                jFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        DictionaryApplication.super.setEnabled(true);
                        jFrame.dispose();
                    }
                });
                JTextPane jTextPane = new JTextPane();
                jTextPane.setFont(new java.awt.Font("Time New Roman", Font.PLAIN, 14));
                JScrollPane jScrollPane = new JScrollPane(jTextPane);
                jScrollPane.setBounds(40, 50, 500, 220);
                JLabel jLabel = new JLabel("Explain:");
                jLabel.setFont(font);
                jLabel.setBounds(40, 15, 200, 35);
                jFrame.add(jLabel);
                jFrame.add(jScrollPane);
                JButton okButton = new JButton("OK");
                okButton.setBounds(270, 300, 60, 35);
                String finalTarget = target;
                okButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        String[] explain = jTextPane.getText().split("\\n");
                        if (explain[0].isEmpty() && explain.length == 1) {
                            JOptionPane.showMessageDialog(getParent(), "Explain should not be empty");
                            return;
                        }
                        int output = JOptionPane.showConfirmDialog(getParent(), "Are you sure?", "Confirmation",
                                JOptionPane.YES_NO_OPTION);
                        if (output == JOptionPane.YES_OPTION) {
                            DM.addWord(dictionary, finalTarget, explain);
                            JOptionPane.showMessageDialog(getParent(), "Inserted successfully");
                        }
                        DictionaryApplication.super.setEnabled(true);
                        jFrame.dispose();
                    }
                });
                jFrame.add(okButton);
                DictionaryApplication.super.setEnabled(false);
                jFrame.setVisible(true);
            }
        });
        this.add(addButton);
    }

    /**
     * Create delete button and handle event.
     */
    private void createDeleteButton() {
        final BufferedImage img;
        try {
            img = ImageIO.read(new File(".\\icon\\delete.png"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e);
            return;
        }
        ImageIcon deleteIcon = new ImageIcon(img.getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        final JButton deleteButton = new JButton(deleteIcon);
        deleteButton.setToolTipText("Delete a word");
        deleteButton.setBounds(1110, 450, 50, 50);
        deleteButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String target = Filename.getText();
                if (target.isEmpty()) {
                    JOptionPane.showMessageDialog(getParent(), "Please choose a word from list");
                    return;
                }
                if (!DM.dictionaryLookup(dictionary, target)) {
                    JOptionPane.showMessageDialog(getParent(), "Not found!");
                    return;
                }
                int output = JOptionPane.showConfirmDialog(getParent(), "Are you sure?", "Confirmation",
                        JOptionPane.YES_NO_OPTION);
                if (output == JOptionPane.YES_OPTION) {
                    DM.deleteWord(dictionary, target);
                    JOptionPane.showMessageDialog(getParent(), "Deleted successfully");
                }
            }
        });
        this.add(deleteButton);
    }

    /**
     * Create edit button and handle event.
     */
    private void createEditButton() {
        final BufferedImage img;
        try {
            img = ImageIO.read(new File(".\\icon\\edit.png"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e);
            return;
        }
        ImageIcon editIcon = new ImageIcon(img.getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        final JButton editButton = new JButton(editIcon);
        editButton.setToolTipText("Fix a word's definition");
        editButton.setBounds(1110, 550, 50, 50);
        editButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String target = Filename.getText();
                if (target.isEmpty()) {
                    JOptionPane.showMessageDialog(getParent(), "Please choose a word from list");
                    return;
                }
                if (!DM.dictionaryLookup(dictionary, target)) {
                    JOptionPane.showMessageDialog(getParent(), "Not found!");
                    return;
                }
                JFrame jFrame = new JFrame("Input");
                jFrame.setLayout(null);
                jFrame.setPreferredSize(new Dimension(600, 400));
                jFrame.pack();
                jFrame.setLocationRelativeTo(null);
                jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                jFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        DictionaryApplication.super.setEnabled(true);
                        jFrame.dispose();
                    }
                });
                JTextPane jTextPane = new JTextPane();
                jTextPane.setFont(new java.awt.Font("Time New Roman", Font.PLAIN, 14));
                JScrollPane jScrollPane = new JScrollPane(jTextPane);
                jScrollPane.setBounds(40, 50, 500, 220);
                JLabel jLabel = new JLabel("New explain:");
                jLabel.setFont(font);
                jLabel.setBounds(40, 15, 200, 35);
                jFrame.add(jLabel);
                jFrame.add(jScrollPane);
                JButton okButton = new JButton("OK");
                okButton.setBounds(270, 300, 60, 35);
                okButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        String[] explain = jTextPane.getText().split("\\n");
                        if (explain[0].isEmpty() && explain.length == 1) {
                            JOptionPane.showMessageDialog(getParent(), "Explain should not be empty");
                            return;
                        }
                        int output = JOptionPane.showConfirmDialog(getParent(), "Are you sure?", "Confirmation",
                                JOptionPane.YES_NO_OPTION);
                        if (output == JOptionPane.YES_OPTION) {
                            DM.changeWord(dictionary, target, explain);
                            JOptionPane.showMessageDialog(getParent(), "Changed successfully");
                        }
                        DictionaryApplication.super.setEnabled(true);
                        jFrame.dispose();
                    }
                });
                jFrame.add(okButton);
                DictionaryApplication.super.setEnabled(false);
                jFrame.setVisible(true);
            }
        });
        this.add(editButton);
    }

    /**
     * Create information button and handle event.
     */
    private void createInfoButton() {
        final BufferedImage img;
        try {
            img = ImageIO.read(new File(".\\icon\\info.png"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e);
            return;
        }
        ImageIcon infoIcon = new ImageIcon(img.getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        final JButton infoButton = new JButton(infoIcon);
        infoButton.setBounds(1110, 750, 50, 50);
        infoButton.setToolTipText("Information");
        infoButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Filename.getStyledDocument().remove(0, Filename.getStyledDocument().getLength());
                } catch (BadLocationException badLocationException) {
                    badLocationException.printStackTrace();
                }
                Definition.setText(information());
            }
        });
        Definition.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(e.getURL().toURI());
                    } catch (IOException | URISyntaxException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        });
        this.add(infoButton);
    }

    /**
     * Create information.
     */
    private String information() {
        String s = "<html><font face='Arial' size=\"+4\">ⓘ INFORMATION</font>";
        s += "<br><br><font face='Arial' size=\"+3\">Author</font>"
                + "<br><ul><li><font face='Arial' color='#ff0000' size=\"+1\"> Bùi Huy Hoàng</font></li>"
                + "<br><li><font face='Arial' color='#ff0000' size=\"+1\"> Trần Minh Hoàng</font></li></ul>"
                + "<br><font face='Arial' size=\"+3\">Description</font>"
                + "<br><ul><li><font face='Arial' size=\"+1\"> A simple English-Vietnamese Dictionary built on java</font></li></ul>"
                + "<br><font face='Arial' size=\"+3\">Source</font>"
                + "<br><ul><li><font face='Arial' size=\"+1\"><a href=\"https://github.com/bhhhoang171/DictionaryBTL\">"
                + "https://github.com/bhhhoang171/DictionaryBTL</a></font></li></ul>"
                + "<br><font face='Arial' size=\"+3\">References</font>"
                + "<br><ul><li><font face='Arial' size=\"+1\">Text to speech API: <a href=\"https://github.com/wihoho/java-google-translate-text-to-speech\">"
                + "https://github.com/wihoho/java-google...</a></font></li>"
                + "<br><li><font face='Arial' size=\"+1\">Data: Lingoes data from <a href=\"https://drive.google.com/file/d/0B4nqOsoZZZrUa1VLenQ5eW13NDA/view?usp=sharing\">"
                + "https://drive.google.com/file/d/...</a></font></li></ul>";
        return s + "</html>";
    }

    /**
     * Create show all words button and handle event.
     */
    private void createShowButton() {
        final JLabel showLabel = new JLabel("<html><font face='Arial' size=\"+1\">Show all words</font></html>");
        showLabel.setBounds(690, 25, 200, 40);
        this.add(showLabel);
        final BufferedImage img;
        try {
            img = ImageIO.read(new File(".\\icon\\show.png"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e);
            return;
        }
        final ImageIcon showIcon = new ImageIcon(img.getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        final JButton show = new JButton(showIcon);
        show.setToolTipText("Show all words");
        show.setBounds(625, 20, 50, 50);
        show.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SearchingBox.setText("");
                model.removeAllElements();
                Trie T = dictionary.getTrieWord().find("");
                if (T != null) {
                    ArrayList<Word> find_words = T.getListOfWord();
                    for (Word w : find_words) {
                        model.addElement(w.getWord_target());
                    }
                }
            }
        });
        this.add(show);
    }

    /**
     * Create export button and handle event.
     */
    private void createExportButton() {
        final JLabel exportLabel = new JLabel("<html><font face='Arial' size=\"+1\">Export to file</font></html>");
        exportLabel.setBounds(965, 30, 300, 30);
        this.add(exportLabel);
        final BufferedImage img;
        try {
            img = ImageIO.read(new File(".\\icon\\export.png"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e);
            return;
        }
        final ImageIcon exportIcon = new ImageIcon(img.getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        final JButton export = new JButton(exportIcon);
        export.setToolTipText("Export dictionary to file");
        export.setBounds(900, 20, 50, 50);
        export.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int output = JOptionPane.showConfirmDialog(getParent(), "Are you sure?", "Confirmation",
                        JOptionPane.YES_NO_OPTION);
                if (output == JOptionPane.YES_OPTION) {
                    DM.dictionaryExportToFile(dictionary);
                }
            }
        });
        this.add(export);
    }

    /**
     * Create audio button and handle event.
     */
    private void createAudioSystem() {
        final BufferedImage audioImg;
        final JLabel UKlabel = new JLabel("<html><font face='Arial' size=\"+1\">UK</font></html>");
        UKlabel.setBounds(1125, 120, 50, 30);
        try {
            audioImg = ImageIO.read(new File(".\\icon\\audio.png"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e);
            return;
        }
        final ImageIcon audioIcon = new ImageIcon(audioImg.getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        final JButton UKbutton = new JButton(audioIcon);
        UKbutton.setToolTipText("Listen");
        UKbutton.setBounds(1110, 150, 50, 50);
        PronunciationAPI p = new PronunciationAPI();
        UKbutton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    InputStream inp = p.getAudio(Filename.getText(), "en-uk");
                    p.play(inp);
                } catch (IOException | JavaLayerException ioException) {
                    ioException.printStackTrace();
                }
            }

        });
        final JLabel USlabel = new JLabel("<html><font face='Arial' size=\"+1\">US</font></html>");
        USlabel.setBounds(1125, 220, 50, 30);
        final JButton USbutton = new JButton(audioIcon);
        USbutton.setToolTipText("Listen");
        USbutton.setBounds(1110, 250, 50, 50);
        USbutton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    InputStream inp = p.getAudio(Filename.getText(), "en-us");
                    p.play(inp);
                } catch (IOException | JavaLayerException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        this.add(UKbutton);
        this.add(UKlabel);
        this.add(USbutton);
        this.add(USlabel);
    }

    /**
     * Create translator API button and handle event.
     */
    private void createTranslateButton() {
        final BufferedImage img;
        try {
            img = ImageIO.read(new File(".\\icon\\translateapi.png"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e);
            return;
        }
        ImageIcon translateIcon = new ImageIcon(img.getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        final JButton translateButton = new JButton(translateIcon);
        translateButton.setToolTipText("Translate sentence using Google API");
        translateButton.setBounds(1110, 650, 50, 50);
        translateButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TranslateApi translateApi = new TranslateApi();
                translateApi.runTranslateApi();
                DictionaryApplication.super.setEnabled(false);
                translateApi.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        DictionaryApplication.super.setEnabled(true);
                        translateApi.dispose();
                    }
                });
            }
        });
        this.add(translateButton);
    }

    /**
     * Run dictionary application.
     */
    public void runApplication() {
        this.createSearchingBox();
        this.createSearchingResults();
        this.createDefinition();
        this.createExportButton();
        this.createShowButton();
        this.createAudioSystem();
        this.createTranslateButton();
        this.createAddButton();
        this.createDeleteButton();
        this.createEditButton();
        this.createInfoButton();
    }
}