import java.util.ArrayList;

public class Dictionary {

    private final Trie TrieWord = new Trie();

    public ArrayList<Word> getListOfWord() {
        return TrieWord.getListOfWord();
    }

    public Trie getTrieWord() {
        return TrieWord;
    }

    public void setTrieWord(ArrayList<Word> listOfWord) {
        for (Word word : listOfWord) {
            TrieWord.add(word);
        }
    }
}
