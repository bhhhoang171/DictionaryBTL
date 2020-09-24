import java.util.ArrayList;

public class Dictionary {

    private Trie TrieWord = new Trie();

    public ArrayList<Word> getListOfWord() {
        return TrieWord.getListOfWord();
    }

    public Trie getTrieWord() {
        return TrieWord;
    }

    public void setTrieWord(ArrayList<Word> listOfWord) {
        for (int i = 0; i < listOfWord.size(); ++i) {
            TrieWord.add(listOfWord.get(i));
        }
    }
}
