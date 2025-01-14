package net.pic4pic.ginger.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LRU Cache
 * @param <A> Key
 * @param <B> Data
 */
public class LRUCache<A, B> extends LinkedHashMap<A, B> {
	
	/**
	 * Serialization helper
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Capacity
	 */
	private final int maxEntries;
	
	/**
	 * listener
	 */
	private EntryRemoveListener<A, B> listener;
	
	/**
	 * Constructor
	 * @param maxEntries
	 */
    public LRUCache(final int maxEntries) {
        this(maxEntries, true);
    }
    
    /**
	 * Constructor
	 * @param maxEntries
	 */
    public LRUCache(final int maxEntries, boolean accessOrdered) {
    	this(maxEntries, accessOrdered, null);
    }
    
    public LRUCache(final int maxEntries, boolean accessOrdered, EntryRemoveListener<A, B> listener) {
        super(maxEntries + 1, 1.0f, accessOrdered);
        this.maxEntries = maxEntries;
        this.listener = listener;
    }
    
    /**
     * Returns <tt>true</tt> if this <code>LruCache</code> has more entries than the maximum specified when it was
     * created.
     *
     * <p>
     * This method <em>does not</em> modify the underlying <code>Map</code>; it relies on the implementation of
     * <code>LinkedHashMap</code> to do that, but that behavior is documented in the JavaDoc for
     * <code>LinkedHashMap</code>.
     * </p>
     *
     * @param eldest
     *            the <code>Entry</code> in question; this implementation doesn't care what it is, since the
     *            implementation is only dependent on the size of the cache
     * @return <tt>true</tt> if the oldest
     * @see java.util.LinkedHashMap#removeEldestEntry(Map.Entry)
     */
    @Override
    protected boolean removeEldestEntry(final Map.Entry<A, B> eldest) {
        boolean decision = super.size() > maxEntries;
        if(decision && this.listener != null){
        	this.listener.onRemovingEntry(eldest.getKey(), eldest.getValue());
        }
        return decision;
    }
    
    public interface EntryRemoveListener<A, B>{
    	void onRemovingEntry(A key, B data);
    }    
}

