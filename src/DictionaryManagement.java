import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class DictionaryManagement {

    public void insertFromCommandline(Dictionary Dic) {

        Scanner sc = new Scanner(System.in);
        int numberOfWord;
        String target;
        String explain;
        Trie TrieWord = Dic.getTrieWord();
        System.out.print("Number of word : ");
        try {
            numberOfWord = sc.nextInt();
        } catch (Exception e) {
            System.out.println("Please insert a number");
            return;
        }
        sc.nextLine();

        for (int i = 0; i < numberOfWord; i++) {

            System.out.print("Word " + (i + 1) + " : ");
            target = sc.nextLine().toLowerCase();

            System.out.print("Explain : ");
            explain = sc.nextLine().toLowerCase();

            TrieWord.add(new Word(target, explain));

        }
    }

    public void insertFromFile(Dictionary Dic) {
        try {
            Scanner sc = new Scanner(new File("dictionaries.txt"));
            String target;
            String  explain;
            Trie TrieWord = Dic.getTrieWord();
            String[] split = sc.nextLine().split(" : ");
            target = split[0];
            explain = split[1];
            TrieWord.add(new Word(target, explain));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void dictionaryLookup(Dictionary Dic) {

        Scanner sc = new Scanner(System.in);
        System.out.print("Please enter the word which you want to lookup : ");
        String WordLookup = sc.nextLine();
        Trie T = Dic.getTrieWord().find(WordLookup);
        if (T == null)
            System.out.println("The word is not found!");
        else
            T.getWord().print();
    }

    public void deleteWord(Dictionary Dic) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Please enter the word which you want to delete : ");
        String WordDelete = sc.nextLine();
        Trie T = Dic.getTrieWord();
        T.delete(WordDelete);
    }

    public void changeWord(Dictionary Dic) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Please enter the word which you want to change : ");
        String WordChange = sc.nextLine();
        System.out.print("Explain : ");
        String ExplainChange = sc.nextLine();
        Trie T = Dic.getTrieWord().find(WordChange);
        if (T == null)
            System.out.println("The word is not found!");
        else
            T.getWord().setWord_explain(ExplainChange);
    }

    public void dictionaryExportToFile(Dictionary Dic) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter("dictionaries.txt"));
            ArrayList<Word> ListOfWord = Dic.getListOfWord();
            for (Word word : ListOfWord) {
                bw.write(word.getWord_target() + " : " + word.getWord_explain() + "\n");
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
