package gaj.common.encoding;

import com.unboundid.util.Base64;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

/**
 * Encapsulates simple methods to aid in the use of HTTP Basic authentication.
 * <br/><b>Note:</b>It is up to the caller to keep this information secure.
 */
public class BasicAuth {

    private static final String BASIC_SEPARATOR = ":";

    /**
     * Converts the supplied user credentials into a basic authorisation.
     *
     * @param loginName - The user's login name.
     * @param password - The user's password.
     * @return The basic authorisation.
     */
    public static String toBasic(String loginName, String password) {
        return Base64.encode(loginName + BASIC_SEPARATOR + password);
    }

    /**
     * Converts the supplied user credentials into a basic authorisation.
     *
     * @param loginName - The user's login name.
     * @param password - The user's password.
     * @return The basic authorisation.
     */
    public static String toBasic(String loginName, char[] password) {
        StringBuilder buf = new StringBuilder(loginName);
        buf.append(BASIC_SEPARATOR);
        buf.append(password);
        return Base64.encode(buf.toString());
    }

    /**
     * Converts the basic authorisation into user credentials.
     *
     * @param auth - The basic authorisation.
     * @return A two-part array containing the user's loginName and password.
     * @throws IllegalArgumentException If the authorisation is invalid.
     */
    public static String[] fromBasic(String auth) {
        try {
            String[] parts = new String(Base64.decode(auth), "utf-8").split(BASIC_SEPARATOR, 2);
            if (parts.length == 2) {
                return parts;
            } // else fall through...
        } catch (UnsupportedEncodingException | ParseException e) {
            // Fall through...
        }
        throw new IllegalArgumentException("Invalid authorisation");
    }

    /**
     * Converts the basic authorisation into user credentials.
     *
     * @param auth - The basic authorisation.
     * @return A two-part array containing the user's loginName and password, or
     * a value of null if the authorisation is invalid.
     */
    public static String[] fromBasic(char[] auth) {
        return fromBasic(new String(auth));
    }

    /**
     * Converts the basic authorisation into an extended authorisation.
     *
     * @param auth - The basic authorisation.
     * @return The extended authorisation.
     */
    public static char[] toExtended(String auth) {
        final int length = auth.length();
        char[] xauth = new char[length * 2];
        for (int i = 0, j = 0; i < length; i++) {
            char c = auth.charAt(i);
            xauth[j++] = toNibble(c >> 4);
            xauth[j++] = toNibble(c & 0x0F);
        }
        return xauth;
    }

    /**
     * Converts the basic authorisation into an extended authorisation.
     *
     * @param auth - The basic authorisation.
     * @return The extended authorisation.
     */
    public static char[] toExtended(char[] auth) {
        final int length = auth.length;
        char[] xauth = new char[length * 2];
        for (int i = 0, j = 0; i < length; i++) {
            char c = auth[i];
            xauth[j++] = toNibble(c >> 4);
            xauth[j++] = toNibble(c & 0x0F);
        }
        return xauth;
    }

    /**
     * Converts the extended authorisation into a basic authorisation.
     *
     * @param xauth - The extended authorisation.
     * @return The basic authorisation, or a value of null if the authorisation is invalid.
     */
    public static char/*@Nullable*/[] fromExtended(String xauth) {
        final int length = xauth.length();
        if (length % 2 != 0) {
            return null;
        }
        char[] auth = new char[length / 2];
        for (int i = 0, j = 0; i < length; ) {
            char c1 = xauth.charAt(i++);
            char c2 = xauth.charAt(i++);
            auth[j++] = (char)(fromNibble(c1) << 4 | fromNibble(c2));
        }
        return auth;
    }

    /**
     * Converts the extended authorisation into a basic authorisation.
     *
     * @param xauth - The extended authorisation.
     * @return The basic authorisation.
     * @throws IllegalArgumentException If the authorisation is invalid.
     */
    public static char[] fromExtended(char[] xauth) {
        final int length = xauth.length;
        if (length % 2 != 0) {
            throw new IllegalArgumentException("Invalid authorisation");
        }
        char[]  auth = new char[length / 2];
        for (int i = 0, j = 0; i < length; ) {
            char c1 = xauth[i++];
            char c2 = xauth[i++];
            auth[j++] = (char)(fromNibble(c1) << 4 | fromNibble(c2));
        }
        return auth;
    }

    private static char toNibble(int i) {
        return (char) ((i >= 10) ? (i - 10 + 'a') : (i + '0'));
    }

    private static int fromNibble(char c) {
        return (c >= 'a') ? (c - 'a' + 10) : (c >= 'A') ? (c - 'A' + 10) : (c - '0');
    }

}
