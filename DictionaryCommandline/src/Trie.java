import java.util.ArrayList;

public class Trie {
    private final Trie[] childen = new Trie[80];
    private Word word;

    /**
     * Set word.
     *
     * @param _word Word
     */
    public void setWord(Word _word) {
        word = new Word(_word);
    }

    /**
     * Get word.
     *
     * @return Word
     */
    public Word getWord() {
        return word;
    }

    /**
     * Add a word to Trie.
     *
     * @param AddWord Word
     */
    public void add(Word AddWord) {
        Trie p = this;
        int k;
        String target = AddWord.getWord_target();
        if (!checkWordInput(target)) {
            return;
        }
        for (int i = 0; i < target.length(); i++) {
            switch (target.charAt(i)) {
                case ' ':
                    k = 0;
                    break;
                case '-':
                    k = 2;
                    break;
                case '.':
                    k = 1;
                    break;
                case '\'':
                    k = 3;
                    break;
                case '(':
                    k = 4;
                    break;
                case ')':
                    k = 5;
                    break;
                case 'à':
                    k = 68;
                    break;
                case 'ã':
                    k = 69;
                    break;
                case 'â':
                    k = 70;
                    break;
                case 'é':
                    k = 71;
                    break;
                case 'è':
                    k = 72;
                    break;
                case 'ê':
                    k = 73;
                    break;
                case 'ö':
                    k = 74;
                    break;
                case 'ô':
                    k = 75;
                    break;
                case 'û':
                    k = 76;
                    break;
                case 'ə':
                    k = 77;
                    break;
                default:
                    if (target.charAt(i) >= 'A' && target.charAt(i) <= 'Z') {
                        k = (int) target.charAt(i) - 'A' + 16;
                    } else if (target.charAt(i) >= 'a' && target.charAt(i) <= 'z') {
                        k = (int) target.charAt(i) - 'a' + 42;
                    } else {
                        k = (int) target.charAt(i) - '0' + 6;
                    }
                    break;
            }
            if (p.childen[k] == null) {
                p.childen[k] = new Trie();
            }
            p = p.childen[k];
        }
        p.word = new Word(AddWord);
    }

    /**
     * Find a word in trie.
     *
     * @param target Word target
     * @return Trie
     */
    public Trie find(String target) {
        if (!checkWordInput(target)) {
            return null;
        }
        Trie p = this;
        int k;
        for (int i = 0; i < target.length(); ++i) {
            switch (target.charAt(i)) {
                case ' ':
                    k = 0;
                    break;
                case '-':
                    k = 2;
                    break;
                case '.':
                    k = 1;
                    break;
                case '\'':
                    k = 3;
                    break;
                case '(':
                    k = 4;
                    break;
                case ')':
                    k = 5;
                    break;
                case 'à':
                    k = 68;
                    break;
                case 'ã':
                    k = 69;
                    break;
                case 'â':
                    k = 70;
                    break;
                case 'é':
                    k = 71;
                    break;
                case 'è':
                    k = 72;
                    break;
                case 'ê':
                    k = 73;
                    break;
                case 'ö':
                    k = 74;
                    break;
                case 'ô':
                    k = 75;
                    break;
                case 'û':
                    k = 76;
                    break;
                case 'ə':
                    k = 77;
                    break;
                default:
                    if (target.charAt(i) >= 'A' && target.charAt(i) <= 'Z') {
                        k = (int) target.charAt(i) - 'A' + 16;
                    } else if (target.charAt(i) >= 'a' && target.charAt(i) <= 'z') {
                        k = (int) target.charAt(i) - 'a' + 42;
                    } else {
                        k = (int) target.charAt(i) - '0' + 6;
                    }
                    break;
            }
            if (p.childen[k] == null) {
                return null;
            }
            p = p.childen[k];
        }
        return p;
    }

    /**
     * Delete a word from trie.
     *
     * @param target Word target
     */
    public void delete(String target) {
        if (!checkWordInput(target)) {
            return;
        }
        Trie p = this;
        int k;
        for (int i = 0; i < target.length(); ++i) {
            switch (target.charAt(i)) {
                case ' ':
                    k = 0;
                    break;
                case '-':
                    k = 2;
                    break;
                case '.':
                    k = 1;
                    break;
                case '\'':
                    k = 3;
                    break;
                case '(':
                    k = 4;
                    break;
                case ')':
                    k = 5;
                    break;
                case 'à':
                    k = 68;
                    break;
                case 'ã':
                    k = 69;
                    break;
                case 'â':
                    k = 70;
                    break;
                case 'é':
                    k = 71;
                    break;
                case 'è':
                    k = 72;
                    break;
                case 'ê':
                    k = 73;
                    break;
                case 'ö':
                    k = 74;
                    break;
                case 'ô':
                    k = 75;
                    break;
                case 'û':
                    k = 76;
                    break;
                case 'ə':
                    k = 77;
                    break;
                default:
                    if (target.charAt(i) >= 'A' && target.charAt(i) <= 'Z') {
                        k = (int) target.charAt(i) - 'A' + 16;
                    } else if (target.charAt(i) >= 'a' && target.charAt(i) <= 'z') {
                        k = (int) target.charAt(i) - 'a' + 42;
                    } else {
                        k = (int) target.charAt(i) - '0' + 6;
                    }
                    break;
            }
            if (p.childen[k] == null) {
                return;
            }
            p = p.childen[k];
        }
        p.word = null;
    }

    /**
     * Create list of words from trie.
     *
     * @return ArrayList<Word>
     */
    public ArrayList<Word> getListOfWord() {
        ArrayList<Word> listOfWord = new ArrayList<>();
        listed(this, listOfWord);
        return listOfWord;
    }

    /**
     * List word.
     *
     * @param T          Trie
     * @param listOfWord ArrayList of word
     */
    private void listed(Trie T, ArrayList<Word> listOfWord) {
        if (T.word != null) {
            listOfWord.add(T.word);
        }
        for (int i = 0; i < 80; ++i) {
            if (T.childen[i] != null) {
                listed(T.childen[i], listOfWord);
            }
        }
    }

    /**
     * Check format of word and ignore all special symbols.
     *
     * @param input Word input
     * @return boolean
     */
    private boolean checkWordInput(String input) {
        input = input.trim();
        for (int i = 0; i < input.length(); ++i) {
            char a = input.charAt(i);
            if (a >= 'a' && a <= 'z') {
                continue;
            } else if (a >= 'A' && a <= 'Z') {
                continue;
            } else if (a >= '0' && a <= '9') {
                continue;
            } else if (a == ' ' || a == '.' || a == '-' || a == '\'' || a == '(' || a == ')') {
                continue;
            } else if (a == 'â' || a == 'ê' || a == 'é' || a == 'è' || a == 'ô' || a == 'ö' || a == 'à' || a == 'û'
                    || a == 'ã' || a == 'ə') {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }
}
