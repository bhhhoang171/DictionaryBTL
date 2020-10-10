import java.io.*;
import java.nio.charset.StandardCharsets;
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

    public void addWord(Dictionary Dic, String target, String explain) {
        Trie TrieWord = Dic.getTrieWord();
        target = target.toLowerCase();
        explain = explain.toLowerCase();
        TrieWord.add(new Word(target, explain));
    }

    public void insertFromFile(Dictionary Dic) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(
                    new InputStreamReader(new FileInputStream("dictionaries.txt"), StandardCharsets.UTF_8));
            Trie TrieWord = Dic.getTrieWord();
            String textInALine;
            String target;
            String explain;
            while ((textInALine = br.readLine()) != null) {
                String[] split = textInALine.split(" : ");
                target = split[0];
                explain = split[1];
                TrieWord.add(new Word(target.toLowerCase(), explain));
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

    public void insertFromFile1(Dictionary Dic) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream("dictionaries.txt"), StandardCharsets.UTF_8));
            Trie TrieWord = Dic.getTrieWord();
            String textInALine;
            String target = "";
            String explain = "";
            while ((textInALine = br.readLine()) != null) {
                if (textInALine.isEmpty()) {
                    continue;
                } else {
                    for(int i = 0; i < textInALine.length(); ++i) {
                        if(textInALine.charAt(i) == '<') {
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

    public boolean dictionaryLookup(Dictionary Dic, String WordLookup) {
        Trie T = Dic.getTrieWord().find(WordLookup);
        if (T == null) {
            return false;
        } else return T.getWord() != null;
    }

    public void deleteWord(Dictionary Dic) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Please enter the word which you want to delete : ");
        String WordDelete = sc.nextLine();
        Trie T = Dic.getTrieWord();
        T.delete(WordDelete);
    }

    public void deleteWord(Dictionary Dic, String WordDelete) {
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

    public void changeWord(Dictionary Dic, String WordChange, String ExplainChange) {
        Trie T = Dic.getTrieWord().find(WordChange);
        if (T == null) {
            System.out.println("The word is not found!");
        } else {
            T.getWord().setWord_explain(ExplainChange);
        }
    }

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
