/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.text;

import java.io.IOException;

/*package-private*/ class TextOutputImpl implements TextOutput {

    private final TextWriter writer;
    private int numWritten = 0;

    /*package-private*/ TextOutputImpl(TextWriter writer) {
        this.writer = writer;
    }

    @Override
    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void append(String output) throws UncheckedIOException {
        try {
            writer.write(output);
            numWritten += output.length();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public int numWritten() {
        return numWritten;
    }

    @Override
    public boolean isEmpty() {
        return numWritten == 0;
    }

    @Override
    public void append(char output) throws UncheckedIOException {
        try {
            writer.write(output);
            numWritten++;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
