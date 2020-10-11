import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Scanner;
public class OnlineSearch{
    OnlineSearch() {

    }
    public String gettext(String text) {
        //String text="learn";
        try {
            URL url = new URL("https://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=vi&dt=t&q="
                    + text.replace(" ", "%20"));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US)");
            //BufferedInputStream x = new BufferedInputStream(urlConn.getInputStream());
            Scanner resScanner = new Scanner(connection.getInputStream(), "UTF8");
            String res = resScanner.nextLine();
            res = res.substring(3, res.length() - 12);
            for (int i = 0; i < res.length(); i++) {
                if (res.charAt(i) == ',') {
                    res = res.substring(0, i) + res.substring(i + 1);
                }
            }
            int quoteCount = 0;
            for (int i = 0; i < res.length(); i++) {
                if (res.charAt(i) == '"') {
                    quoteCount++;
                }
                if (quoteCount == 2) {
                    res = res.substring(1, i);
                }
            }
            // System.out.println(res);
            return res;
        }
        catch (Exception e) {
            return "";
        }

    }
}