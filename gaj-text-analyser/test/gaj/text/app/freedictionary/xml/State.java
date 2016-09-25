package gaj.text.app.freedictionary.xml;

/**
 * Indicates the possible macro-states that a section handler may take.
 */
/*package-private*/ enum State {

	// Default state:
	/** 
	 * Indicates the initial or final state. 
	 * May transition to {@link #SECTION}. 
	 */
	INIT(null),

	// Structural states:
	/** 
	 * Indicates that processing of a section has started. 
	 * May transition to {@link #INIT}, {@link #WORD}
	 * or {@link #SEGMENT}. 	 
	 */
	SECTION(false),
	/** 
	 * Indicates that a segment is being handled. 
	 * May transition to {@link #SECTION}, {@link #TAG},
	 * {@link #WORD}, {@link #ITEM} or
	 * {@link #SUBITEM}.  
	 */
	SEGMENT(false),
	/** 
	 * Indicates the start of a segment item. 
	 * May transition to {@link #SEGMENT},
	 * {@link TAG}, {@link EXAMPLE} or {@link #SUBITEM}. 
	 */
	ITEM(false),
	/** 
	 * Indicates the start of a sub-item. 
	 * May transition to {@link #ITEM}, {@link #TAG} or {@link #EXAMPLE}. 
	 */
	SUBITEM(false),
	/** 
	 * Indicates that a section or segment word is being obtained. 
	 * May transition to {@link #SECTION} or {@link #SEGMENT},
	 * depending upon context.
	 */

	// Textual states:
	WORD(true),
	/** 
	 * Indicates that a segment, item or sub-item POS tag is being obtained. 
	 * May transition to {@link #SEGMENT}, {@link #ITEM} or {@link #SUBITEM},
	 * depending upon context.
	 */
	TAG(true),
    /** 
     * Indicates that a segment, item or sub-item POS tag feature is being obtained. 
     * May transition to {@link #SEGMENT}, {@link #ITEM} or {@link #SUBITEM},
     * depending upon context.
     */
    TAG_FEATURE(true),
	/** 
	 * Indicates that an item or sub-item example is being obtained. 
	 * May transition to {@link #ITEM} or {@link #SUBITEM}, depending upon context.
	 */
	EXAMPLE(true),
	/**
	 * Indicates that the state history should be rewound to the parent state.
	 */
	REWIND(null),
    /**
     * Indicates some unexpected structural element that should be ignored.
     */
    OTHER(null), 
	;

	private final boolean isTextual;
	private final boolean isStructural;

	private State(Boolean isTextual) {
		this.isTextual = isTextual != null && isTextual;
		this.isStructural = isTextual != null && !isTextual;
		
	}

	/*package-private*/ boolean isTextual() {
		return isTextual;
	}

	/*package-private*/ boolean isStructural() {
		return isStructural;
	}
	
}
