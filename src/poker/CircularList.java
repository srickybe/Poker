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
public class CircularList<T> {

    public Node<T> head;

    public CircularList() {
    }

    public boolean add(T data) {
        if (head != null) {
            Node node = new Node(head.previous, data, head);
            head.previous.next = node;
            head.previous = node;
            
            return true;
        } 
        else {
            head = new Node(null, data, null);
            head.next = head;
            head.previous = head;
            
            return true;
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
    
    public CircularListIterator<T> listIterator(){
        return new CircularListIterator<>(head);
    }
    /*
    /**
     * Returns an iterator it such that it.next() returns data 
     * @param data
     * @return an iterator it such that it.next() returns data if the list
     * contains data, null otherwise or null if the list is empty 
     */
    /*public CircularListIterator<T> listIterator(T data){
        if(isEmpty()){
            return null;
        }
        
        if(head.data.equals(data)){
            return new CircularListIterator<>(head);
        }
        
        Node<T> cur = head.next;
        
        while(cur != head){
            if(cur.data.equals(data)){
                return new CircularListIterator<>(cur);
            }
            
            cur = cur.next;
        }
        
        return null;
    }*/
    
    public int size(){
        if (isEmpty()){
            return 0;
        }
        
        int res = 1;
        Node<T> cur = head.next;
        
        while(cur != head){
            ++res;
            cur = cur.next;
        }
        
        return res;
    }
    
    //TODO Throw an exception when the list is empty
    public T get(int index){
        Node<T> res = head;
        
        for(int i = 0; i < index; ++i){
            res = res.next;
        }
        
        return res.data;
    }
}

