import jaco.mp3.a.D;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;

public class Interface extends JFrame {
    private Dictionary dictionary = new Dictionary();
    private DictionaryManagement DM = new DictionaryManagement();
    private JTextField SearchingBox;
    private JList SearchingResults;
    private JScrollPane scroll;
    private JTextArea Definition;
    private JScrollPane scroll1;
    private DefaultListModel model;
    Interface() {
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
        SearchingBox.setBounds(0,20,450,50);
        SearchingBox.setFont(new java.awt.Font("Times New Roman",0,20));
        this.add(SearchingBox);
    }
    void creatSeachingResults() {
        SearchingResults = new JList();
        model = new DefaultListModel();
        SearchingResults.setFont(new java.awt.Font("Times New Roman",0,20));
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
                    for (Word w : find_words)
                        model.addElement(w.getWord_target());
                }
            }
        });
        scroll = new JScrollPane(SearchingResults);
        scroll.setBounds(0,100,450,720);
        this.add(scroll);

    }
    void creatDefinition() {
        Definition = new JTextArea();
        Definition.setFont(new java.awt.Font("Times New Roman",0,20));
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
    public static void main(String []args) {

        Interface inf = new Interface();
        inf.creatSearchingBox();
        inf.creatSeachingResults();
        inf.creatDefinition();
    }
}
