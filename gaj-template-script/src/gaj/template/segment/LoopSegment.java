/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.segment;


public class LoopSegment extends CommandSegment {

    private final String loopVar;

    /*package-private*/ LoopSegment(String loopVar) {
        super(CommandType.StartLoop);
        this.loopVar = loopVar;
    }

    public String getLoopVariable() {
        return loopVar;
    }

}
