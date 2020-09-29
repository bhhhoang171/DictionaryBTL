//import jaco.mp3.a.D;

import com.sun.deploy.panel.JSmartTextArea;
import jaco.mp3.player.MP3Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.Option;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DictionaryApplication extends JFrame {
    private Dictionary dictionary = new Dictionary();
    private DictionaryManagement DM = new DictionaryManagement();
    private JTextField SearchingBox;
    private JList SearchingResults;
    private JScrollPane scroll;
    private JSmartTextArea Definition;
    private JScrollPane scroll1;
    private JButton OptionButton;
    private JTextArea Filename = new JTextArea();
    private JLabel Search;
    private JLabel Result;
    private JLabel WordsExplain;
    private JPanel main;
    private JButton Export;
    private JButton Show;
    private JButton Audio;
    private DefaultListModel model;
    private MP3Player player;
    private final java.awt.Font font = new java.awt.Font("Arial",0,20);

    DictionaryApplication() {
        super("Dictionary");
        Image icon = Toolkit.getDefaultToolkit().getImage("icon\\dict_icon.png");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setPreferredSize(new Dimension(1200,900));
        this.setLocation(300,50);
        this.setUndecorated(true);
        this.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        DM.insertFromFile(dictionary);
        this.setIconImage(icon);
        this.getContentPane().setBackground(Color.decode("#43adc4"));
        this.setVisible(true);
    }

    void creatSearchingBox() {
        SearchingBox = new JTextField();
        SearchingBox.setBounds(0,30,450,35);
        SearchingBox.setFont(font);
        SearchingBox.setToolTipText("Prefix search");
        Search = new JLabel("Search");
        Search.setFont(font);
        Search.setBounds(10, 3, 100, 25);
        this.add(Search);
        this.add(SearchingBox);
    }

    void creatSeachingResults() {
        Result = new JLabel("Searching Result");
        Result.setFont(font);
        Result.setBounds(10, 70, 200, 25);
        this.add(Result);
        WordsExplain = new JLabel("Definition");
        WordsExplain.setFont(font);
        WordsExplain.setBounds(510, 73, 100, 20);
        this.add(WordsExplain);
        SearchingResults = new JList();
        model = new DefaultListModel();
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
        scroll = new JScrollPane(SearchingResults);
        scroll.setBounds(0,100,450,720);
        this.add(scroll);
        System.gc();
    }

    void creatDefinition() {
        Definition = new JSmartTextArea();
        //Definition.setColumns(5);
        Definition.setFont(font);
        SearchingResults.addListSelectionListener(new ListSelectionListener() {
            String defaultpath = "C:\\Users\\hoang\\OneDrive\\Desktop\\DictionaryBTL\\Lingoes English\\Longman 2005 Voice Package - British";
            String filename = new String();
            String filepath = new String();
            public void valueChanged(ListSelectionEvent e) {
                int selected_index = SearchingResults.getSelectedIndex();
                Trie T = dictionary.getTrieWord().find(SearchingBox.getText());
                if (T != null && selected_index >= 0) {
                    ArrayList<Word> find_words = T.getListOfWord();
                    Word selected_word = find_words.get(selected_index);
                    Definition.setText(selected_word.getWord_explain());
                    Filename.setText(selected_word.getWord_target());
                }
                filename = Filename.getText();
                Character prechar = Filename.getText().charAt(0);
                if (Character.isLowerCase(prechar)) {
                    prechar = Character.toUpperCase(prechar);
                }
                filepath = defaultpath + "\\" + prechar + "\\" + filename + ".mp3";
                player = new MP3Player(new File(filepath));

            }
        });
        scroll1 = new JScrollPane(Definition);
        scroll1.setBounds(500,100,670,720);
        this.add(scroll1);
        System.gc();
    }

    void creatOptionButton() {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("icon\\option.png"));
        } catch (IOException e) {}
        ImageIcon optionIcon = new ImageIcon(img.getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        OptionButton = new JButton(optionIcon);
        OptionButton.setBounds(1100,20,50,50);
        OptionButton.setToolTipText("Option");
        final JPopupMenu optionMenu = new JPopupMenu("Option");
        JMenuItem addMenuItem = new JMenuItem("Add a word");
        addMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String target = JOptionPane.showInputDialog("Please insert a word you want to add").toLowerCase().trim();
                while (target.isEmpty() || target.charAt(0) == ' ') {
                    JOptionPane.showMessageDialog(getParent(), "Please insert a word");
                     target = JOptionPane.showInputDialog("Please insert a word you want to add").toLowerCase().trim();
                }
                for (int i = 0; i < target.length(); ++i) {
                    if (target.charAt(i) < 'a' || target.charAt(i) > 'z') {
                        if (target.charAt(i) != ' ' && target.charAt(i) != '.' && target.charAt(i) != '-' && target.charAt(i) != '\'') {
                            JOptionPane.showMessageDialog(getParent(), "This word is not accepted !");
                            return;
                        }
                    }
                }
                String explain = JOptionPane.showInputDialog("Explain:").trim();
                while  (explain.isEmpty() || explain.charAt(0) == ' ') {
                    JOptionPane.showMessageDialog(getParent(), "Please insert a word");
                    explain = JOptionPane.showInputDialog("Explain:").trim();

                }
                int output = JOptionPane.showConfirmDialog(getParent(),"Are you sure?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (output == JOptionPane.YES_OPTION) {
                    DM.insertFromCommandline(dictionary, target, explain);
                    JOptionPane.showMessageDialog(getParent(), "Inserted successfully");
                }
            }
        });
        JMenuItem removeMenuItem = new JMenuItem("Remove a word");
        removeMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String target = JOptionPane.showInputDialog("Please insert a word you want to delete").toLowerCase().trim();
                while (target.isEmpty() || target.charAt(0) == ' ') {
                    JOptionPane.showMessageDialog(getParent(), "Please insert a word");
                    target = JOptionPane.showInputDialog("Please insert a word you want to delete").toLowerCase().trim();
                }
                for (int i = 0; i < target.length(); ++i) {
                    if (target.charAt(i) < 'a' || target.charAt(i) > 'z') {
                        if (target.charAt(i) != ' ' && target.charAt(i) != '.' && target.charAt(i) != '-' && target.charAt(i) != '\'') {
                            JOptionPane.showMessageDialog(getParent(), "This word is not accepted !");
                            return;
                        }
                    }
                }
                int output = JOptionPane.showConfirmDialog(getParent(),"Are you sure?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (output == JOptionPane.YES_OPTION) {
                    DM.deleteWord(dictionary, target);
                    JOptionPane.showMessageDialog(getParent(), "Deleted successfully");
                }
            }
        });
        JMenuItem changeMenuItem = new JMenuItem("Change a word explain");
        changeMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String target = JOptionPane.showInputDialog("Please insert a word you want to change").toLowerCase().trim();
                while (target.isEmpty() || target.charAt(0) == ' ') {
                    JOptionPane.showMessageDialog(getParent(), "Please insert a word");
                    target = JOptionPane.showInputDialog("Please insert a word you want to change").toLowerCase().trim();
                }
                for (int i = 0; i < target.length(); ++i) {
                    if (target.charAt(i) < 'a' || target.charAt(i) > 'z') {
                        if (target.charAt(i) != ' ' && target.charAt(i) != '.' && target.charAt(i) != '-' && target.charAt(i) != '\'') {
                            JOptionPane.showMessageDialog(getParent(), "This word is not accepted !");
                            return;
                        }
                    }
                }
                String explain = JOptionPane.showInputDialog("New explain:").trim();
                while (explain.isEmpty()) {
                    JOptionPane.showMessageDialog(getParent(), "This meaning is not accepted !");
                    explain = JOptionPane.showInputDialog("New explain:").trim();
                }
                int output = JOptionPane.showConfirmDialog(getParent(),"Are you sure?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (output == JOptionPane.YES_OPTION) {
                    DM.changeWord(dictionary, target, explain);
                    JOptionPane.showMessageDialog(getParent(), "Changed successfully");
                }
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
    }

    void creatShowButton() {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("icon\\show.png"));
        } catch (IOException e) {}
        ImageIcon showIcon = new ImageIcon(img.getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        Show = new JButton(showIcon);
        Show.setToolTipText("Show all words");
        Show.setBounds(1050, 20, 50, 50);
        Show.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
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
        this.add(Show);
        System.gc();
    }

    void creatExportButton() {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("icon\\export.png"));
        } catch (IOException e) {}
        ImageIcon exportIcon = new ImageIcon(img.getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        Export = new JButton(exportIcon);
        Export.setToolTipText("Export dictionary to file");
        Export.setBounds(1000, 20, 50, 50);
        Export.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int output = JOptionPane.showConfirmDialog(getParent(),"Are you sure?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (output == JOptionPane.YES_OPTION) {
                    DM.dictionaryExportToFile(dictionary);
                }
            }
        });
        this.add(Export);
    }
    void creatAudioSystem() {
        BufferedImage audioImg = null;
        try {
            audioImg = ImageIO.read(new File("icon\\audio.png"));
        } catch (IOException e) {}
        ImageIcon audioIcon = new ImageIcon(audioImg.getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        Audio = new JButton(audioIcon);
        Audio.setToolTipText("Listen");
        Audio.setBounds(950,20,50,50);
        Audio.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                player.play();
            }
        });
        this.add(Audio);
        this.pack();
        System.gc();
    }
    
    void runApplication() {
        this.creatSearchingBox();
        this.creatSeachingResults();
        this.creatDefinition();
        this.creatExportButton();
        this.creatShowButton();
        this.creatOptionButton();
        this.creatAudioSystem();
    }

    public static void main(String[] args) {
        DictionaryApplication basic_interface = new DictionaryApplication();
        basic_interface.runApplication();
    }
}
