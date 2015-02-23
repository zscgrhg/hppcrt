package com.carrotsearch.hppcrt.implementations;

import java.util.Random;

import com.carrotsearch.hppcrt.Util;
import com.carrotsearch.hppcrt.XorShiftRandom;
import com.carrotsearch.hppcrt.maps.ObjectIntOpenIdentityHashMap;

public class HppcIdentityIntMap extends MapImplementation<ObjectIntOpenIdentityHashMap<MapImplementation.ComparableInt>>
{
    private ComparableInt[] insertKeys;
    private ComparableInt[] containsKeys;
    private ComparableInt[] removedKeys;
    private int[] insertValues;

    protected HppcIdentityIntMap(final int size, final float loadFactor)
    {
        super(new ObjectIntOpenIdentityHashMap<ComparableInt>(size, loadFactor));
    }

    /**
     * Setup
     */
    @Override
    public void setup(final int[] keysToInsert, final MapImplementation.HASH_QUALITY hashQ, final int[] keysForContainsQuery, final int[] keysForRemovalQuery) {

        final Random prng = new XorShiftRandom(0x122335577L);

        this.insertKeys = new ComparableInt[keysToInsert.length];

        this.containsKeys = new ComparableInt[keysForContainsQuery.length];
        this.removedKeys = new ComparableInt[keysForRemovalQuery.length];

        this.insertValues = new int[keysToInsert.length];

        //Auto box into Integers, they must have the same length anyway.
        for (int ii = 0; ii < keysToInsert.length; ii++) {

            this.insertKeys[ii] = new ComparableInt(keysToInsert[ii], hashQ);

            this.insertValues[ii] = prng.nextInt();
        }

        //Auto box into Integers, they must have the same length anyway.
        for (int ii = 0; ii < keysForContainsQuery.length; ii++) {

            this.containsKeys[ii] = new ComparableInt(keysForContainsQuery[ii], hashQ);
        }

        //Auto box into Integers, they must have the same length anyway.
        for (int ii = 0; ii < keysForRemovalQuery.length; ii++) {

            this.removedKeys[ii] = new ComparableInt(keysForRemovalQuery[ii], hashQ);
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

        final ObjectIntOpenIdentityHashMap<ComparableInt> instance = this.instance;
        final int[] values = this.insertValues;

        int count = 0;

        final ComparableInt[] keys = this.insertKeys;

        for (int i = 0; i < keys.length; i++) {

            count += instance.put(keys[i], values[i]);
        }

        return count;
    }

    @Override
    public int benchContainKeys()
    {
        final ObjectIntOpenIdentityHashMap<ComparableInt> instance = this.instance;

        int count = 0;

        final ComparableInt[] keys = this.containsKeys;

        for (int i = 0; i < keys.length; i++) {

            count += instance.containsKey(keys[i]) ? 1 : 0;
        }

        return count;
    }

    @Override
    public int benchRemoveKeys() {

        final ObjectIntOpenIdentityHashMap<ComparableInt> instance = this.instance;

        int count = 0;

        final ComparableInt[] keys = this.removedKeys;

        for (int i = 0; i < keys.length; i++) {

            count += instance.remove(keys[i]);
        }

        return count;
    }
}