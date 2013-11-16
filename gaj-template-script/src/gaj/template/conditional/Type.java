/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.conditional;

import java.util.Collection;
import java.util.Date;

/**
 * Used to optionally cast or convert a named property into the desired type.
 * <pre><em>TYPE</em> := <em>INTEGER_TYPE</em> &#124; <em>DOUBLE_TYPE</em> &#124; <em>DATE_TYPE</em> &#124; <em>LOOP_TYPE</em>
 *
 *<em>INTEGER_TYPE</em> := "i"; <em>DOUBLE_TYPE</em> := "d"; <em>DATE_TYPE</em> := "D"; <em>LOOP_TYPE</em> := "L"</pre>
 */
public enum Type {

    INTEGER("i", Integer.class),
    DOUBLE("d", Double.class),
    DATE("D", Date.class),
    LOOP_LIST("L", Collection.class);

    public final String value;
    public final Class<?> type;

    private Type(String value, Class<?> type) {
        this.value = value;
        this.type = type;
    }
}