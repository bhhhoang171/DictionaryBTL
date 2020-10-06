//import jaco.mp3.a.D;

import com.sun.deploy.panel.JSmartTextArea;
import jaco.mp3.player.MP3Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DictionaryApplication extends JFrame {
    private final Dictionary dictionary = new Dictionary();
    private final DictionaryManagement DM = new DictionaryManagement();
    private final JTextField SearchingBox = new JTextField();
    private final JList<String> SearchingResults = new JList<>();
    private final JSmartTextArea Definition = new JSmartTextArea();
    private final JLabel Search = new JLabel("Search");
    private final JLabel Result = new JLabel("Searching Result");
    private final JLabel WordsExplain = new JLabel("Definition");
    private JPanel main;
    private final TranslateApi translateApi = new TranslateApi();
    private final DefaultListModel<String> model = new DefaultListModel<>();
    private MP3Player player;
    private final java.awt.Font font = new java.awt.Font("Arial", 0, 20);

    DictionaryApplication() {
        super("Dictionary");
        final Image icon = Toolkit.getDefaultToolkit().getImage("icon\\dict_icon.png");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setPreferredSize(new Dimension(1200, 900));
        this.setLocation(300, 50);
        this.setUndecorated(true);
        this.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        DM.insertFromFile1(dictionary);
        this.setIconImage(icon);
        this.getContentPane().setBackground(Color.decode("#43adc4"));
        this.setVisible(true);
    }

    void createSearchingBox() {
        SearchingBox.setBounds(0, 30, 450, 35);
        SearchingBox.setFont(font);
        SearchingBox.setToolTipText("Prefix search");
        Search.setFont(font);
        Search.setBounds(10, 3, 100, 25);
        this.add(Search);
        this.add(SearchingBox);
    }

    void createSearchingResults() {
        Result.setFont(font);
        Result.setBounds(10, 70, 200, 25);
        this.add(Result);
        WordsExplain.setFont(font);
        WordsExplain.setBounds(510, 73, 100, 20);
        this.add(WordsExplain);
        //model = new DefaultListModel();
        SearchingResults.setFont(font);
        SearchingResults.setModel(model);

        SearchingBox.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();

            }

            public void update() {
                model.removeAllElements();
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
        final JScrollPane scroll = new JScrollPane(SearchingResults);
        scroll.setBounds(0, 100, 450, 720);
        this.add(scroll);
        System.gc();
    }

    void createDefinition() {
        Definition.setFont(font);
        SearchingResults.addListSelectionListener(new ListSelectionListener() {
            final String defaultpath = "Longman 2005 Voice Package - British";
            String filename = "";
            String filepath = "";

            public void valueChanged(ListSelectionEvent e) {
                int selected_index = SearchingResults.getSelectedIndex();
                Trie T = dictionary.getTrieWord().find(SearchingBox.getText());
                if (T != null && selected_index >= 0) {
                    ArrayList<Word> find_words = T.getListOfWord();
                    Word selected_word = find_words.get(selected_index);
                    Definition.setText(selected_word.getWord_explain());
                    filename = selected_word.getWord_target();
                }
                char prechar = filename.charAt(0);
                if (Character.isLowerCase(prechar)) {
                    prechar = Character.toUpperCase(prechar);
                }
                filepath = defaultpath + "\\" + prechar + "\\" + filename + ".mp3";
                player = new MP3Player(new File(filepath));

            }
        });
        final JScrollPane scroll1 = new JScrollPane(Definition);
        scroll1.setBounds(500, 100, 670, 720);
        this.add(scroll1);
        System.gc();
    }

    void createOptionButton() {
        final BufferedImage img;
        try {
            img = ImageIO.read(new File("icon\\option.png"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e);
            return;
        }
        final ImageIcon optionIcon = new ImageIcon(img.getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        final JButton OptionButton = new JButton(optionIcon);
        OptionButton.setBounds(1100, 20, 50, 50);
        OptionButton.setToolTipText("Option");
        final JPopupMenu optionMenu = new JPopupMenu("Option");
        final JMenuItem addMenuItem = new JMenuItem("Add a word");
        addMenuItem.addActionListener(e -> {
            String target;
            try {
                target = JOptionPane.showInputDialog("Please insert a word you want to add").toLowerCase()
                        .trim();
            } catch (Exception er) {
                return;
            }
            while (target.isEmpty()) {
                JOptionPane.showMessageDialog(getParent(), "Please insert a word");
                target = JOptionPane.showInputDialog("Please insert a word you want to add").toLowerCase().trim();
            }
            for (int i = 0; i < target.length(); ++i) {
                char a = target.charAt(i);
                if (a < 'a' || a > 'z') {
                    if (a != ' ' && a != '.' && a != '-' && a != '\'') {
                        if (a != 'â' && a != 'ê' && a != 'é' && a != 'è' && a != 'ô' && a != 'ö' && a != 'à'
                                && a != 'û' && a != 'ã' && a != 'ə') {
                            JOptionPane.showMessageDialog(getParent(), "This word is not accepted !");
                            return;
                        }
                    }
                }
            }
            String explain;
            try {
                explain = JOptionPane.showInputDialog("Explain:").trim();
            } catch (Exception er) {
                return;
            }
            while (explain.isEmpty()) {
                JOptionPane.showMessageDialog(getParent(), "Please insert a word");
                explain = JOptionPane.showInputDialog("Explain:").trim();

            }
            int output = JOptionPane.showConfirmDialog(getParent(), "Are you sure?", "Confirmation",
                    JOptionPane.YES_NO_OPTION);
            if (output == JOptionPane.YES_OPTION) {
                DM.insertFromCommandline(dictionary, target, explain);
                JOptionPane.showMessageDialog(getParent(), "Inserted successfully");
            }
        });
        final JMenuItem removeMenuItem = new JMenuItem("Remove a word");
        removeMenuItem.addActionListener(e -> {
            String target;
            try {
                target = JOptionPane.showInputDialog("Please insert a word you want to delete").toLowerCase()
                        .trim();
            } catch (Exception er) {
                return;
            }
            while (target.isEmpty()) {
                JOptionPane.showMessageDialog(getParent(), "Please insert a word");
                target = JOptionPane.showInputDialog("Please insert a word you want to delete").toLowerCase()
                        .trim();
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
        });
        final JMenuItem changeMenuItem = new JMenuItem("Change a word explain");
        changeMenuItem.addActionListener(e -> {
            String target;
            try {
                target = JOptionPane.showInputDialog("Please insert a word you want to change").toLowerCase()
                        .trim();
            } catch (Exception er) {
                return;
            }
            while (target.isEmpty()) {
                JOptionPane.showMessageDialog(getParent(), "Please insert a word");
                target = JOptionPane.showInputDialog("Please insert a word you want to change").toLowerCase()
                        .trim();
            }
            if (!DM.dictionaryLookup(dictionary, target)) {
                JOptionPane.showMessageDialog(getParent(), "Not found!");
                return;
            }
            String explain;
            try {
                explain = JOptionPane.showInputDialog("New explain:").trim();
            } catch (Exception er) {
                return;
            }
            while (explain.isEmpty()) {
                JOptionPane.showMessageDialog(getParent(), "This meaning is not accepted !");
                explain = JOptionPane.showInputDialog("New explain:").trim();
            }
            int output = JOptionPane.showConfirmDialog(getParent(), "Are you sure?", "Confirmation",
                    JOptionPane.YES_NO_OPTION);
            if (output == JOptionPane.YES_OPTION) {
                DM.changeWord(dictionary, target, explain);
                JOptionPane.showMessageDialog(getParent(), "Changed successfully");
            }
        });
        optionMenu.add(addMenuItem);
        optionMenu.add(removeMenuItem);
        optionMenu.add(changeMenuItem);
        optionMenu.setPopupSize(200, 100);
        OptionButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                optionMenu.show(OptionButton, e.getX(), e.getY());
            }
        });
        this.add(OptionButton);
        System.gc();
    }

    void createShowButton() {
        final BufferedImage img;
        try {
            img = ImageIO.read(new File("icon\\show.png"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e);
            return;
        }
        final ImageIcon showIcon = new ImageIcon(img.getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        final JButton show = new JButton(showIcon);
        show.setToolTipText("Show all words");
        show.setBounds(1050, 20, 50, 50);
        show.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SearchingBox.setText(null);
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
        System.gc();
    }

    void createExportButton() {
        final BufferedImage img;
        try {
            img = ImageIO.read(new File("icon\\export.png"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e);
            return;
        }
        final ImageIcon exportIcon = new ImageIcon(img.getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        final JButton export = new JButton(exportIcon);
        export.setToolTipText("Export dictionary to file");
        export.setBounds(1000, 20, 50, 50);
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
        System.gc();
    }

    void createAudioSystem() {
        final BufferedImage audioImg;
        try {
            audioImg = ImageIO.read(new File("icon\\audio.png"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e);
            return;
        }
        final ImageIcon audioIcon = new ImageIcon(audioImg.getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        final JButton audio = new JButton(audioIcon);
        audio.setToolTipText("Listen");
        audio.setBounds(950, 20, 50, 50);
        audio.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                player.play();
            }
        });
        this.add(audio);
        this.pack();
        System.gc();
    }

    void creattranslateButton() {
        final JButton translateButton = new JButton("Translate sentence");
        translateButton.setToolTipText("Translate sentence using Google API");
        translateButton.setBounds(500, 30, 150, 35);
        translateButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                translateApi.runTranslateApi();
            }
        });
        this.add(translateButton);
        System.gc();
    }

    void runApplication() {
        this.createSearchingBox();
        this.createSearchingResults();
        this.createDefinition();
        this.createExportButton();
        this.createShowButton();
        this.createOptionButton();
        this.createAudioSystem();
        this.creattranslateButton();
    }

    public static void main(String[] args) {
        DictionaryApplication basic_interface = new DictionaryApplication();
        basic_interface.runApplication();
    }
}