package main.service;

public  class Node<T> {
    public T data;
    public Node<T> next;
    public Node<T> prev;

    public Node(Node<T> prev, T task, Node<T> next) {
        this.data = task;
        this.next = next;
        this.prev = prev;
    }
}