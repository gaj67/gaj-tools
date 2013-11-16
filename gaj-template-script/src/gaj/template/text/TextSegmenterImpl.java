/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.text;

import gaj.template.segment.ModifiableTextSegment;




/**
 * Allows segmentation of a text stream.
 */
/*package-private*/ class TextSegmenterImpl implements TextSegmenter {

    private final TextInput stream;
    private static final DelimiterInfo NULL_RESULT = new DelimiterInfo() {
        @Override
        public int getIndex() {
            return -1;
        }

        @Override
        public int getGroup() {
            return -1;
        }

        @Override
        public String getDelimiter() {
            return null;
        }

    };

    /**
     * Binds the segmenter to the given text stream.
     * 
     * @param input - The reader for the text to be segmented.
     */
    /*package-private*/ TextSegmenterImpl(TextInput stream) {
        this.stream = stream;
    }

    @Override
    public DelimiterInfo segment(ModifiableTextSegment textBlock, String... delimiters) {
        final int numDelimiters = delimiters.length;
        while (stream.hasNext()) {
            textBlock.append(stream.next());
            for (int i = 0; i < numDelimiters; i++) {
                final String delimiter = delimiters[i];
                if (textBlock.endsWith(delimiter)) {
                    textBlock.unwind(delimiter.length());
                    final int idx = i;
                    return new DelimiterInfo() {
                        @Override
                        public int getIndex() {
                            return idx;
                        }

                        @Override
                        public int getGroup() {
                            return 0;
                        }

                        @Override
                        public String getDelimiter() {
                            return delimiter;
                        }
                    };
                }
            }
        }
        return NULL_RESULT; // End of stream.
    }

    @Override
    public DelimiterInfo segment(final ModifiableTextSegment textBlock, final String[]... delimiters) {
        final int numGroups = delimiters.length;
        while (stream.hasNext()) {
            textBlock.append(stream.next());
            for (int i = 0; i < numGroups; i++) {
                String[] groupOfDelimiters = delimiters[i];
                final int numDelimiters = groupOfDelimiters.length;
                for (int j = 0; j < numDelimiters; j++) {
                    final String delimiter = groupOfDelimiters[j];
                    if (textBlock.endsWith(delimiter)) {
                        textBlock.unwind(delimiter.length());
                        final int group = i, idx = j;
                        return new DelimiterInfo() {
                            @Override
                            public int getIndex() {
                                return idx;
                            }

                            @Override
                            public int getGroup() {
                                return group;
                            }

                            @Override
                            public String getDelimiter() {
                                return delimiter;
                            }
                        };
                    }
                }
            }
        }
        return NULL_RESULT; // End of stream.
    }

    @Override
    public DelimiterInfo segmentEscaped(ModifiableTextSegment textBlock, String escape, boolean strip, String... delimiters) {
        while (true) {
            DelimiterInfo info = segment(textBlock, delimiters);
            String delimiter = info.getDelimiter();
            if (delimiter == null || !textBlock.endsWith(escape))
                return info;
            if (strip) textBlock.unwind(escape.length());
            textBlock.append(delimiter);
        }
    }

    @Override
    public DelimiterInfo segmentEscaped(ModifiableTextSegment textBlock, String escape, boolean strip, String[]... delimiters) {
        while (true) {
            DelimiterInfo info = segment(textBlock, delimiters);
            String delimiter = info.getDelimiter();
            if (delimiter == null || !textBlock.endsWith(escape))
                return info;
            if (strip) textBlock.unwind(escape.length());
            textBlock.append(delimiter);
        }
    }

}
