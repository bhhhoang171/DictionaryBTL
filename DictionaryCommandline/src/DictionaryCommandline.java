import java.util.ArrayList;
import java.util.Scanner;

public class DictionaryCommandline {

    public void showAllWords(Dictionary Dic) {
        ArrayList<Word> ListOfWord = Dic.getListOfWord();
        if (ListOfWord.size() == 0) {
            System.out.println("The dictionary is empty");
        }
        System.out.printf("%-5s%-15s%-15s", "No", "| English", "| Vietnamese");
        System.out.println();
        Word word;
        for (int index = 0; index < ListOfWord.size(); ++index) {
            word = ListOfWord.get(index);
            System.out.printf("%-5d| %-15s| %-15s", index + 1, word.getWord_target(), word.getWord_explain());
            System.out.println();
        }

    }

    public void dictionaryBasic(Dictionary Dic) {
        System.out.print("Please choose one number from 1 to 3\n" + "   1 : insert word\n" + "   2 : show all word\n"
                + "   3 : exit\n");
        Scanner sc = new Scanner(System.in);
        int choose = 0;
        try {
            choose = sc.nextInt();
        } catch (Exception e) {
            System.out.println("Please choose a number");
        }
        DictionaryManagement dic1 = new DictionaryManagement();
        switch (choose) {
            case 1:
                dic1.insertFromCommandline(Dic);
                dictionaryBasic(Dic);
                break;
            case 2:
                showAllWords(Dic);
                dictionaryBasic(Dic);
                break;
            case 3:
                System.exit(0);
            default:
                dictionaryBasic(Dic);
                break;
        }
    }

    public void dictionarySearcher(Dictionary Dic) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Please enter the word which you want to searcher : ");
        String WordSearch = sc.nextLine();
        Trie T = Dic.getTrieWord().find(WordSearch);
        if (T == null) {
            System.out.println("The word not found");
            return;
        }
        Word word;
        ArrayList<Word> ListOfWord = T.getListOfWord();
        for (int index = 0; index < ListOfWord.size(); ++index) {
            word = ListOfWord.get(index);
            System.out.printf("%-5d| %-15s| %-15s", index + 1, word.getWord_target(), word.getWord_explain());
            System.out.println();
        }
    }

    public void dictionaryAdvanced(Dictionary Dic) {
        DictionaryManagement dic1 = new DictionaryManagement();
        System.out.print("Please choose one number from 1 to 9\n" + "   1 : insert word from commandline\n"
                + "   2 : show all word\n" + "   3 : lookup word\n" + "   4 : delete word\n" + "   5 : change word\n"
                + "   6 : search word\n" + "   7 : insert word from file\n" + "   8 : export word to file\n"
                + "   9 : exit\n");

        Scanner sc = new Scanner(System.in);
        int choose = 0;
        try {
            choose = sc.nextInt();
        } catch (Exception e) {
            System.out.println("Please choose a number");
        }
        switch (choose) {
            case 1:
                dic1.insertFromCommandline(Dic);
                dictionaryAdvanced(Dic);
                break;
            case 2:
                showAllWords(Dic);
                dictionaryAdvanced(Dic);
                break;
            case 3:
                dic1.dictionaryLookup(Dic);
                dictionaryAdvanced(Dic);
                break;
            case 4:
                dic1.deleteWord(Dic);
                dictionaryAdvanced(Dic);
                break;
            case 5:
                dic1.changeWord(Dic);
                dictionaryAdvanced(Dic);
                break;
            case 6:
                dictionarySearcher(Dic);
                dictionaryAdvanced(Dic);
                break;
            case 7:
                dic1.insertFromFile(Dic);
                dictionaryAdvanced(Dic);
                break;
            case 8:
                dic1.dictionaryExportToFile(Dic);
                dictionaryAdvanced(Dic);
                break;
            case 9:
                System.exit(0);
            default:
                dictionaryAdvanced(Dic);
                break;
        }
    }
}
