import java.util.ArrayList;

public class Trie {
    private Trie[] childen = new Trie[30];
    private Word word;

    public void setWord(Word _word) {
        word = new Word(_word);
    }

    public Word getWord() {
        return word;
    }

    public void add(Word AddWord) {
        Trie p = this;
        int k;
        String target = AddWord.getWord_target();
        if (!checkWordInput(target)) {
            return;
        }
        for (int i = 0; i < target.length(); i++) {
            if (target.charAt(i) == ' ') {
                k = 'z' - 'a' + 1;
            } else {
                k = (int) (target.charAt(i) - 'a');
            }
            if (p.childen[k] == null) {
                p.childen[k] = new Trie();
            }
            p = p.childen[k];
        }
        p.word = new Word(AddWord);
    }

    public Trie find(String target) {
        if (!checkWordInput(target)) {
            return null;
        }
        Trie p = this;
        int k;
        for (int i = 0; i < target.length(); ++i) {
            if (target.charAt(i) == ' ') {
                k = 'z' - 'a' + 1;
            } else {
                k = (int) (target.charAt(i) - 'a');
            }
            if (p.childen[k] == null) {
                return null;
            }
            p = p.childen[k];
        }
        return p;
    }

    public void delete(String target) {
        if (!checkWordInput(target)) {
            return;
        }
        Trie p = this;
        int k;
        for (int i = 0; i < target.length(); ++i) {
            if (target.charAt(i) == ' ') {
                k = 'z' - 'a' + 1;
            } else {
                k = (int) (target.charAt(i) - 'a');
            }
            if (p.childen[k] == null) {
                return;
            }
            p = p.childen[k];
        }
        p.word = null;
    }

    public ArrayList<Word> getListOfWord() {
        ArrayList<Word> listOfWord = new ArrayList<>();
        listed(this, listOfWord);
        return listOfWord;
    }

    private void listed(Trie T, ArrayList<Word> listOfWord) {
        if (T.word != null)
            listOfWord.add(T.word);
        for (int i = 0; i < 30; ++i) {
            if (T.childen[i] != null)
                listed(T.childen[i], listOfWord);
        }
    }

    private boolean checkWordInput(String input) {
        input = fixWordInput(input);
        for (int i = 0; i < input.length(); ++i) {
            if (input.charAt(i) < 'a' || input.charAt(i) > 'z') {
                if (input.charAt(i) != ' ') {
                    System.out.println("Please insert a word");
                    return false;
                }
            }
        }
        return true;
    }

    private String fixWordInput(String input) {
        for (int i = 0; i < input.length(); ++i) {
            if (input.charAt(i) != ' ') {
                input = input.substring(i, input.length() - i);
                break;
            }
        }
        for (int i = input.length() - 1; i >= 0; --i) {
            if (input.charAt(i) != ' ') {
                input = input.substring(0, i + 1);
                break;
            }
        }
        return input;
    }
}
