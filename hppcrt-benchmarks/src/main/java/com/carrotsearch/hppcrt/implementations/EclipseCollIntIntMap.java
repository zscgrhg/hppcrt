package com.carrotsearch.hppcrt.implementations;


import java.util.Arrays;
import java.util.Random;

import org.eclipse.collections.impl.map.mutable.primitive.IntIntHashMap;

import com.carrotsearch.hppcrt.Util;
import com.carrotsearch.hppcrt.XorShift128P;

public class EclipseCollIntIntMap extends MapImplementation<IntIntHashMap>
{
    private int[] insertKeys;
    private int[] containsKeys;
    private int[] removedKeys;
    private int[] insertValues;

    public EclipseCollIntIntMap(final int size, final float loadFactor)
    {
        //load factor is fixed to 0.5 !
        super(new IntIntHashMap(size));
    }

    /**
     * Setup
     */
    @Override
    public void setup(final int[] keysToInsert, final MapImplementation.HASH_QUALITY hashQ, final int[] keysForContainsQuery, final int[] keysForRemovalQuery) {

        final Random prng = new XorShift128P(0x122335577L);

        //make a full copy
        this.insertKeys = Arrays.copyOf(keysToInsert, keysToInsert.length);
        this.containsKeys = Arrays.copyOf(keysForContainsQuery, keysForContainsQuery.length);
        this.removedKeys = Arrays.copyOf(keysForRemovalQuery, keysForRemovalQuery.length);

        this.insertValues = new int[keysToInsert.length];

        for (int i = 0; i < this.insertValues.length; i++) {

            this.insertValues[i] = prng.nextInt();
        }
    }

    @Override
    public void clear() {
        this.instance.clear();
    }

    @Override
    public int size() {

        return this.instance.size();
    }

    @Override
    public int benchPutAll() {

        final IntIntHashMap instance = this.instance;

        final int[] values = this.insertValues;

        final int count = 0;

        final int[] keys = this.insertKeys;

        for (int i = 0; i < keys.length; i++) {

            //those ones do not return the previous value....
            instance.put(keys[i], values[i]);
        }

        return instance.size();
    }

    @Override
    public int benchContainKeys()
    {
        final IntIntHashMap instance = this.instance;

        int count = 0;

        final int[] keys = this.containsKeys;

        for (int i = 0; i < keys.length; i++) {

            count += instance.containsKey(keys[i]) ? 1 : 0;
        }

        return count;
    }

    @Override
    public int benchRemoveKeys() {

        final IntIntHashMap instance = this.instance;

        final int count = 0;

        final int[] keys = this.removedKeys;

        for (int i = 0; i < keys.length; i++) {

            //those ones do not return the previous value....
            instance.remove(keys[i]);
        }

        return instance.size();
    }

    @Override
    public void setCopyOfInstance(final MapImplementation<?> toCloneFrom) {

        //no clone, but a copy constructor
        this.instance = new IntIntHashMap(((IntIntHashMap) toCloneFrom.instance));
    }

    @Override
    public void reshuffleInsertedKeys(final Random rand) {
        Util.shuffle(this.insertKeys, rand);

    }

    @Override
    public void reshuffleInsertedValues(final Random rand) {
        Util.shuffle(this.insertValues, rand);

    }
}