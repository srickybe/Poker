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
public class CircularList<T> {

    public Node<T> head;

    public CircularList() {
    }

    public void add(T data) {
        if (head != null) {
            Node node = new Node(head.previous, data, head);
            head.previous.next = node;
            head.previous = node;
        } 
        else {
            head = new Node(head, data, head);
        }
    }

    public boolean contains(T data) {
        if (!isEmpty()) {
            if (head.data == data) {
                return true;
            }

            Node cur = head.next;

            while (cur != head) {
                if (cur.data == data) {
                    return true;
                }

                cur = cur.next;
            }

            return false;
        }
        return false;
    }

    public boolean isEmpty() {
        return head == null;
    }
}

