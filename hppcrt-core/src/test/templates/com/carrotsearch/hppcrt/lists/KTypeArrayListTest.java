package com.carrotsearch.hppcrt.lists;

import static com.carrotsearch.hppcrt.TestUtils.assertEquals2;
import static com.carrotsearch.hppcrt.TestUtils.assertListEquals;
import static com.carrotsearch.hppcrt.TestUtils.assertSortedListEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.carrotsearch.hppcrt.*;
import com.carrotsearch.hppcrt.lists.*;
import com.carrotsearch.hppcrt.TestUtils;
import com.carrotsearch.hppcrt.cursors.*;
import com.carrotsearch.hppcrt.mutables.*;
import com.carrotsearch.hppcrt.predicates.*;
import com.carrotsearch.hppcrt.procedures.*;
import com.carrotsearch.hppcrt.sets.*;
import com.carrotsearch.hppcrt.sorting.*;

// ${TemplateOptions.doNotGenerateKType("BOOLEAN")}
/**
 * Unit tests for {@link KTypeArrayList}.
 */
/*! ${TemplateOptions.generatedAnnotation} !*/
public class KTypeArrayListTest<KType> extends AbstractKTypeTest<KType>
{
    /**
     * Per-test fresh initialized instance.
     */
    public KTypeArrayList<KType> list;

    public volatile long guard;

    @BeforeClass
    public static void configure()
    {
        IteratorPool.configureInitialPoolSize(8);
    }

    /* */
    @Before
    public void initialize()
    {
        list = KTypeArrayList.newInstance();
    }

    @After
    public void checkConsistency()
    {
        if (list != null)
        {
            for (int i = list.elementsCount; i < list.buffer.length; i++) {
                /*! #if ($TemplateOptions.KTypeGeneric) !*/
                Assert.assertTrue(Intrinsics.<KType> defaultKTypeValue() == list.buffer[i]);
                /*! #end !*/
            }
        }
    }

    /* */
    @Test
    public void testInitiallyEmpty()
    {
        Assert.assertEquals(0, list.size());
    }

    /* */
    @Test
    public void testAdd()
    {
        list.add(key1, key2);
        TestUtils.assertListEquals(list.toArray(), 1, 2);
    }

    /* */
    @Test
    public void testAddTwoArgs()
    {
        list.add(key1, key2);
        list.add(key3, key4);
        TestUtils.assertListEquals(list.toArray(), 1, 2, 3, 4);
    }

    /* */
    @Test
    public void testAddArray()
    {
        list.add(asArray(0, 1, 2, 3), 1, 2);
        TestUtils.assertListEquals(list.toArray(), 1, 2);
    }

    /* */
    /*! #if ($TemplateOptions.KTypeGeneric) !*/
    @SuppressWarnings("unchecked")
    /*! #end !*/
    @Test
    public void testAddVarArg()
    {
        list.add(asArray(0, 1, 2, 3));
        list.add(key4, key5, key6, key7);
        TestUtils.assertListEquals(list.toArray(), 0, 1, 2, 3, 4, 5, 6, 7);
    }

    /* */
    @Test
    public void testAddAll()
    {
        final KTypeArrayList<KType> list2 = KTypeArrayList.newInstance();
        list2.add(asArray(0, 1, 2));

        list.addAll(list2);
        list.addAll(list2);

        TestUtils.assertListEquals(list.toArray(), 0, 1, 2, 0, 1, 2);
    }

    /*! #if ($TemplateOptions.KTypeGeneric) !*/
    @Test
    public void testAddAll_subclass()
    {
        class A {
        }

        class B extends A {
        }

        final KTypeArrayList<B> list2 = new KTypeArrayList<B>();
        list2.add(new B());

        final KTypeArrayList<A> list3 = new KTypeArrayList<A>();
        list3.add(new B());
        list3.add(new A());
        list3.addAll(list2);
        Assert.assertEquals(3, list3.size());
    }

    /*! #end !*/

    /* */
    @Test
    public void testInsert()
    {
        list.insert(0, k1);
        list.insert(0, k2);
        list.insert(2, k3);
        list.insert(1, k4);

        TestUtils.assertListEquals(list.toArray(), 2, 4, 1, 3);
    }

