package gaj.text.tokenisation;

/**
 * Provides access to text tokenisation methods.
 */
public abstract class TokeniserFactory {

    private TokeniserFactory() {}
    
    public static Tokeniser getTokeniser() {
        return new TokeniserImpl();
    }

}
