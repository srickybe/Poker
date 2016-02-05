/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker;

import java.util.ListIterator;

/**
 *
 * @author ricky
 * @param <E>
 */
public class CircularListIterator<E> {

    private final ListIterator<E> iterator;

    public CircularListIterator(ListIterator<E> iterator) {
        this.iterator = iterator;
    }

    public boolean hasNext() {
        if (!iterator.hasNext()) {
            while (iterator.hasPrevious()) {
                iterator.previous();
            }
        }

        return iterator.hasNext();
    }

    public boolean hasPrevious() {
        if (!iterator.hasPrevious()) {
            while (iterator.hasNext()) {
                iterator.next();
            }
        }

        return iterator.hasPrevious();
    }

    public E next() {
        if (hasNext()) {
            return iterator.next();
        }

        return iterator.next();
    }

    public E previous() {
        if (hasPrevious()) {
            return iterator.previous();
        }

        return iterator.previous();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final CircularListIterator<?> other = (CircularListIterator<?>) obj;
        
        return this.iterator.equals(other.iterator);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }
    
    
}
