package util;

import java.util.HashSet;

/**
 * Created by extradikke on 6-11-14.
 */
public class SynchornizedHashSet<T> extends HashSet<T> {

    HashSet<T> hashSet;

    public SynchornizedHashSet() {
        hashSet = new HashSet<>();
    }



    public synchronized boolean removeSync(T obj) {
        return hashSet.remove(obj);

    }


    public synchronized boolean addSync(T object){
        return hashSet.add(object);
    }


}
