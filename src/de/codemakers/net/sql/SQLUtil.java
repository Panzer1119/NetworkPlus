package de.codemakers.net.sql;

import de.codemakers.util.StringUtil;

/**
 * SQLUtil
 *
 * @author Paul Hagedorn
 */
public class SQLUtil {

    public static final String quote(Object object) {
        if (object == null) {
            return "NULL";
        }
        final String text = "" + object;
        if (text.isEmpty()) {
            return "";
        }
        return ((StringUtil.isStringDigitsOnly(text) || (text.startsWith("'") && text.endsWith("'"))) ? text : "'" + text + "'").replace("\\", "\\\\");
    }

}
