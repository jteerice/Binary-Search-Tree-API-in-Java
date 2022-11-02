import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SymbolTable<Key extends Comparable<Key>, Value> implements ISymbolTable<Key , Value> {
    private class Node{
        Key key;            /* The key that the node will store */
        Value val;          /* The value the node will store */
        Node left, right;   /* pointers to the left and right children of this node */
        int size;           /* The number of nodes in the subtree rooted by this node */
        public Node(Key k, Value v, int s){
            this.key = k;
            this.val = v;
            this.size = s;
        }
    }

    /* the root of our tree */
    Node root;

    public void put(Key k, Value v) throws InvalidParameterException {
        if (k == null) throw new InvalidParameterException("no key was passed");
        if (v == null) throw new InvalidParameterException("no value was passed");
        root = put(root, k, v);
    }

    private Node put(Node n, Key k, Value v) {
        Node newNode = new Node(k, v, 1);

        if (n == null) return newNode;

        int cmp = k.compareTo(n.key);
        if (cmp < 0) n.left = put(n.left, k, v);
        else if (cmp > 0) n.right = put(n.right, k, v);
        else throw new InvalidParameterException("duplicate key");
        n.size = 1 + size(n.left) + size(n.right);

        return n;
    }

    public Value get(Key k) throws NoSuchElementException, InvalidParameterException {
        if (k == null) throw new InvalidParameterException("no key was passed");
        Value valCheck = get(root, k).val;
        if (valCheck == null) throw new NoSuchElementException("Key doesn't exist");
        else return valCheck;
    }

    private Node get(Node n, Key k) {
        if (n == null) throw new NoSuchElementException("key doesn't exist");

        int cmp = k.compareTo(n.key);
        if (cmp < 0) return get(n.left, k);
        else if (cmp > 0) return get(n.right, k);
        else return n;
    }

    public void del(Key k) throws NoSuchElementException {
        if(k == null) { throw new InvalidParameterException("no key was passed"); }
        root = del(root, k);
    }

    private Node del(Node n, Key k) {
        if (n == null) throw new NoSuchElementException("key doesn't exist");
        int cmp = k.compareTo(n.key);
        if (cmp < 0) n.left = del(n.left, k);
        else if (cmp > 0) n.right = del(n.right, k);
        // Found the node to delete
        else {
            if (n.left == null && n.right == null) return null; // No children
            else if (n.left == null) return n.right;            // Right only child
            else if (n.right == null) return n.left;            // Left only child
            else {                                              // Two children
                Node tmp = n;
                n = findMin(tmp.right);
                n.right = deleteMin(tmp.right);
                n.left = tmp.left;
            }
        }
        n.size = 1 + size(n.left) + size(n.right);
        return n;
    }

    private Node findMin(Node n) {
        if (n.left == null) return n;
        return findMin(n.left);
    }

    private Node deleteMin(Node n) {
        if (n.left == null) return n.right;
        n.left = deleteMin(n.left);
        n.size = 1 + size(n.left) + size(n.right);
        return n;
    }


    public boolean contains(Key k){
        if (k == null) throw new InvalidParameterException("key is null");
        if (root == null) return false;
        Node n = contains(root, k);

        return n != null;
    }

    private Node contains(Node n, Key k) {
        if (n == null) return null;
        int cmp = k.compareTo(n.key);
        if (cmp < 0) return contains(n.left, k);
        else if (cmp > 0) return contains(n.right, k);
        else return n;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        else return x.size;
    }

    public LinkedList<Key> keys() {
        LinkedList<Key> list = new LinkedList<>();
        keys(root, list);
        return list;
    }

    private void keys(Node n, LinkedList<Key> list) {
        if (n == null) return;
        keys(n.left, list);
        list.add(n.key);
        keys(n.right, list);
    }
