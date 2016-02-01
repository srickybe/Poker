/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker;

import java.util.ArrayList;

/**
 *
 * @author ricky
 * @param <E>
 */
public class CircularList<E> {

    private final ArrayList<E> list;

    public CircularList() {
        list = new ArrayList<>();
    }

    public boolean add(E data) {
        return list.add(data);
    }

    public boolean contains(E data) {
        return list.contains(data);
    }

    public int indexOf(Object obj) {
        return list.indexOf(obj);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public E get(int index) {
        return list.get(index);
    }

    public CircularListIterator<E> listIterator() {
        return new CircularListIterator<>(list.listIterator());
    }

    CircularListIterator<E> listIterator(int index) {
        return new CircularListIterator(list.listIterator(index));
    }

    public int size() {
        return list.size();
    }
    
    public static void main(String args[]){
        CircularList<Double> ints = new CircularList<>();
        
        for(double i = 0; i < 10; ++i){
            ints.add(i * 1.0e6 + 100 * Math.random());
        }
        
        CircularListIterator<Double> it = ints.listIterator();
        
        for(int i = 0; i < 30; ++i){
            System.out.println("Forward: i = " + i + ": " + it.next());
        }
        
        for(int i = 0; i < 30; ++i){
            System.out.println("Backwards: i = " + i + ": " + it.previous());
        }
        
        for(int i = 0; i < 30; ++i){
            System.out.println("i = " + i + ": " + 
                    ((i<15)? it.next(): it.previous()));
        }
        
        System.out.println("ints.get(9) = " + ints.get(9));
        
        int res;
        
        try{
            res = ints.indexOf(-1000);
            System.out.println("res = " + res);
            res = ints.indexOf(4);
            System.out.println("res = " + res);
        }
        catch(Exception e){
            System.out.println(e);
        }
        
        for(int i = 0; i < 1; ++i){
            CircularListIterator i2 = ints.listIterator(9);
            System.out.println("i2.next() = " + i2.next());
            System.out.println("ints.indexOf(7e6) = " + ints.indexOf(ints.get(7)));
        }
    }
}
