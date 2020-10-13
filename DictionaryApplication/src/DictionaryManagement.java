import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class DictionaryManagement {

    /**
     * Add a word to dictionary.
     *
     * @param Dic     Dictionary
     * @param target  Word target
     * @param explain Word explain
     */
    public void addWord(Dictionary Dic, String target, String[] explain) {
        Trie TrieWord = Dic.getTrieWord();
        StringBuilder res = new StringBuilder("<html><font size=\"+2\"><i>" + target + "</i></font>");
        for (String line : explain) {
            res.append("<br/><ul><li><font color='#cc0000' size=\"+1\"><b>").append(line)
                    .append("</b></font></li></ul>");
        }
        TrieWord.add(new Word(target, res + "</html>"));
    }

    /**
     * Insert words from file.
     *
     * @param Dic Dictionary
     */
    public void insertFromFile(Dictionary Dic) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(
                    new InputStreamReader(new FileInputStream("dictionaries.txt"), StandardCharsets.UTF_8));
            Trie TrieWord = Dic.getTrieWord();
            String textInALine;
            String target = "";
            String explain = "";
            while ((textInALine = br.readLine()) != null) {
                if (textInALine.isEmpty()) {
                    continue;
                } else {
                    for (int i = 0; i < textInALine.length(); ++i) {
                        if (textInALine.charAt(i) == '<') {
                            target = textInALine.substring(0, i).trim();
                            explain = textInALine.substring(i, textInALine.length()).trim();
                            break;
                        }
                    }
                }
                TrieWord.add(new Word(target, explain));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Find a word.
     *
     * @param Dic        Dicitonary
     * @param WordLookup Word need to find
     * @return boolean
     */
    public boolean dictionaryLookup(Dictionary Dic, String WordLookup) {
        Trie T = Dic.getTrieWord().find(WordLookup);
        if (T == null) {
            return false;
        } else {
            return T.getWord() != null;
        }
    }

    /**
     * Remove a word.
     *
     * @param Dic        Dictionary
     * @param WordDelete Word need to remove
     */
    public void deleteWord(Dictionary Dic, String WordDelete) {
        Trie T = Dic.getTrieWord();
        T.delete(WordDelete);
    }

    /**
     * Change a word's definition.
     *
     * @param Dic           Dictionary
     * @param WordChange    Word need to fix
     * @param ExplainChange New definition
     */
    public void changeWord(Dictionary Dic, String WordChange, String[] ExplainChange) {
        Trie T = Dic.getTrieWord().find(WordChange);
        StringBuilder res = new StringBuilder("<html><font size=\"+2\"><i>" + WordChange + "</i></font>");
        if (T == null) {
            System.out.println("The word is not found!");
            return;
        } else
            for (String line : ExplainChange) {
                res.append("<br/><ul><li><font color='#cc0000' size=\"+1\"><b>").append(line)
                        .append("</b></font></li></ul>");
            }
        T.getWord().setWord_explain(res + "</html>");
    }

    /**
     * Export new dictionary to file.
     *
     * @param Dic Dictionary
     */
    public void dictionaryExportToFile(Dictionary Dic) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream("dictionaries.txt"), StandardCharsets.UTF_8));
            ArrayList<Word> ListOfWord = Dic.getListOfWord();
            for (Word word : ListOfWord) {
                bw.write(word.getWord_target() + "\t" + word.getWord_explain() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
