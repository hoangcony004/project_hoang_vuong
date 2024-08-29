package hoang_vuong.project.doan.qdl;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringUtils {

    public static String removeDiacritics(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("").toLowerCase();
    }
}

