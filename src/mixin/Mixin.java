package mixin;

import java.util.LinkedList;

/**
 * Application-independent support for base classes and mixin classes.
 * Conceptually, an instance of this class has a superclass instance
 * (whose methods are statically bound) and a current receiver
 * (whose methods are dynamically bound).
 */
class Composable<Base> {

    /**
     * The static superclass instance for this object, that is, the next
     * object up the conceptual inheritance hierarchy.
     */
    protected Base zuper;

    /**
     * The dynamic receiver instance for this object, that is, the object
     * at the bottom of the conceptual inheritance hierarchy (beginning
     * of the chain of responsibility).
     */
    protected Base thiz;

    /**
     * Sets the superclass instance for this object.
     */
    @SuppressWarnings("unchecked")
    private void setSuper(Base zuper) {
        this.zuper = zuper;
        System.out.println(this + ".zuper = " + zuper);
    }

    /**
     * Recursively sets the current receiver for this object
     * and its conceptual superclass instances.
     */
    @SuppressWarnings("unchecked")
    private void setThis(Base thiz) {
        this.thiz = thiz;
        System.out.println(this + ".thiz = " + thiz);
        if (zuper instanceof Composable) {
            ((Composable<Base>) zuper).setThis(thiz);
        }
    }

    /**
     * Constructs an instance of an extensible base class.  
     */
    protected Composable() { this(null); }
    
    /**
     * Dynamically extends a conceptual superclass instance.
     * @param zuper the instance of an extensible base class 
     */
    @SuppressWarnings("unchecked")
    protected Composable(Base zuper) {
        setSuper(zuper);
        setThis((Base) this);
    }

    /**
     * Provides access to this instance 
     * viewed as an instance of the underlying argument type.
     * @return the extended object
     */
    protected Base getThis() { return thiz; }
    
    /**
     * Provides access to the conceptual superclass instance
     * viewed as an instance of the underlying argument type.
     * @return the original object
     */
    protected Base getSuper() { return zuper; }
}

/**
 * A basic collection.
 */
interface Collection<Item> {
    void insert(Item item);
    Item remove();
    void clear();
    boolean isEmpty();
}

/**
 * Application-specific support for mixin classes.  Subclasses should only
 * implement those methods in the application-specific interface that they
 * want to refine.  For convenience, it provides default implementations
 * of all methods in the application-specific interface; this simulates
 * method inheritance.
 */
abstract class CollectionMixin<Item> extends Composable<Collection<Item>> implements Collection<Item> {
    protected CollectionMixin(Collection<Item> zuper) { super(zuper); }
    public void insert(Item item) { getSuper().insert(item); }
    public Item remove() { return getSuper().remove(); }
    public void clear() { getSuper().clear(); }
    public boolean isEmpty() { return getSuper().isEmpty(); }
}

/**
 * An example implementation of Collection to be used as a base class.
 * Note that methods that the subclass possibly refines must be invoked
 * via getThis.
 */
class Queue<Item> extends Composable<Collection<Item>> implements Collection<Item> {
    private LinkedList<Item> items = new LinkedList<Item>();
    public void insert(Item item) { items.addLast(item); }
    public Item remove() { return items.removeFirst(); }
    public void clear() {
        while (! getThis().isEmpty()) {
            getThis().remove();
        }
    }
    public boolean isEmpty() { return items.isEmpty(); }
}

/**
 * An example implementation of Collection to be used as an
 * abstract subclass (mixin) for adding the SizeOf capability
 * to an arbitrary Collection implementation.  Note that methods
 * from the superclass in the conceptual sense must be invoked
 * via getSuper.
 */
class SizeOf<Item> extends CollectionMixin<Item> {
    private int count;
    public SizeOf(Collection<Item> zuper) { super(zuper); }
    public void insert(Item item) {
        super.insert(item);
        count ++;
    }
    public Item remove() {
        Item result = super.remove();
        if (result != null) {
            count --;
        }
        return result;
    }
    public int size() { return count; }
}

/**
 * An example implementation of Collection to be used as an
 * abstract subclass (mixin) for adding the Verbose capability
 * (reporting on every operation) to an arbitrary Collection
 * implementation.
 */
class Verbose<Item> extends CollectionMixin<Item> {
    public Verbose(Collection<Item> zuper) { super(zuper); }
    public void insert(Item item) {
        super.insert(item);
        System.out.println(this + ".insert(" + item + ")");
    }
    public Item remove() {
        Item result = super.remove();
        System.out.println(this + ".remove() -> " + result);
        return result;
    }
    public void clear() {
        super.clear();
        System.out.println(this + ".clear()");
    }
}

/**
 * Main class for testing this mixin framework.
 */
public class Mixin {
    public static void main(String[] args) {
        SizeOf<String> s = 
          new SizeOf<String>(
            new Verbose<String>(
              new Verbose<String>(
              	new Queue<String>())));
        s.insert("hello");
        s.insert("world");
        System.out.println(s.size()); // prints 2
        s.clear();
        System.out.println(s.size()); // prints 0
        // note that we cannot invoke reset on s
        // because SizeOf is not a subtype of Verbose
    }
}