    /* */
    @Test
    public void testSet()
    {
        list.add(asArray(0, 1, 2));

        TestUtils.assertEquals2(0, list.set(0, k3));
        TestUtils.assertEquals2(1, list.set(1, k4));
        TestUtils.assertEquals2(2, list.set(2, k5));

        TestUtils.assertListEquals(list.toArray(), 3, 4, 5);
    }

    /* */
    @Test
    public void testRemove()
    {
        list.add(asArray(0, 1, 2, 3, 4, 5, 6, 7, 8));

        Assert.assertEquals(0, castType(list.remove(0)));
        Assert.assertEquals(3, castType(list.remove(2)));
        Assert.assertEquals(2, castType(list.remove(1)));
        Assert.assertEquals(6, castType(list.remove(3)));

        TestUtils.assertListEquals(list.toArray(), 1, 4, 5, 7, 8);
    }

    /* */
    @Test
    public void testRemoveRange()
    {
        list.add(asArray(0, 1, 2, 3, 4));

        list.removeRange(0, 2);
        TestUtils.assertListEquals(list.toArray(), 2, 3, 4);

        list.removeRange(2, 3);
        TestUtils.assertListEquals(list.toArray(), 2, 3);

        list.removeRange(1, 1);
        TestUtils.assertListEquals(list.toArray(), 2, 3);

        list.removeRange(0, 1);
        TestUtils.assertListEquals(list.toArray(), 3);
    }

    /* */
    @Test
    public void testRemoveFirstLast()
    {
        list.add(asArray(0, 1, 2, 1, 0));

        Assert.assertEquals(-1, list.removeFirstOccurrence(k5));
        Assert.assertEquals(-1, list.removeLastOccurrence(k5));
        TestUtils.assertListEquals(list.toArray(), 0, 1, 2, 1, 0);

        Assert.assertEquals(1, list.removeFirstOccurrence(k1));
        TestUtils.assertListEquals(list.toArray(), 0, 2, 1, 0);
        Assert.assertEquals(3, list.removeLastOccurrence(k0));
        TestUtils.assertListEquals(list.toArray(), 0, 2, 1);
        Assert.assertEquals(0, list.removeLastOccurrence(k0));
        TestUtils.assertListEquals(list.toArray(), 2, 1);
        Assert.assertEquals(-1, list.removeLastOccurrence(k0));

        /*! #if ($TemplateOptions.KTypeGeneric) !*/
        list.clear();
        list.add(newArray(k0, null, k2, null, k0));
        Assert.assertEquals(1, list.removeFirstOccurrence(null));
        Assert.assertEquals(2, list.removeLastOccurrence(null));
        TestUtils.assertListEquals(list.toArray(), 0, 2, 0);
        /*! #end !*/
    }

    /* */
    @Test
    public void testRemoveAll()
    {
        list.add(asArray(0, 1, 0, 1, 0));

        Assert.assertEquals(0, list.removeAllOccurrences(k2));
        Assert.assertEquals(3, list.removeAllOccurrences(k0));
        TestUtils.assertListEquals(list.toArray(), 1, 1);

        Assert.assertEquals(2, list.removeAllOccurrences(k1));
        Assert.assertTrue(list.isEmpty());

        /*! #if ($TemplateOptions.KTypeGeneric) !*/
        list.clear();
        list.add(newArray(k0, null, k2, null, k0));
        Assert.assertEquals(2, list.removeAllOccurrences(null));
        Assert.assertEquals(0, list.removeAllOccurrences(null));
        TestUtils.assertListEquals(list.toArray(), 0, 2, 0);
        /*! #end !*/
    }

    /* */
    @Test
    public void testRemoveAllFromLookupContainer()
    {
        list.add(asArray(0, 1, 2, 1, 0));

        final KTypeOpenHashSet<KType> list2 = KTypeOpenHashSet.newInstance();
        list2.add(asArray(0, 2));

        Assert.assertEquals(3, list.removeAll(list2));
        Assert.assertEquals(0, list.removeAll(list2));

        TestUtils.assertListEquals(list.toArray(), 1, 1);
    }

    /* */
    @Test
    public void testRemoveAllWithPredicate()
    {
        list.add(newArray(k0, k1, k2, k1, k4));

        Assert.assertEquals(3, list.removeAll(new KTypePredicate<KType>()
                {
            @Override
            public boolean apply(final KType v)
            {
                return v == key1 || v == key2;
            };
                }));

        TestUtils.assertListEquals(list.toArray(), 0, 4);
    }

