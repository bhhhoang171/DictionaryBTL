import java.util.ArrayList;

public class Trie {
    private final Trie[] childen = new Trie[42];
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
            switch (target.charAt(i)) {
                case ' ':
                    k = 'z' - 'a' + 1;
                    break;
                case '-':
                    k = 'z' - 'a' + 2;
                    break;
                case '.':
                    k = 'z' - 'a' + 3;
                    break;
                case '\'':
                    k = 'z' - 'a' + 4;
                    break;
                case 'é':
                    k = 'z' - 'a' + 5;
                    break;
                case 'â':
                    k = 'z' - 'a' + 6;
                    break;
                case 'è':
                    k = 'z' - 'a' + 7;
                    break;
                case 'ö':
                    k = 'z' - 'a' + 8;
                    break;
                case 'ô':
                    k = 'z' - 'a' + 9;
                    break;
                case 'ê':
                    k = 'z' - 'a' + 10;
                    break;
                case 'à':
                    k = 'z' - 'a' + 11;
                    break;
                case 'ã':
                    k = 'z' - 'a' + 12;
                    break;
                case 'ə':
                    k = 'z' - 'a' + 13;
                    break;
                case 'û':
                    k = 'z' - 'a' + 14;
                    break;
                default:
                    k = (int) target.charAt(i) - 'a';
                    break;
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
            switch (target.charAt(i)) {
                case ' ':
                    k = 'z' - 'a' + 1;
                    break;
                case '-':
                    k = 'z' - 'a' + 2;
                    break;
                case '.':
                    k = 'z' - 'a' + 3;
                    break;
                case '\'':
                    k = 'z' - 'a' + 4;
                    break;
                case 'é':
                    k = 'z' - 'a' + 5;
                    break;
                case 'â':
                    k = 'z' - 'a' + 6;
                    break;
                case 'è':
                    k = 'z' - 'a' + 7;
                    break;
                case 'ö':
                    k = 'z' - 'a' + 8;
                    break;
                case 'ô':
                    k = 'z' - 'a' + 9;
                    break;
                case 'ê':
                    k = 'z' - 'a' + 10;
                    break;
                case 'à':
                    k = 'z' - 'a' + 11;
                    break;
                case 'ã':
                    k = 'z' - 'a' + 12;
                    break;
                case 'ə':
                    k = 'z' - 'a' + 13;
                    break;
                case 'û':
                    k = 'z' - 'a' + 14;
                    break;
                default:
                    k = (int) target.charAt(i) - 'a';
                    break;
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
            switch (target.charAt(i)) {
                case ' ':
                    k = 'z' - 'a' + 1;
                    break;
                case '-':
                    k = 'z' - 'a' + 2;
                    break;
                case '.':
                    k = 'z' - 'a' + 3;
                    break;
                case '\'':
                    k = 'z' - 'a' + 4;
                    break;
                case 'é':
                    k = 'z' - 'a' + 5;
                    break;
                case 'â':
                    k = 'z' - 'a' + 6;
                    break;
                case 'è':
                    k = 'z' - 'a' + 7;
                    break;
                case 'ö':
                    k = 'z' - 'a' + 8;
                    break;
                case 'ô':
                    k = 'z' - 'a' + 9;
                    break;
                case 'ê':
                    k = 'z' - 'a' + 10;
                    break;
                case 'à':
                    k = 'z' - 'a' + 11;
                    break;
                case 'ã':
                    k = 'z' - 'a' + 12;
                    break;
                case 'ə':
                    k = 'z' - 'a' + 13;
                    break;
                case 'û':
                    k = 'z' - 'a' + 14;
                    break;
                default:
                    k = (int) target.charAt(i) - 'a';
                    break;
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
        if (T.word != null) {
            listOfWord.add(T.word);
        }
        for (int i = 0; i < 30; ++i) {
            if (T.childen[i] != null) {
                listed(T.childen[i], listOfWord);
            }
        }
    }

    private boolean checkWordInput(String input) {
        input = input.trim();
        for (int i = 0; i < input.length(); ++i) {
            char a = input.charAt(i);
            if (a < 'a' || a > 'z') {
                if (a != ' ' && a != '.' && a != '-' && a != '\'') {
                    if (a != 'â' && a != 'ê' && a != 'é' && a != 'è' && a != 'ô' && a != 'ö' && a != 'à' && a != 'û'
                            && a != 'ã' && a != 'ə') {
                        System.out.println("Please insert a word");
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
