/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker;

/**
 *
 * @author ricky
 */
public class CircularListIterator<T> {

    private Node<T> current;

    public CircularListIterator(Node<T> head) {
        current = head;
    }

    public boolean hasNext() {
        return true;
    }

    public T next() {
        T data = current.data;
        current = current.next;

        return data;
    }
}