    /* */
    @Test
    public void testRetainAllWithPredicate()
    {
        list.add(newArray(k0, k1, k2, k1, k0));

        Assert.assertEquals(2, list.retainAll(new KTypePredicate<KType>()
                {
            @Override
            public boolean apply(final KType v)
            {
                return v == key1 || v == key2;
            };
                }));

        TestUtils.assertListEquals(list.toArray(), 1, 2, 1);
    }

    /* */
    @Test
    public void testRemoveAllWithPredicateInterrupted()
    {
        list.add(newArray(k0, k1, k2, k1, k4));

        final RuntimeException t = new RuntimeException();
        try
        {
            //the assert below should never be triggered because of the exception
            //so give it an invalid value in case the thing terminates  = initial size
            Assert.assertEquals(5, list.removeAll(new KTypePredicate<KType>()
                    {
                @Override
                public boolean apply(final KType v)
                {
                    if (v == key2)
                        throw t;
                    return v == key1;
                };
                    }));
            Assert.fail();
        }
        catch (final RuntimeException e)
        {
            // Make sure it's really our exception...
            if (e != t)
                throw e;
        }

        // And check if the list is in consistent state.
        TestUtils.assertListEquals(list.toArray(), 0, key2, key1, 4);
        Assert.assertEquals(4, list.size());
    }

    /* */
    @Test
    public void testIndexOf()
    {
        list.add(asArray(0, 1, 2, 1, 0));

        /*! #if ($TemplateOptions.KTypeGeneric) !*/
        list.add((KType) null);
        Assert.assertEquals(5, list.indexOf(null));
        /*! #end !*/

        Assert.assertEquals(0, list.indexOf(k0));
        Assert.assertEquals(-1, list.indexOf(k3));
        Assert.assertEquals(2, list.indexOf(k2));
    }

    /* */
    @Test
    public void testLastIndexOf()
    {
        list.add(asArray(0, 1, 2, 1, 0));

        /*! #if ($TemplateOptions.KTypeGeneric) !*/
        list.add((KType) null);
        Assert.assertEquals(5, list.lastIndexOf(null));
        /*! #end !*/

        TestUtils.assertEquals2(4, list.lastIndexOf(k0));
        TestUtils.assertEquals2(-1, list.lastIndexOf(k3));
        TestUtils.assertEquals2(2, list.lastIndexOf(k2));
    }

    /* */
    @Test
    public void testEnsureCapacity()
    {
        list.ensureCapacity(100);
        Assert.assertTrue(list.buffer.length >= 100);

        list.ensureCapacity(1000);
        list.ensureCapacity(1000);
        Assert.assertTrue(list.buffer.length >= 1000);
    }

    @Test
    public void testResizeAndCleanBuffer()
    {
        list.ensureCapacity(20);
        Arrays.fill(list.buffer, k1);

        list.resize(10);
        Assert.assertEquals(10, list.size());

        for (int i = 0; i < list.size(); i++) {

            TestUtils.assertEquals2(Intrinsics.<KType> defaultKTypeValue(), list.get(i));

        }

        Arrays.fill(list.buffer, Intrinsics.<KType> defaultKTypeValue());

        for (int i = 5; i < list.size(); i++)
            list.set(i, k1);

        list.resize(5);
        Assert.assertEquals(5, list.size());

        for (int i = list.size(); i < list.buffer.length; i++) {
            //only objects get cleared for GC sake.
            /*! #if ($TemplateOptions.KTypeGeneric) !*/
            TestUtils.assertEquals2(Intrinsics.<KType> defaultKTypeValue(), list.buffer[i]);
            /*! #end !*/
        }

    }

    /* */
    @Test
    public void testTrimToSize()
    {
        list.add(asArray(1, 2));
        list.trimToSize();
        Assert.assertEquals(2, list.buffer.length);
    }

    /* */
    @Test
    public void testRelease()
    {
        list.add(asArray(1, 2));
        list.release();
        Assert.assertEquals(0, list.size());
        list.add(asArray(1, 2));
        Assert.assertEquals(2, list.size());
    }

