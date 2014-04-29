/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.segment;

import gaj.iterators.core.IteratorFactory;
import gaj.template.data.Embedder;
import gaj.template.data.ScriptData;
import gaj.template.data.ScriptDataFactory;
import gaj.template.text.TextOutput;
import gaj.template.text.UncheckedIOException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Encapsulates an entire loop block of the form:
 * <p/>{@link CommandType#StartLoop} { {@link Segment} } {@link CommandType#EndLoop}.
 *
 */
/*package-private*/ class LoopBlockSegment extends Segment {

    public static final String LOOP_COUNT_PROPERTY = "COUNT";
    public static final String LOOP_SIZE_PROPERTY = "SIZE";
    private static final String LOOP_PROPERTY_SEPARATOR = ".";

    private final LoopSegment cmd;
    private final Embedder block;
    private final String loopSep = LOOP_PROPERTY_SEPARATOR; // XXX: If configurable, pass into constructor!

    /*package-private*/ LoopBlockSegment(LoopSegment cmd, Embedder block) {
        super(SegmentType.Block);
        this.cmd = cmd;
        this.block = block;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean embed(ScriptData data, TextOutput output) throws UncheckedIOException {
        // Determine type of loop - either collection or counter.
        String loopName = cmd.getLoopVariable();
        Object loopData = data.getProperty(loopName);
        if (loopData == null)
            throw ScriptDataFactory.newException("Undefined loop property: " + loopName);
        Iterator<? extends Object> iter;
        int loopSize;
        if (loopData instanceof Collection<?>) {
            @SuppressWarnings("unchecked")
            Collection<? extends Object> list = (Collection<? extends Object>)loopData;
            loopSize = list.size();
            iter = list.iterator();
        } else {
            loopSize = ScriptDataFactory.toInt(loopData);
            if (loopSize == Integer.MIN_VALUE)
                throw ScriptDataFactory.newException("Invalid loop property: " + loopName + " = " + loopData);
            iter = IteratorFactory.newIterator(ScriptDataFactory.newData(), loopSize);
        }
        // Execute loop.
        final String loopPrefix = loopName + loopSep;
        Map<String,Object> keep = getLoopLikeProperties(data, loopPrefix); // In case we trash them.
        boolean isOutput = false;
        for (int loopCounter = 1; loopCounter <= loopSize; loopCounter++) {
            Object loopItem = iter.next();
            if (!(loopItem instanceof ScriptData))
                throw ScriptDataFactory.newException(
                        String.format("Invalid loop item: %s[%d] = %s", loopName, loopCounter-1, loopItem));                
            setLoopProperties(data, (ScriptData)loopItem, loopPrefix, loopCounter, loopSize);
            isOutput |= block.embed(data, output);
            unsetLoopProperties(data, loopPrefix, keep); // Wipe out local properties and reset preserved ones.
        }
        return isOutput;
    }

    private Map<String,Object> getLoopLikeProperties(ScriptData data, String loopPrefix) {
        Map<String,Object> map = new HashMap<>();
        for (String key : data.getPropertyNames()) {
            if (key.startsWith(loopPrefix))
                map.put(key, data.getProperty(key));
        }
        return map;
    }

    private void setLoopProperties(ScriptData data, ScriptData loopItem, String loopPrefix, int loopCount, int loopSize) {
        for (String key : loopItem.getPropertyNames()) {
            data.setProperty(loopPrefix + key, loopItem.getProperty(key));
        }
        data.setProperty(loopPrefix + LOOP_COUNT_PROPERTY, loopCount);
        data.setProperty(loopPrefix + LOOP_SIZE_PROPERTY, loopSize);
    }

    private void unsetLoopProperties(ScriptData data, String loopPrefix, Map<String,Object> map) {
        for (String key : data.getPropertyNames()) {
            if (key.startsWith(loopPrefix)) 
                data.setProperty(key, null);
        }
        for (Entry<String,Object> entry : map.entrySet()) {
            data.setProperty(entry.getKey(), entry.getValue());
        }
    }

}
