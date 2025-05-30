package utils;

import java.util.*;

/**
 * This is totally overkill and i have no idea if all of it works, it behaves like a set and should have a listener attached to it, so have fun ig ?
 * @param <E>
 * @author Stryckoeurzzz
 */
public class ObservableSet<E> implements Set<E> {
    private Set<E> internalSet = new HashSet<>();
    private final List<SetChangeListener<E>> listeners = new ArrayList<>();

    public interface SetChangeListener<E> {
        void onAdd(E element);
        void onRemove(E element);
    }

    public ObservableSet(Set<E> backingSet) {
        this.internalSet = backingSet;
    }
    
    public void addListener(SetChangeListener<E> listener) {
        listeners.add(listener);
    }

    public void removeListener(SetChangeListener<E> listener) {
        listeners.remove(listener);
    }

    private void notifyAdd(E element) {
        for (SetChangeListener<E> listener : listeners) {
            listener.onAdd(element);
        }
    }

    private void notifyRemove(E element) {
        for (SetChangeListener<E> listener : listeners) {
            listener.onRemove(element);
        }
    }

    @Override
    public boolean add(E e) {
        if (internalSet.add(e)) {
            notifyAdd(e);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        if (internalSet.remove(o)) {
            @SuppressWarnings("unchecked")
            E e = (E) o;
            notifyRemove(e);
            return true;
        }
        return false;
    }

    @Override public int size() { return internalSet.size(); }
    @Override public boolean isEmpty() { return internalSet.isEmpty(); }
    @Override public boolean contains(Object o) { return internalSet.contains(o); }
    @Override public Iterator<E> iterator() { return internalSet.iterator(); }
    @Override public Object[] toArray() { return internalSet.toArray(); }
    @Override public <T> T[] toArray(T[] a) { return internalSet.toArray(a); }
    @Override public boolean containsAll(Collection<?> c) { return internalSet.containsAll(c); }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            changed |= add(e);
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Iterator<E> it = internalSet.iterator();
        while (it.hasNext()) {
            E e = it.next();
            if (!c.contains(e)) {
                it.remove();
                notifyRemove(e);
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            changed |= remove(o);
        }
        return changed;
    }

    @Override
    public void clear() {
        Iterator<E> it = internalSet.iterator();
        while (it.hasNext()) {
            E e = it.next();
            it.remove();
            notifyRemove(e);
        }
    }
}
