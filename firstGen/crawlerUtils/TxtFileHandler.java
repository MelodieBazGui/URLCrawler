package crawlerUtils;
import java.io.*;

/**
 * Self explanatory class, this literally writes to a file the given strings and resets output files to zero upon rerun (behavior codded in CrawlerControler)
 * @author Mélodie Bazeries Guilbault
 */
public class TxtFileHandler {

    public static void writeToOutputFile(String url, String filePath) throws IOException {
        File file = new File(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(url);
            writer.newLine();
        }
    }

    public static void resetOutputFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            try (FileWriter writer = new FileWriter(file, false)) {
                writer.write("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
