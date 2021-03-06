package com.carrotsearch.hppcrt.maps;

import com.carrotsearch.hppcrt.*;

/*! #import("com/carrotsearch/hppcrt/Intrinsics.java") !*/
/**
 * Tests for {@link KTypeVTypeHashMap}.
 */
/*! ${TemplateOptions.generatedAnnotation} !*/
public class KTypeVTypeHashMapTest<KType, VType> extends AbstractKTypeVTypeHashMapTest<KType, VType>
{
    @Override
    protected KTypeVTypeMap<KType, VType> createNewMapInstance(final int initialCapacity, final double loadFactor) {

        if (initialCapacity == 0 && loadFactor == HashContainers.DEFAULT_LOAD_FACTOR) {

            return new KTypeVTypeHashMap<KType, VType>();

        } else if (loadFactor == HashContainers.DEFAULT_LOAD_FACTOR) {

            return new KTypeVTypeHashMap<KType, VType>(initialCapacity);
        }

        //generic case
        return new KTypeVTypeHashMap<KType, VType>(initialCapacity, loadFactor);
    }

    @Override
    protected KType[] getKeys(final KTypeVTypeMap<KType, VType> testMap) {

        final KTypeVTypeHashMap<KType, VType> concreteClass = (KTypeVTypeHashMap<KType, VType>) (testMap);

        return Intrinsics.<KType[]> cast(concreteClass.keys);
    }

    @Override
    protected VType[] getValues(final KTypeVTypeMap<KType, VType> testMap) {
        final KTypeVTypeHashMap<KType, VType> concreteClass = (KTypeVTypeHashMap<KType, VType>) (testMap);

        return Intrinsics.<VType[]> cast(concreteClass.values);
    }

    @Override
    protected boolean isAllocatedDefaultKey(final KTypeVTypeMap<KType, VType> testMap) {
        final KTypeVTypeHashMap<KType, VType> concreteClass = (KTypeVTypeHashMap<KType, VType>) (testMap);

        return concreteClass.allocatedDefaultKey;

    }

    @Override
    protected VType getAllocatedDefaultKeyValue(final KTypeVTypeMap<KType, VType> testMap) {
        final KTypeVTypeHashMap<KType, VType> concreteClass = (KTypeVTypeHashMap<KType, VType>) (testMap);

        return concreteClass.allocatedDefaultKeyValue;
    }

    @Override
    protected KTypeVTypeMap<KType, VType> getClone(final KTypeVTypeMap<KType, VType> testMap) {
        final KTypeVTypeHashMap<KType, VType> concreteClass = (KTypeVTypeHashMap<KType, VType>) (testMap);

        return concreteClass.clone();
    }

    @Override
    protected KTypeVTypeMap<KType, VType> getFrom(final KTypeVTypeMap<KType, VType> testMap) {
        final KTypeVTypeHashMap<KType, VType> concreteClass = (KTypeVTypeHashMap<KType, VType>) (testMap);

        return KTypeVTypeHashMap.from(concreteClass);
    }

    @Override
    protected KTypeVTypeMap<KType, VType> getFromArrays(final KType[] keys, final VType[] values) {

        return KTypeVTypeHashMap.from(Intrinsics.<KType[]> cast(keys),
                Intrinsics.<VType[]> cast(values));
    }

    @Override
    protected KTypeVTypeMap<KType, VType> getCopyConstructor(final KTypeVTypeMap<KType, VType> testMap) {
        final KTypeVTypeHashMap<KType, VType> concreteClass = (KTypeVTypeHashMap<KType, VType>) (testMap);

        return new KTypeVTypeHashMap<KType, VType>(concreteClass);
    }

    @Override
    protected int getEntryPoolSize(final KTypeVTypeMap<KType, VType> testMap) {
        final KTypeVTypeHashMap<KType, VType> concreteClass = (KTypeVTypeHashMap<KType, VType>) (testMap);

        return concreteClass.entryIteratorPool.size();
    }

    @Override
    protected int getKeysPoolSize(final KTypeCollection<KType> keys) {

        final KTypeVTypeHashMap<KType, VType>.KeysCollection concreteClass = (KTypeVTypeHashMap<KType, VType>.KeysCollection) (keys);

        return concreteClass.keyIteratorPool.size();
    }

    @Override
    protected int getValuesPoolSize(final KTypeCollection<VType> values) {
        final KTypeVTypeHashMap<KType, VType>.ValuesCollection concreteClass = (KTypeVTypeHashMap<KType, VType>.ValuesCollection) (values);

        return concreteClass.valuesIteratorPool.size();
    }

    @Override
    protected int getEntryPoolCapacity(final KTypeVTypeMap<KType, VType> testMap) {
        final KTypeVTypeHashMap<KType, VType> concreteClass = (KTypeVTypeHashMap<KType, VType>) (testMap);

        return concreteClass.entryIteratorPool.capacity();
    }

    @Override
    protected int getKeysPoolCapacity(final KTypeCollection<KType> keys) {
        final KTypeVTypeHashMap<KType, VType>.KeysCollection concreteClass = (KTypeVTypeHashMap<KType, VType>.KeysCollection) (keys);

        return concreteClass.keyIteratorPool.capacity();
    }

    @Override
    protected int getValuesPoolCapacity(final KTypeCollection<VType> values) {
        final KTypeVTypeHashMap<KType, VType>.ValuesCollection concreteClass = (KTypeVTypeHashMap<KType, VType>.ValuesCollection) (values);

        return concreteClass.valuesIteratorPool.capacity();
    }

    //////////////////////////////////////
    /// Implementation-specific tests
    /////////////////////////////////////

    //none for now

}