    /* */
    @Test
    public void testGrowth()
    {
        final int maxGrowth = 10;
        final int count = 500;

        list = new KTypeArrayList<KType>(0,
                new BoundedProportionalArraySizingStrategy(5, maxGrowth, 2));

        for (int i = 0; i < count; i++)
            list.add(cast(i));

        Assert.assertEquals(count, list.size());

        for (int i = 0; i < count; i++)
            TestUtils.assertEquals2(cast(i), list.get(i));

        Assert.assertTrue("Buffer size: 510 <= " + list.buffer.length,
                list.buffer.length <= count + maxGrowth);
    }

    /* */
    @Test
    public void testIterable()
    {
        list.add(asArray(0, 1, 2, 3));
        int count = 0;
        for (final KTypeCursor<KType> cursor : list)
        {
            count++;
            TestUtils.assertEquals2(list.get(cursor.index), cursor.value);
            TestUtils.assertEquals2(list.buffer[cursor.index], cursor.value);
        }
        Assert.assertEquals(count, list.size());

        count = 0;
        list.resize(0);
        for (@SuppressWarnings("unused")
        final KTypeCursor<KType> cursor : list)
        {
            count++;
        }
        Assert.assertEquals(0, count);
    }

    /* */
    @Test
    public void testIterator()
    {
        list.add(asArray(0, 1, 2, 3));
        final Iterator<KTypeCursor<KType>> iterator = list.iterator();
        int count = 0;
        while (iterator.hasNext())
        {
            iterator.hasNext();
            iterator.hasNext();
            iterator.hasNext();
            iterator.next();
            count++;
        }
        Assert.assertEquals(count, list.size());

        list.resize(0);
        Assert.assertFalse(list.iterator().hasNext());
    }

    /* */
    @Test
    public void testForEachWithProcedure()
    {
        list.add(asArray(1, 2, 3));
        final IntHolder holder = new IntHolder();
        list.forEach(new KTypeProcedure<KType>() {
            int index = 0;

            @Override
            public void apply(final KType v)
            {
                TestUtils.assertEquals2(v, list.get(index++));
                holder.value = index;
            }
        });
        Assert.assertEquals(holder.value, list.size());
    }

    /* */
    @Test
    public void testForEachReturnValueFromAnonymousClass()
    {
        list.add(asArray(1, 2, 3));
        final int result = list.forEach(new KTypeProcedure<KType>() {
            int index = 0;

            @Override
            public void apply(final KType v)
            {
                TestUtils.assertEquals2(v, list.get(index++));
            }
        }).index;
        Assert.assertEquals(result, list.size());
    }

    /* */
    @Test
    public void testClear()
    {
        list.add(asArray(1, 2, 3));
        list.clear();
        checkConsistency();
    }

    /* */
    /*! #if ($TemplateOptions.KTypeGeneric) !*/
    @SuppressWarnings("unchecked")
    /*! #end !*/
    @Test
    public void testFrom()
    {
        final KTypeArrayList<KType> variable = KTypeArrayList.from(k1, k2, k3);
        Assert.assertEquals(3, variable.size());
        TestUtils.assertListEquals(variable.toArray(), 1, 2, 3);
    }

    /* */
    /*! #if ($TemplateOptions.KTypeGeneric) !*/
    @SuppressWarnings("unchecked")
    /*! #end !*/
    @Test
    public void testHashCodeEquals()
    {
        final ObjectArrayList<Integer> l0 = ObjectArrayList.from();
        Assert.assertEquals(1, l0.hashCode());
        Assert.assertEquals(l0, ObjectArrayList.from());

        final KTypeArrayList<KType> l1 = KTypeArrayList.from(k1, k2, k3);
        final KTypeArrayList<KType> l2 = KTypeArrayList.from(k1, k2);
        l2.add(k3);

        Assert.assertEquals(l1.hashCode(), l2.hashCode());
        Assert.assertEquals(l1, l2);
    }

    /*! #if ($TemplateOptions.KTypeGeneric) !*/
    @Test
    @SuppressWarnings("unchecked")
    public void testHashCodeWithNulls()
    {
        final KTypeArrayList<KType> l1 = KTypeArrayList.from(k1, null, k3);
        final KTypeArrayList<KType> l2 = KTypeArrayList.from(k1, null, k3);

        Assert.assertEquals(l1.hashCode(), l2.hashCode());
        Assert.assertEquals(l1, l2);
    }

