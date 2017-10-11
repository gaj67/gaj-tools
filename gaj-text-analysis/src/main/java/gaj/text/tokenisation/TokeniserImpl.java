package gaj.text.tokenisation;

import java.util.ArrayList;
import java.util.List;

/*package-private*/ class TokeniserImpl implements Tokeniser {

    @Override
    public SequenceTextSpan tokenise(String text) {
        List<TextSpan> spans = new ArrayList<>();

        // Extract contiguous sub-spans of differing types.
        StringBuilder buf = new StringBuilder();
        TextSpanType type = null;
        int start = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            TextSpanType newType = determineSpanType(c);
            if (type != null && newType != type) {
                spans.add(constructSubSpan(start, buf.toString(), type));
                start += buf.length();
                buf.setLength(0);
            }
            type = newType;
            buf.append(c);
        }

        // Handling any dangling sub-span.
        if (buf.length() > 0) {
            spans.add(constructSubSpan(start, buf.toString(), type));
        }

        return constructSequenceSpan(spans);
    }

    private TextSpanType determineSpanType(char c) {
        if (Character.isWhitespace(c)) return TextSpanType.WHITESPACE;
        if (Character.isAlphabetic(c)) return TextSpanType.ALPHABETIC;
        if (Character.isDigit(c)) return TextSpanType.NUMERIC;
        return TextSpanType.SYMBOLIC;
    }

    private TextSpan constructSubSpan(int start, String text, TextSpanType type) {
        return new TextSpan() {
            @Override
            public int getStart() {
                return start;
            }

            @Override
            public int getEnd() {
                return start + text.length();
            }

            @Override
            public String getText() {
                return text;
            }

            @Override
            public TextSpanType getType() {
                return type;
            }
        };
    }

    private SequenceTextSpan constructSequenceSpan(List<TextSpan> spans) {
        return new SequenceTextSpan() {
            @Override
            public List<TextSpan> getSpans() {
                return spans;
            }
        };
    }

}
