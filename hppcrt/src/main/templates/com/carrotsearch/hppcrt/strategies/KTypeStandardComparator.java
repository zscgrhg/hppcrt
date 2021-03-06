package com.carrotsearch.hppcrt.strategies;

import com.carrotsearch.hppcrt.*;

/*! #import("com/carrotsearch/hppcrt/Intrinsics.java") !*/
/**
 * Standard  {@link KTypeComparator} for <code>KType</code>s, providing the same behavior as either natural ordering
 * for primitives, or Comparable for objects.
 */
/*! ${TemplateOptions.generatedAnnotation} !*/
public final class KTypeStandardComparator<KType> implements KTypeComparator<KType>
{
    public KTypeStandardComparator() {
        // nothing
    }

    @Override
    public int compare(final KType e1, final KType e2) {

        return Intrinsics.<KType> compareUnchecked(e1, e2);
    }

    @Override
    public boolean equals(final Object o) {

        if (o instanceof KTypeStandardComparator<?>) {

            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {

        return KTypeStandardComparator.class.hashCode();
    }
}
