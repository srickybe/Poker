/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker;

import java.util.Objects;

/**
 *
 * @author ricky
 * @param <T>
 */
public class CircularListIterator<T> {

    private Node<T> current;

    public CircularListIterator(Node<T> head) {
        current = head;
    }

    public boolean hasNext() {
        return current != null;
    }

    public T next() {
        T data = current.data;
        current = current.next;

        return data;
    }

    boolean moveTo(T data){
        if(current == null){
            return false;
        }
        
        if(current.data.equals(data)){
            return false;
        }
        
        Node<T> cur = current.next;
        
        while(cur != current){
            if(cur.data.equals(data)){
                return true;
            }
            
            cur = cur.next;
        } 
        
        return false;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.current);
        return hash;
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
        
        return Objects.equals(this.current, other.current);
    }  
}
