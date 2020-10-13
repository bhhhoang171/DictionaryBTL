import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    /**
     * @author Bui Huy Hoang - https://github.com/bhhhoang171
     * @author Tran Minh Hoang - https://github.com/chnk58hoang
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new DictionaryApplication().runApplication();
            } catch (Exception ex) {
                Logger.getLogger(DictionaryApplication.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
