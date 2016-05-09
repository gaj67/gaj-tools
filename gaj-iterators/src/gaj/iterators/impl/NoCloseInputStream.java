package gaj.iterators.impl;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * {@link InputStream} that cannot be closed.
 */
/*package-private*/ class NoCloseInputStream extends FilterInputStream {

	/*package-private*/ NoCloseInputStream(InputStream is) {
		super(is);
	}

	@Override
	public void close() throws IOException {
		// Intentionally left empty. use closeInput() to close
	}

	public void doClose() throws IOException {
		super.close();
	}

}