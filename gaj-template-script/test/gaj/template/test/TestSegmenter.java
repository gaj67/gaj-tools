/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.test;

import gaj.template.segment.ModifiableTextSegment;
import gaj.template.segment.SegmentFactory;
import gaj.template.text.DelimiterInfo;
import gaj.template.text.TextIOFactory;
import gaj.template.text.TextInput;
import gaj.template.text.TextSegmenter;
import gaj.template.text.TextSegmenterFactory;
import java.io.FileReader;
import java.io.IOException;

public class TestSegmenter {

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Arguments: <text-file-path> <delimiter-string> {<delimiter-string>}");
        } else {
            String[] delimiters = new String[args.length-1];
            System.arraycopy(args, 1, delimiters, 0, delimiters.length);
            process(args[0], delimiters);
        }
    }

    private static void process(String path, String[] delimiters) throws IOException {
        TextInput stream = TextIOFactory.newInput(new FileReader(path));
        TextSegmenter segmenter = TextSegmenterFactory.newSegmenter(stream);
        while (true) {
            System.out.println("----- text-block -----");
            ModifiableTextSegment textBlock = SegmentFactory.newTextSegment();
            DelimiterInfo info = segmenter.segment(textBlock, delimiters);
            System.out.println(textBlock.toString());
            System.out.println("----- delimiter -----");
            System.out.printf("Group: %d, Index: %d, Delimiter: %s%n",
                    info.getGroup(), info.getIndex(),
                    (info.getDelimiter() == null) ? "null" : "\"" + info.getDelimiter() + "\"");
            if (info.getIndex() < 0) break;
        }
    }
}
