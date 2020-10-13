import java.util.ArrayList;

public class Dictionary {

    private final Trie TrieWord = new Trie();

    /**
     * Get List of words.
     *
     * @return ArrayList<Word>
     */
    public ArrayList<Word> getListOfWord() {
        return TrieWord.getListOfWord();
    }

    /**
     * Get trie word.
     *
     * @return Trie
     */
    public Trie getTrieWord() {
        return TrieWord;
    }

    /**
     * Create a trie word from an ArrayList.
     *
     * @param listOfWord ArrayList of words
     */
    public void setTrieWord(ArrayList<Word> listOfWord) {
        for (Word word : listOfWord) {
            TrieWord.add(word);
        }
    }
}
