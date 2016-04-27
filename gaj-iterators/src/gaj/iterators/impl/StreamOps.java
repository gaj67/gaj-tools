package gaj.iterators.impl;

import gaj.iterators.core.StreamOp;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class StreamOps {

	private StreamOps() {}

	public static <T> StreamOp<T,T> newFilterOp(Predicate<? super T> predicate) {
		return new StreamOp<T, T>() {
			@SuppressWarnings("unchecked")
			@Override
			public Stream<T> apply(Stream<? extends T> stream) {
				return (Stream<T>) stream.filter(predicate);
			}
		};
	}

    @SafeVarargs
	public static <T> Predicate<? super T> newAndPredicate(Predicate<? super T>... predicates) {
    	if (predicates.length == 0) {
        	return new Predicate<T>() {
    			@Override
    			public boolean test(T t) {
    				return true;
    			}
    		};
    	}
    	if (predicates.length == 1) {
    		return predicates[0];
    	}
    	return new Predicate<T>() {
			@Override
			public boolean test(T t) {
				for (Predicate<? super T> predicate : predicates) {
					if (!predicate.test(t)) return false;
				}
				return true;
			}
		};
    }

	public static <T> Predicate<? super T> newAndPredicate(Iterable<? extends Predicate<? super T>> predicates) {
    	return new Predicate<T>() {
			@Override
			public boolean test(T t) {
				for (Predicate<? super T> predicate : predicates) {
					if (!predicate.test(t)) return false;
				}
				return true;
			}
		};
    }

	@SafeVarargs
	public static <T> StreamOp<T,T> newAndOp(Predicate<? super T>... predicates) {
		return newFilterOp(newAndPredicate(predicates));
	}

	public static <T> StreamOp<T,T> newAndOp(Iterable<? extends Predicate<? super T>> predicates) {
		return newFilterOp(newAndPredicate(predicates));
	}

    @SafeVarargs
	public static <T> Predicate<? super T> newOrPredicate(Predicate<? super T>... predicates) {
    	if (predicates.length == 0) {
        	return new Predicate<T>() {
    			@Override
    			public boolean test(T t) {
    				return false;
    			}
    		};
    	}
    	if (predicates.length == 1) {
    		return predicates[0];
    	}
    	return new Predicate<T>() {
			@Override
			public boolean test(T t) {
				for (Predicate<? super T> predicate : predicates) {
					if (predicate.test(t)) return true;
				}
				return false;
			}
		};
    }

	public static <T> Predicate<? super T> newOrPredicate(Iterable<? extends Predicate<? super T>> predicates) {
    	return new Predicate<T>() {
			@Override
			public boolean test(T t) {
				for (Predicate<? super T> predicate : predicates) {
					if (predicate.test(t)) return true;
				}
				return false;
			}
		};
    }

	@SafeVarargs
	public static <T> StreamOp<T,T> newOrOp(Predicate<? super T>... predicates) {
		return newFilterOp(newOrPredicate(predicates));
	}

	public static <T> StreamOp<T,T> newOrOp(Iterable<? extends Predicate<? super T>> predicates) {
		return newFilterOp(newOrPredicate(predicates));
	}

	public static <T,R> StreamOp<T,R> newMapOp(Function<? super T,? extends R> function) {
		return new StreamOp<T, R>() {
			@SuppressWarnings("unchecked")
			@Override
			public Stream<R> apply(Stream<? extends T> stream) {
				return (Stream<R>) stream.map(function);
			}
		};
	}

}