    /*! #end !*/

    /*! #if ($TemplateOptions.KTypeGeneric) !*/
    @Test
    public void testToArrayWithClass()
    {
        final KTypeArrayList<Integer> l1 = KTypeArrayList.from(1, 2, 3);
        final Integer[] result = l1.toArray(Integer.class);
        Assert.assertArrayEquals(new Integer[] { 1, 2, 3 }, result); // dummy
    }

    /*! #end !*/

    /*! #if ($TemplateOptions.KTypeGeneric) !*/
    @SuppressWarnings("unchecked")
    @Test
    public void testToArray()
    {
        final KTypeArrayList<KType> l1 = KTypeArrayList.from(k1, k2, k3);
        final Object[] result = l1.toArray();
        Assert.assertArrayEquals(new Object[] { k1, k2, k3 }, result);
    }

    /*! #end !*/

    /* */
    /*! #if ($TemplateOptions.KTypeGeneric) !*/
    @SuppressWarnings("unchecked")
    /*! #end !*/
    @Test
    public void testClone()
    {
        list.add(k1, k2, k3);

        final KTypeArrayList<KType> cloned = list.clone();
        cloned.removeAllOccurrences(key1);

        TestUtils.assertSortedListEquals(list.toArray(), key1, key2, key3);
        TestUtils.assertSortedListEquals(cloned.toArray(), key2, key3);
    }

    /* */
    /*! #if ($TemplateOptions.KTypeGeneric) !*/
    @SuppressWarnings("unchecked")
    /*! #end !*/
    @Test
    public void testToString()
    {
        Assert.assertEquals("["
                + key1 + ", "
                + key2 + ", "
                + key3 + "]", KTypeArrayList.from(k1, k2, k3).toString());
    }

    @Test
    public void testPooledIteratorForEach()
    {
        // Unbroken for-each loop
        final int TEST_SIZE = 10000;
        final long TEST_ROUNDS = 100;

        final KTypeArrayList<KType> testContainer = createArrayWithOrderedData(TEST_SIZE);

        final long checksum = testContainer.forEach(new KTypeProcedure<KType>() {

            long count;

            @Override
            public void apply(final KType value)
            {
                count += castType(value);
            }
        }).count;

        long testValue = 0;
        final long initialPoolSize = testContainer.valueIteratorPool.size();

        for (int round = 0; round < TEST_ROUNDS; round++)
        {
            //for-each in test :
            testValue = 0;
            for (final KTypeCursor<KType> cursor : testContainer)
            {
                //we consume 1 iterator for this loop
                Assert.assertEquals(initialPoolSize - 1, testContainer.valueIteratorPool.size());

                testValue += castType(cursor.value);
            }

            //check checksum the iteration
            Assert.assertEquals(checksum, testValue);

            //iterator is returned to its pool
            Assert.assertEquals(initialPoolSize, testContainer.valueIteratorPool.size());
        } //end for rounds
    }

    @Test
    public void testPooledIteratorBrokenForEach()
    {
        // for-each loop interrupted

        final int TEST_SIZE = 5000;
        final long TEST_ROUNDS = 100;

        final KTypeArrayList<KType> testContainer = createArrayWithOrderedData(TEST_SIZE);

        int count = 0;
        for (int round = 0; round < TEST_ROUNDS; round++)
        {
            //for-each in test :
            final long initialPoolSize = testContainer.valueIteratorPool.size();

            count = 0;
            for (final KTypeCursor<KType> cursor : testContainer)
            {
                guard += castType(cursor.value);
                //we consume 1 iterator for this loop, but reallocs can happen,
                //so we can only say its != initialPoolSize
                Assert.assertTrue(initialPoolSize != testContainer.valueIteratorPool.size());

                //brutally interrupt in the middle
                if (count > TEST_SIZE / 2)
                {
                    break;
                }

                count++;
            } //end for-each

            //iterator is NOT returned to its pool, due to the break.
            //reallocation could happen, so that the only testable thing
            //is that the size is != full pool
            Assert.assertTrue(initialPoolSize != testContainer.valueIteratorPool.size());
        } //end for rounds

        //Due to policy of the Iterator pool, the intended pool never get bigger that some limit
        //despite the Iterator leak.
        Assert.assertTrue(testContainer.valueIteratorPool.capacity() < IteratorPool.getMaxPoolSize() + 1);
    }

