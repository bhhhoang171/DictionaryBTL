//import jaco.mp3.a.D;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.html.Option;
import java.awt.event.*;
import java.util.ArrayList;

public class DictionaryApplication extends JFrame {
    private Dictionary dictionary = new Dictionary();
    private DictionaryManagement DM = new DictionaryManagement();
    private JTextField SearchingBox;
    private JList SearchingResults;
    private JScrollPane scroll;
    private JTextArea Definition;
    private JScrollPane scroll1;
    private JButton OptionButton;
    private JLabel Search;
    private JLabel Result;
    private JLabel WordsExplain;
    private JButton Export;
    private JButton Show;
    private DefaultListModel model;
    private final java.awt.Font font = new java.awt.Font("Times New Roman",0,20);

    DictionaryApplication() {
        super("Dictionary");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.pack();
        this.setLayout(null);
        this.setSize(1200,900);
        DM.insertFromFile(dictionary);
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
        /*Show = new JButton("Show all words");
        Show.setBounds(500, 10, 150, 30);
        Show.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Trie T = dictionary.getTrieWord().find("");
                if (T != null) {
                    ArrayList<Word> find_words = T.getListOfWord();
                    for (Word w : find_words) {
                        model.addElement(w.getWord_target());
                    }
                }
            }
        });
        this.add(Show);*/
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
    }

    void creatDefinition() {
        Definition.setFont(font);
        SearchingResults.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selected_index = SearchingResults.getSelectedIndex();
                Trie T = dictionary.getTrieWord().find(SearchingBox.getText());
                if (T != null && selected_index >= 0) {
                    ArrayList<Word> find_words = T.getListOfWord();
                    Word selected_word = find_words.get(selected_index);
                    Definition.setText(selected_word.getWord_explain());
                }

            }
        });
        scroll1 = new JScrollPane(Definition);
        scroll1.setBounds(500,100,670,720);
        this.add(scroll1);
    }

    void creatOptionButton() {
        OptionButton = new JButton("O");
        OptionButton.setBounds(1100,20,50,50);
        OptionButton.setToolTipText("Option");
        final JPopupMenu optionMenu = new JPopupMenu("Option");
        JMenuItem addMenuItem = new JMenuItem("Add a word");
        addMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String target = JOptionPane.showInputDialog("Please insert a word you want to add").toLowerCase();
                for (int i = 0; i < target.length(); ++i) {
                    if (target.charAt(i) < 'a' || target.charAt(i) > 'z') {
                        if (target.charAt(i) != ' ' && target.charAt(i) != '.' && target.charAt(i) != '-' && target.charAt(i) != '\'') {
                            JOptionPane.showMessageDialog(getParent(), "Please insert a word");
                            return;
                        }
                    }
                }
                String explain = JOptionPane.showInputDialog("Explain:");
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
                String target = JOptionPane.showInputDialog("Please insert a word you want to delete").toLowerCase();
                for (int i = 0; i < target.length(); ++i) {
                    if (target.charAt(i) < 'a' || target.charAt(i) > 'z') {
                        if (target.charAt(i) != ' ' && target.charAt(i) != '.' && target.charAt(i) != '-' && target.charAt(i) != '\'') {
                            JOptionPane.showMessageDialog(getParent(), "Please insert a word");
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
                String target = JOptionPane.showInputDialog("Please insert a word you want to change").toLowerCase();
                for (int i = 0; i < target.length(); ++i) {
                    if (target.charAt(i) < 'a' || target.charAt(i) > 'z') {
                        if (target.charAt(i) != ' ' && target.charAt(i) != '.' && target.charAt(i) != '-' && target.charAt(i) != '\'') {
                            JOptionPane.showMessageDialog(getParent(), "Please insert a word");
                            return;
                        }
                    }
                }
                String explain = JOptionPane.showInputDialog("New explain:");
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
        OptionButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                optionMenu.show(OptionButton, e.getX(), e.getY());
            }
        });
        this.add(OptionButton);
    }

    void creatShowButton() {
        Show = new JButton("S");
        Show.setToolTipText("Show all words");
        Show.setBounds(1050, 20, 50, 50);
        Show.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
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
    }

    void creatExportButton() {
        Export = new JButton("E");
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

    void runApplication() {
        this.creatExportButton();
        this.creatShowButton();
        this.creatOptionButton();
        this.creatSearchingBox();
        this.creatSeachingResults();
        this.creatDefinition();
    }

    public static void main(String[] args) {
        DictionaryApplication basic_interface = new DictionaryApplication();
        basic_interface.runApplication();
    }
}
