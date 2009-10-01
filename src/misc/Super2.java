package misc;

class Super2 { }

class MyList {
  public void insert(Object x) { }
  public boolean contains(Object x) { return false; }
  // ...
}

class MySet extends MyList {
  @Override
  public void insert(Object x) {
    if (! this.contains(x)) {
      super.insert(x);
    }
  }
}

class MyHashSet extends MySet {
  // ...
}