    @Test
    public void testPooledIteratorFullIteratorLoop()
    {
        // for-each loop interrupted

        final int TEST_SIZE = 5000;
        final long TEST_ROUNDS = 100;

        final KTypeArrayList<KType> testContainer = createArrayWithOrderedData(TEST_SIZE);

        final long checksum = testContainer.forEach(new KTypeProcedure<KType>() {

            long count;

            @Override
            public void apply(final KType value)
            {
                count += castType(value);
            }
        }).count;

        long testValue = 0;
        final int startingPoolSize = testContainer.valueIteratorPool.size();

        for (int round = 0; round < TEST_ROUNDS; round++)
        {
            //Classical iterator loop, with manually allocated Iterator
            final int initialPoolSize = testContainer.valueIteratorPool.size();

            final KTypeArrayList<KType>.ValueIterator loopIterator = testContainer.iterator();

            Assert.assertEquals(initialPoolSize - 1, testContainer.valueIteratorPool.size());

            testValue = 0;
            while (loopIterator.hasNext())
            {
                testValue += castType(loopIterator.next().value);
            } //end IteratorLoop

            //iterator is returned automatically to its pool, by normal iteration termination
            Assert.assertEquals(initialPoolSize, testContainer.valueIteratorPool.size());

            //checksum
            Assert.assertEquals(checksum, testValue);
        } //end for rounds

        // pool initial size is untouched anyway
        Assert.assertEquals(startingPoolSize, testContainer.valueIteratorPool.size());
    }

    @Test
    public void testPooledIteratorBrokenIteratorLoop()
    {
        // for-each loop interrupted

        final int TEST_SIZE = 5000;
        final long TEST_ROUNDS = 100;

        final KTypeArrayList<KType> testContainer = createArrayWithOrderedData(TEST_SIZE);
        final int startingPoolSize = testContainer.valueIteratorPool.size();

        int count = 0;
        for (int round = 0; round < TEST_ROUNDS; round++)
        {
            //Classical iterator loop, with manually allocated Iterator
            final long initialPoolSize = testContainer.valueIteratorPool.size();

            final KTypeArrayList<KType>.ValueIterator loopIterator = testContainer.iterator();

            Assert.assertEquals(initialPoolSize - 1, testContainer.valueIteratorPool.size());

            count = 0;
            while (loopIterator.hasNext())
            {
                guard += castType(loopIterator.next().value);

                //brutally interrupt in the middle
                if (count > TEST_SIZE / 2)
                {
                    break;
                }
                count++;
            } //end IteratorLoop

            //iterator is NOT returned to its pool, due to the break.
            Assert.assertEquals(initialPoolSize - 1, testContainer.valueIteratorPool.size());

            //manual return to the pool
            loopIterator.release();

            //now the pool is restored
            Assert.assertEquals(initialPoolSize, testContainer.valueIteratorPool.size());

        } //end for rounds

        // pool initial size is untouched anyway
        Assert.assertEquals(startingPoolSize, testContainer.valueIteratorPool.size());
    }

    @Test
    public void testPooledIteratorExceptionIteratorLoop()
    {
        final int TEST_SIZE = 5000;
        final long TEST_ROUNDS = 100;

        final KTypeArrayList<KType> testContainer = createArrayWithOrderedData(TEST_SIZE);

        final long checksum = testContainer.forEach(new KTypeProcedure<KType>() {

            long count;

            @Override
            public void apply(final KType value)
            {
                count += castType(value);
            }
        }).count;

        final int startingPoolSize = testContainer.valueIteratorPool.size();

        int count = 0;
        KTypeArrayList<KType>.ValueIterator loopIterator = null;

        for (int round = 0; round < TEST_ROUNDS; round++)
        {
            try
            {
                loopIterator = testContainer.iterator();
                Assert.assertEquals(startingPoolSize - 1, testContainer.valueIteratorPool.size());

                guard = 0;
                count = 0;
                while (loopIterator.hasNext())
                {
                    guard += castType(loopIterator.next().value);

                    //brutally interrupt in the middle some of the loops, but not all
                    if (round > TEST_ROUNDS / 2 && count > TEST_SIZE / 2)
                    {
                        throw new Exception("Oups some problem in the loop occured");
                    }
                    count++;
                } //end while

                //iterator is returned to its pool in case of normal loop termination
                Assert.assertEquals(startingPoolSize, testContainer.valueIteratorPool.size());
                Assert.assertEquals(checksum, guard);

            }
            catch (final Exception e)
            {
                //iterator is NOT returned to its pool because of the exception
                Assert.assertEquals(startingPoolSize - 1, testContainer.valueIteratorPool.size());

                //manual return to the pool then
                loopIterator.release();

                //now the pool is restored
                Assert.assertEquals(startingPoolSize, testContainer.valueIteratorPool.size());
            }
        } //end for rounds

        // pool initial size is untouched anyway
        Assert.assertEquals(startingPoolSize, testContainer.valueIteratorPool.size());
    }

