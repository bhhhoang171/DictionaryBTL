public class Word {

    private String word_target;
    private String word_explain;

    public Word() {
    }

    /**
     * Intialize word.
     *
     * @param word_target  Word target
     * @param word_explain Word explain
     */
    public Word(String word_target, String word_explain) {
        this.word_target = word_target;
        this.word_explain = word_explain;
    }

    /**
     * Intialize word from other word.
     *
     * @param input Word
     */
    public Word(Word input) {
        this.word_target = input.word_target;
        this.word_explain = input.word_explain;
    }

    /**
     * Get word target.
     *
     * @return String
     */
    public String getWord_target() {
        return word_target;
    }

    /**
     * Set word target.
     *
     * @param word_target Word target
     */
    public void setWord_target(String word_target) {
        this.word_target = word_target;
    }

    /**
     * Get word explain.
     *
     * @return String
     */
    public String getWord_explain() {
        return word_explain;
    }

    /**
     * Set word explain.
     *
     * @param word_explain Word explain
     */
    public void setWord_explain(String word_explain) {
        this.word_explain = word_explain;
    }

    /**
     * Print a word.
     */
    public void print() {
        System.out.printf("%20s|%20s", word_target, word_explain);
        System.out.println();
    }
}