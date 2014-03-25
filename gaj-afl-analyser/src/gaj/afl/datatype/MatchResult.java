package gaj.afl.datatype;
import java.util.HashMap;
import java.util.Map;

public enum MatchResult {
	win("dftd"), loss("lost to"), draw("drew");

	private final String externalValue;

	MatchResult(String value) {
	   this.externalValue = value;
	}

	public String toExternal() {
	   return externalValue;
	}

   private static final Map<String, MatchResult> map = new HashMap<String, MatchResult>();
   static {
      for (MatchResult r : values()) map.put(r.externalValue, r);
   }

	public static MatchResult fromExternal(String value) {
		MatchResult r = map.get(value);
		if (r == null) throw new IllegalArgumentException("Unknown type: " + value);
		return r;
	}

	public int getClassification() {
	   return this.ordinal();
	}

	/**
	 * Indicates the match status from the other team's point of view.
	 * 
	 * @return
	 */
	public MatchResult reverse() {
		switch (this) {
			case draw:
				return draw;
			case loss:
				return win;
			case win:
				return loss;
			default:
				throw new IllegalStateException("Unknown match result: " + this);
		}
	}
};
