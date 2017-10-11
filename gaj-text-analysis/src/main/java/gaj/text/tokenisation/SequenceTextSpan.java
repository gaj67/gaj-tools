package gaj.text.tokenisation;

import java.util.List;

/**
 * Represents a contiguous sequence of text spans.
 */
public interface SequenceTextSpan extends TextSpan {

    /**
     * Obtains the list of text spans comprising the sequence.
     * 
     * @return The list of sub-spans.
     */
    List<TextSpan> getSpans();

    /**
     * Indicates whether or not the sub-spans truly represent a contiguous
     * sequence. This only checks the starting and ending positions of the
     * sub-spans, not that their text combines correctly.
     * 
     * @return A value of null if the sub-spans are contiguous, or an error
     *         message if they are not.
     */
    @Override
    default /*@Nullable*/ String validate() {
        int end = 0;
        List<TextSpan> spans = getSpans();
        if (spans == null) return "Null sequence";
        for (int i = 0; i < spans.size(); i++) {
            TextSpan span = spans.get(i);
            String err = span.validate();
            if (err != null)
                return "Sub-span [" + i + "]: " + err;
            if (i > 0 && span.getStart() != end)
                return "Sub-span [" + i + "]: Not adjacent to the previous sub-span";
            end = span.getEnd();
        }
        return null;
    }

    @Override
    default TextSpanType getType() {
        return TextSpanType.SEQUENCE;
    }

    @Override
    default String getText() {
        StringBuilder buf = new StringBuilder();
        for (TextSpan span : getSpans()) {
            buf.append(span.getText());
        }
        return buf.toString();
    }

    @Override
    default int getStart() {
        List<TextSpan> spans = getSpans();
        return spans.isEmpty() ? 0 : spans.get(0).getStart();
    }

    @Override
    default int getEnd() {
        List<TextSpan> spans = getSpans();
        return spans.isEmpty() ? 0 : spans.get(spans.size() - 1).getEnd();
    }

}
