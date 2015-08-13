package br.com.smartsy.fwj.util;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public final class CastUtil {

	private CastUtil() {
	}

	public static <T, U> Map<T, U> cast(Map<?, ?> p) {
		return (Map<T, U>) p;
	}

	public static <T, U> Map<T, U> cast(Map<?, ?> p, Class<T> t, Class<U> u) {
		return (Map<T, U>) p;
	}

	public static <T, U> Dictionary<T, U> cast(Dictionary<?, ?> p) {
		return (Dictionary<T, U>) p;
	}

	public static <T, U> Dictionary<T, U> cast(Dictionary<?, ?> p, Class<T> t, Class<U> u) {
		return (Dictionary<T, U>) p;
	}

	public static <T> Collection<T> cast(Collection<?> p) {
		return (Collection<T>) p;
	}

	public static <T> Collection<T> cast(Collection<?> p, Class<T> cls) {
		return (Collection<T>) p;
	}

	public static <T> List<T> cast(List<?> p) {
		return (List<T>) p;
	}

	public static <T> List<T> cast(List<?> p, Class<T> cls) {
		return (List<T>) p;
	}

	public static <T> Iterator<T> cast(Iterator<?> p) {
		return (Iterator<T>) p;
	}

	public static <T> Iterator<T> cast(Iterator<?> p, Class<T> cls) {
		return (Iterator<T>) p;
	}

	public static <T> Enumeration<T> cast(Enumeration<?> p) {
		return (Enumeration<T>) p;
	}

	public static <T> Enumeration<T> cast(Enumeration<?> p, Class<T> cls) {
		return (Enumeration<T>) p;
	}

	public static <T> Set<T> cast(Set<?> p) {
		return (Set<T>) p;
	}

	public static <T> Set<T> cast(Set<?> p, Class<T> cls) {
		return (Set<T>) p;
	}

	public static <T, U> Map.Entry<T, U> cast(Map.Entry<?, ?> p) {
		return (Map.Entry<T, U>) p;
	}

	public static <T, U> Map.Entry<T, U> cast(Map.Entry<?, ?> p, Class<T> pc, Class<U> uc) {
		return (Map.Entry<T, U>) p;
	}

}