    @Test
    public void testPooledIteratorExceptionSafe()
    {
        final int TEST_SIZE = 104171;
        final long TEST_ROUNDS = 15;

        final KTypeArrayList<KType> testContainer = createArrayWithOrderedData(TEST_SIZE);

        final long checksum = testContainer.forEach(new KTypeProcedure<KType>() {

            long count;

            @Override
            public void apply(final KType value)
            {
                count += castType(value);
            }
        }).count;

        final int initialPoolSize = testContainer.valueIteratorPool.size();

        //start with a non full pool, remove 3 elements
        final AbstractIterator<KTypeCursor<KType>> loopIteratorFake = testContainer.iterator();
        final AbstractIterator<KTypeCursor<KType>> loopIteratorFake2 = testContainer.iterator();
        final AbstractIterator<KTypeCursor<KType>> loopIteratorFake3 = testContainer.iterator();

        final int startingTestPoolSize = testContainer.valueIteratorPool.size();

        Assert.assertEquals(initialPoolSize - 3, startingTestPoolSize);

        int count = 0;
        KTypeArrayList<KType>.ValueIterator loopIterator = null;

        for (int round = 0; round < TEST_ROUNDS; round++)
        {
            try
            {
                loopIterator = testContainer.iterator();
                Assert.assertEquals(startingTestPoolSize - 1, testContainer.valueIteratorPool.size());

                guard = 0;
                count = 0;

                while (loopIterator.hasNext())
                {
                    guard += castType(loopIterator.next().value);

                    //brutally interrupt in the middle some of the loops, but not all
                    if (round > TEST_ROUNDS / 2 && count > TEST_SIZE / 2)
                    {
                        throw new Exception("Oups some problem in the loop occured");
                    }
                    count++;
                } //end while

                //iterator is returned to its pool in case of normal loop termination
                Assert.assertEquals(startingTestPoolSize, testContainer.valueIteratorPool.size());
                Assert.assertEquals(checksum, guard);

                //still, try to return it ....
                loopIterator.release();

                //nothing has changed
                Assert.assertEquals(startingTestPoolSize, testContainer.valueIteratorPool.size());
            }
            catch (final Exception e)
            {
                //iterator is NOT returned to its pool because of the exception
                Assert.assertEquals(startingTestPoolSize - 1, testContainer.valueIteratorPool.size());

                //manual return to the pool then
                loopIterator.release();

                //now the pool is restored
                Assert.assertEquals(startingTestPoolSize, testContainer.valueIteratorPool.size());

                //continue to try to release...
                loopIterator.release();

                //nothing has changed
                Assert.assertEquals(startingTestPoolSize, testContainer.valueIteratorPool.size());
            }
        } //end for rounds

        // pool initial size is untouched anyway
        Assert.assertEquals(startingTestPoolSize, testContainer.valueIteratorPool.size());

        //finally return the fake ones, several times
        loopIteratorFake.release();
        loopIteratorFake.release();
        loopIteratorFake.release();
        loopIteratorFake2.release();
        loopIteratorFake2.release();
        loopIteratorFake2.release();
        loopIteratorFake3.release();
        loopIteratorFake3.release();

        Assert.assertEquals(initialPoolSize, testContainer.valueIteratorPool.size());
    }

