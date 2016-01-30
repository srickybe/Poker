/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker;

/**
 *
 * @author ricky
 * @param <T>
 */
public class Node<T> {

    public Node<T> previous;
    public T data;
    public Node<T> next;

    public Node(Node prev, T data, Node next) {
        this.previous = prev;
        this.data = data;
        this.next = next;
    }
}
