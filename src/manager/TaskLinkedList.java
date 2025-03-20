package manager;

import java.util.ArrayList;
import java.util.List;

public class TaskLinkedList<T> {

    public Node<T> head;
    public Node<T> tail;


    TaskLinkedList() {
        head = null;
        tail = null;
    }

    Node<T> linkLast(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        return newNode;
    }

    List<T> getTasks() {
        Node<T> task = head;
        List<T> result = new ArrayList<>();
        while (task != null) {
            result.add(task.data);
            task = task.next;
        }
        return result;

    }

    void removeNode(Node<T> node) {
        if (node == null) {
            return;
        }
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
    }
}
