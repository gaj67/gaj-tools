/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.segment;


public class CommandSegment extends Segment {

    private final CommandType type;

    /*package-private*/ CommandSegment(CommandType type) {
        super(SegmentType.Command);
        this.type = type;
    }
    
    /**
     * Specifies the script command embodied by the segment.
     * 
     * @return The command type.
     */
    public CommandType getCommandType() {
        return type;
    }

    @Override
    public SegmentType getType() {
        return SegmentType.Command;
    }

    @Override
    public boolean isEmpty() {
        return false; // By default, commands will have or imply side-effects.
    }

}