    @Test
    public void testSort()
    {
        //natural ordering comparator
        final KTypeComparator<KType> comp = new KTypeComparator<KType>() {

            @Override
            public int compare(final KType e1, final KType e2)
            {
                int res = 0;

                if (castType(e1) < castType(e2))
                {
                    res = -1;
                }
                else if (castType(e1) > castType(e2))
                {
                    res = 1;
                }

                return res;
            }
        };

        final int TEST_SIZE = (int) 1e6;
        //A) Sort an array of random values of primitive types

        /*! #if ($TemplateOptions.KTypePrimitive)
        //A-1) full sort
        KTypeArrayList<KType> primitiveList = createArrayWithRandomData(TEST_SIZE, 1515411541215L);
        primitiveList.sort();
        assertOrder(primitiveList, 0, primitiveList.size());
        //A-2) Partial sort
        primitiveList = createArrayWithRandomData(TEST_SIZE, 87454541215L);
        primitiveList.sort(12150,789444);
        assertOrder(primitiveList, 12150, 789444);
        #end !*/

        //B) Sort with Comparator
        //B-1) Full sort
        KTypeArrayList<KType> comparatorList = createArrayWithRandomData(TEST_SIZE, 4871164545215L);
        comparatorList.sort(comp);
        assertOrder(comparatorList, 0, comparatorList.size());
        //B-2) Partial sort
        comparatorList = createArrayWithRandomData(TEST_SIZE, 877521454L);
        comparatorList.sort(98748, 999548, comp);
        assertOrder(comparatorList, 98748, 999548);
    }

    @Test
    public void testPreallocatedSize()
    {
        final Random randomVK = new Random(315451248541L);
        //Test that the container do not resize if less that the initial size

        final int NB_TEST_RUNS = 50;

        for (int run = 0; run < NB_TEST_RUNS; run++)
        {
            //1) Choose a random number of elements
            /*! #if ($TemplateOptions.isKType("GENERIC", "INT", "LONG", "FLOAT", "DOUBLE")) !*/
            final int PREALLOCATED_SIZE = randomVK.nextInt(100000);
            /*!
            #elseif ($TemplateOptions.isKType("SHORT", "CHAR"))
             int PREALLOCATED_SIZE = randomVK.nextInt(100000);
            #else
              int PREALLOCATED_SIZE = randomVK.nextInt(100000);
            #end !*/

            //2) Preallocate to PREALLOCATED_SIZE :
            final KTypeArrayList<KType> newList = KTypeArrayList.newInstanceWithCapacity(PREALLOCATED_SIZE);

            //3) Add PREALLOCATED_SIZE different values. At the end, size() must be == PREALLOCATED_SIZE,
            //and internal buffer/allocated must not have changed of size
            final int contructorBufferSize = newList.buffer.length;

            for (int i = 0; i < PREALLOCATED_SIZE; i++)
            {
                newList.add(cast(randomVK.nextInt()));

                //internal size has not changed.
                Assert.assertEquals(contructorBufferSize, newList.buffer.length);
            }

            Assert.assertEquals(PREALLOCATED_SIZE, newList.size());
        } //end for test runs
    }

    /**
     * Test natural ordering between [startIndex; endIndex[
     * @param expected
     * @param actual
     * @param length
     */
    private void assertOrder(final KTypeArrayList<KType> order, final int startIndex, final int endIndex)
    {
        for (int i = startIndex + 1; i < endIndex; i++)
        {
            if (castType(order.get(i - 1)) > castType(order.get(i)))
            {
                Assert.assertTrue(String.format("Not ordered: (previous, next) = (%d, %d) at index %d",
                        castType(order.get(i - 1)), castType(order.get(i)), i), false);
            }
        }
    }

    private KTypeArrayList<KType> createArrayWithOrderedData(final int size)
    {
        final KTypeArrayList<KType> newArray = KTypeArrayList.newInstanceWithCapacity(KTypeArrayList.DEFAULT_CAPACITY);

        for (int i = 0; i < size; i++)
        {
            newArray.add(cast(i));
        }

        return newArray;
    }

    private KTypeArrayList<KType> createArrayWithRandomData(final int size, final long randomSeed)
    {
        final Random prng = new Random(randomSeed);

        final KTypeArrayList<KType> newArray = KTypeArrayList.newInstanceWithCapacity(KTypeArrayList.DEFAULT_CAPACITY);

        for (int i = 0; i < size; i++)
        {

            newArray.add(cast(prng.nextInt()));
        }

        return newArray;
    }

}