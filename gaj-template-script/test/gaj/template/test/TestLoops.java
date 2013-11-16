/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.test;

import gaj.template.data.ScriptData;
import gaj.template.data.ScriptDataException;
import gaj.template.data.ScriptDataFactory;
import gaj.template.parser.ScriptParserFactory;
import gaj.template.script.ScriptTemplate;
import gaj.template.text.TextIOFactory;
import gaj.template.text.TextInput;
import gaj.template.text.TextOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import org.junit.Assert;
import org.junit.Test;

public class TestLoops {

    /**
     * Test for-loop with simple text.
     */
    @Test
    public void testForLoopSimple() {
        System.out.printf("%s::testForLoopSimple", this.getClass().getSimpleName());
        ScriptTemplate template = parseScript("/*loopTest*/@@for(X)Text@@next()/*done!*/");
        {
            System.out.printf("%nTesting with X = 5... "); System.out.flush();
            ScriptData data= ScriptDataFactory.newData("X", 5);
            StringBuilder buf = embedData(template, data);
            Assert.assertEquals("TextTextTextTextText", buf.toString());
        } {
            System.out.printf("%nTesting with X = 1... "); System.out.flush();
            ScriptData data= ScriptDataFactory.newData("X", 1);
            StringBuilder buf = embedData(template, data);
            Assert.assertEquals("Text", buf.toString());
        } {
            System.out.printf("%nTesting with X = 0... "); System.out.flush();
            ScriptData data= ScriptDataFactory.newData("X", 0);
            StringBuilder buf = embedData(template, data);
            Assert.assertEquals("", buf.toString());
        } {
            System.out.printf("%nTesting with X = -1... "); System.out.flush();
            ScriptData data= ScriptDataFactory.newData("X", -1);
            StringBuilder buf = embedData(template, data);
            Assert.assertEquals("", buf.toString());
        } {
            System.out.printf("%nTesting with X = undef (expect exception)... "); System.out.flush();
            try {
                ScriptData data= ScriptDataFactory.newData();
                @SuppressWarnings("unused")
                StringBuilder buf = embedData(template, data);
                Assert.assertTrue("Expected failure", false);
            } catch (ScriptDataException e) {}
        } {
            System.out.printf("%nTesting with X = Object (expect exception)... "); System.out.flush();
            try {
                ScriptData data= ScriptDataFactory.newData("X", new Object());
                @SuppressWarnings("unused")
                StringBuilder buf = embedData(template, data);
                Assert.assertTrue("Expected failure", false);
            } catch (ScriptDataException e) {}
        } {
            System.out.printf("%nTesting with X = <ScriptData>({X:\"A\"}, {Y:\"B\"}, {Z:3})... "); System.out.flush();
            Collection<ScriptData> loopList = Arrays.asList(
                    ScriptDataFactory.newData("X", "A"),
                    ScriptDataFactory.newData("Y", "B"),
                    ScriptDataFactory.newData("Z", 3)
                    );
            ScriptData data= ScriptDataFactory.newData("X", loopList); 
            StringBuilder buf = embedData(template, data);
            Assert.assertEquals("TextTextText", buf.toString());
        } {
            System.out.printf("%nTesting with X = <Object>()... "); System.out.flush();
            ScriptData data= ScriptDataFactory.newData("X", new LinkedList<Object>());
            StringBuilder buf = embedData(template, data);
            Assert.assertEquals("", buf.toString());
        } {
            System.out.printf("%nTesting with X = <Object>(Object) (expect exception)... "); System.out.flush();
            try {
                ScriptData data= ScriptDataFactory.newData("X", Arrays.asList(new Object()));
                @SuppressWarnings("unused")
                StringBuilder buf = embedData(template, data);
                Assert.assertTrue("Expected failure", false);
            } catch (ScriptDataException e) {}
        }
        System.out.println("\nPassed!");
    }

    /**
     * Test for-loop with loop-variable substitution.
     */
    @Test
    public void testForLoopVars() {
        System.out.printf("%s::testForLoopVars", this.getClass().getSimpleName());
        ScriptTemplate template = parseScript("@@for(X)${X.SIZE}S${X.COUNT}C${X.other=?}@@next()@@set(X.other=10)");
        {
            System.out.printf("%nTesting with X = 5... "); System.out.flush();
            ScriptData data= ScriptDataFactory.newData("X", 5);
            StringBuilder buf = embedData(template, data);
            Assert.assertEquals("5S1C?5S2C?5S3C?5S4C?5S5C?", buf.toString());
            Assert.assertEquals("10", data.getProperty("X.other"));
        } {
            System.out.printf("%nTesting with X = 2, X.other = 3... "); System.out.flush();
            ScriptData data= ScriptDataFactory.newData("X", 2, "X.other", 3);
            StringBuilder buf = embedData(template, data);
            Assert.assertEquals("2S1C32S2C3", buf.toString());
            Assert.assertEquals("10", data.getProperty("X.other"));
        } {
            System.out.printf("%nTesting with X = <ScriptData>({other:\"A\"}, {other:\"B\"}, {either:3})... "); System.out.flush();
            Collection<ScriptData> loopList = Arrays.asList(
                    ScriptDataFactory.newData("other", "A"),
                    ScriptDataFactory.newData("other", "B"),
                    ScriptDataFactory.newData("either", 3)
                    );
            ScriptData data= ScriptDataFactory.newData("X", loopList, "X.uther", 42); 
            StringBuilder buf = embedData(template, data);
            Assert.assertEquals("3S1CA3S2CB3S3C?", buf.toString());
            Assert.assertEquals(42, data.getProperty("X.uther"));
            Assert.assertEquals(null, data.getProperty("X.either"));
        }
        System.out.println("\nPassed!");
    }

    private StringBuilder embedData(ScriptTemplate template, ScriptData data) {
        StringBuilder buf = new StringBuilder();
        TextOutput output = TextIOFactory.newOutput(buf);
        template.embed(data, output);
        return buf;
    }

    private ScriptTemplate parseScript(String script) {
        TextInput input = TextIOFactory.newInput(script);
        ScriptTemplate template = ScriptParserFactory.newParser().parse(input);
        return template;
    }

    /**
     * Test of counting for-loop.
     * @throws IOException
     */
    //@Test
    public void testForLoopCounter() throws IOException {
    }

    //@Test
    public void testLoopWithGlobals() throws IOException {
        System.out.printf("%s::testLoopWithGlobals%n", this.getClass().getSimpleName());
        /*
        System.out.printf("Testing... "); System.out.flush();
        data.setProperty("X", -1); // Set X globally - X will also be locally set in the template.
        String output = embedTemplate(templateFile, data);
        Assert.assertEquals("[3]1[foo]42[3]2[foo]42[3]3[foo]42", output);
        Assert.assertEquals(-1, data.getProperty("X")); // Check that the global X hasn't changed.
        Assert.assertNull(data.getProperty("Y")); // Check that locally set Y is not global.
         */
        System.out.printf("Passed!%n");
    }

}
