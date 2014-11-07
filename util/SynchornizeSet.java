package util;

import java.util.HashSet;

/**
 * Created by extradikke on 6-11-14.
 */
public class SynchornizeSet<T> extends HashSet<T> {

    HashSet<T> hashSet;

    public SynchornizeSet() {
        hashSet = new HashSet<>();
    }



    public synchronized boolean removeSync(T obj) {
        return hashSet.remove(obj);

    }


    public synchronized boolean addSync(T object){
        return hashSet.add(object);
    }


}
