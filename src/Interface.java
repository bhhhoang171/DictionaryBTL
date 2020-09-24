import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Interface extends JFrame {
    private JList SearchingResult;
    private JTextArea Definition;
    private JPanel Title;
    private JTextField SearchingBox;
    private JPanel MainPanel;
    private JLabel Dictionary;
    private JTextField Searchingfor;
    private Dictionary dict = new Dictionary();
    private DictionaryManagement DM = new DictionaryManagement();
    private DictionaryCommandline DC = new DictionaryCommandline();
    private DefaultListModel Defaultmodel = new DefaultListModel();

    Interface() {
        super("Basic Dictionary");
        this.setContentPane(this.MainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        // DC.dictionaryAdvanced(dict);
        SearchingResult.setModel(Defaultmodel);
        SearchingResult.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int WordIndex = SearchingResult.getSelectedIndex();
                if (WordIndex >= 0) {
                    Word w = dict.getListOfWord().get(WordIndex);
                    Definition.setText(w.getWord_explain());

                }

            }
        });
        DM.insertFromFile(dict);
        /*SearchingBox.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SearchingBox.setText("");
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
                SearchingBox.setText(" ");

            }
        }); */
        SearchingBox.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                warn();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                warn();

            }

            public void warn() {
                Searchingfor.setText("Searching for: " + SearchingBox.getText());
                Defaultmodel.removeAllElements();
                String find_word = SearchingBox.getText();
                Trie T = dict.getTrieWord().find(find_word);
                if (T != null) {
                    ArrayList<Word> find_words = T.getListOfWord();
                    for (Word w : find_words)
                        Defaultmodel.addElement(w.getWord_target());
                }
            }
        });

    }

    public static void main(String[] args) {
        Interface intf = new Interface();
        intf.setSize(800, 800);
        intf.setVisible(true);
    }
}
