package starlords.util;

import com.fs.starfarer.api.Global;

public class StringUtil {

    public static String getString(String category, String id) {
        try {
            return Global.getSettings().getString(category, id);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getString(String category, String id, String ... replacements) {
        String str = getString(category, id);
        if (str == null) return null;
        for (String replacement : replacements) {
            str = str.replaceFirst("\\$string", replacement);
        }
        return str;
    }
}
