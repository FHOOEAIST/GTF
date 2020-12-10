/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.graph;

import org.testng.Assert;
import org.testng.annotations.Test;
import science.aist.gtf.graph.impl.MetaTagCollectionImpl;
import science.aist.gtf.graph.impl.MetaTagImpl;

import java.util.Optional;

/**
 * <p>Test class for {@link MetaTagCollectionImpl}</p>
 *
 * @author Andreas Pointner
 */
public class MetaTagCollectionTest {
    @Test
    public void testMetaTag1() {
        // given
        MetaTagCollection v = new MetaTagCollectionImpl();

        // when
        boolean res = v.addMetaTag(new MetaTagImpl<>("height", 12.0));

        // then
        Optional<MetaTag<Double>> tag = v.<Double>getMetaTags().stream().filter(p -> p.getKey().equals("height")).findFirst();
        Assert.assertTrue(res);
        Assert.assertTrue(tag.isPresent());
        Assert.assertNotNull(tag.get().getValue());
        Assert.assertEquals(tag.get().getValue(), 12.0, 0.001);
    }

    @Test
    public void testMetaTagRemove() {
        // given
        MetaTagCollection v = new MetaTagCollectionImpl();
        MetaTagImpl<Double> height = new MetaTagImpl<>("height", 12.0);
        v.addMetaTag(height);

        // when
        final boolean b = v.removeMetaTag(height);

        // then
        Assert.assertTrue(b);
    }

    @Test
    public void testMetaTag2() {
        // given
        MetaTagCollection v = new MetaTagCollectionImpl();
        v.addMetaTag(new MetaTagImpl<>("height", 12.0));

        // when
        double height = v.getMetaTagValue("height", Double.class);

        // then
        Assert.assertEquals(height, 12.0, 0.001);
    }

    @Test
    public void testMetaTag7() {
        // given
        MetaTagCollection v = new MetaTagCollectionImpl();
        v.addMetaTag(new MetaTagImpl<>("height", 12.0));

        // when
        double height = v.getMetaTagValue("height");

        // then
        Assert.assertEquals(height, 12.0, 0.001);
    }

    @Test
    public void testMetaTag3() {
        // given --> double
        MetaTagCollection v = new MetaTagCollectionImpl();
        v.addMetaTag(new MetaTagImpl<>("height", 12.0d));

        // when  --> double is assignable to number
        Number height = v.getMetaTagValue("height", Number.class);

        // then
        Assert.assertNotNull(height);
        Assert.assertEquals(height.doubleValue(), 12.0, 0.001);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testMetaTag4() {
        // given  --> double
        MetaTagCollection v = new MetaTagCollectionImpl();
        v.addMetaTag(new MetaTagImpl<>("height", 12.0d));

        // when --> double should not be assignable to int
        v.getMetaTagValue("height", Integer.class);

        // then --> exception expected!
        Assert.fail();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testMetaTag5() {
        // given  --> double
        MetaTagCollection v = new MetaTagCollectionImpl();
        v.addMetaTag(new MetaTagImpl<>("height", 12.0d));

        // when --> try to get a tag with non existing key.
        v.getMetaTagValue("width", Double.class);

        // then --> exception expected!
        Assert.fail();
    }

    @Test
    public void testMetaTag8() {
        // given --> double
        MetaTagCollection v = new MetaTagCollectionImpl();
        v.addMetaTag(new MetaTagImpl<>("height", 12.0d));

        // when  --> double is assignable to number
        Optional<Number> height = v.tryGetMetaTagValue("height", Number.class);

        // then
        Assert.assertNotNull(height);
        Assert.assertTrue(height.isPresent());
        Assert.assertEquals(height.get().doubleValue(), 12.0, 0.001);
    }

    @Test
    public void testMetaTag9() {
        // given  --> double
        MetaTagCollection v = new MetaTagCollectionImpl();
        v.addMetaTag(new MetaTagImpl<>("height", 12.0d));

        // when --> double should not be assignable to int
        Optional<Integer> height = v.tryGetMetaTagValue("height", Integer.class);

        // then
        Assert.assertNotNull(height);
        Assert.assertFalse(height.isPresent());
    }

    @Test
    public void testMetaTag10() {
        // given --> double
        MetaTagCollection v = new MetaTagCollectionImpl();
        v.addMetaTag(new MetaTagImpl<>("height", 12.0d));

        // when  --> double is assignable to number
        Optional<Number> height = v.tryGetMetaTagValue("height");

        // then
        Assert.assertNotNull(height);
        Assert.assertTrue(height.isPresent());
        Assert.assertEquals(height.get().doubleValue(), 12.0, 0.001);
    }

    @Test
    public void testMetaTag11() {
        // given  --> double
        MetaTagCollection v = new MetaTagCollectionImpl();
        v.addMetaTag(new MetaTagImpl<>("height", 12.0d));

        // when --> double should not be assignable to int
        Optional<Integer> height = v.tryGetMetaTagValue("width");

        // then
        Assert.assertNotNull(height);
        Assert.assertFalse(height.isPresent());
    }

    @Test(expectedExceptions = ClassCastException.class)
    public void testMetaTag12() {
        // given  --> double
        MetaTagCollection v = new MetaTagCollectionImpl();
        v.addMetaTag(new MetaTagImpl<>("height", 12.0d));

        // when --> double should not be assignable to int (but as it is a cast it is still working here)
        Optional<Integer> height = v.tryGetMetaTagValue("height");

        // then
        Assert.assertNotNull(height);
        Assert.assertTrue(height.isPresent());
        int h = height.get(); // this fails now class cast exception
        Assert.assertEquals(h, 12.0); // this code is never reached
    }

    @Test
    public void testRemoveMetaTag() {
        // given
        MetaTagCollection v = new MetaTagCollectionImpl();
        v.addMetaTag(new MetaTagImpl<>("height", 12.0d));
        v.addMetaTag(new MetaTagImpl<>("width", 12.0d));

        // when
        v.removeMetaTag("height");

        // then
        Assert.assertEquals(v.getMetaTags().size(), 1);
    }

    @Test
    public void testRemoveMetaTag2() {
        // given
        MetaTagCollection v = new MetaTagCollectionImpl();
        v.addMetaTag(new MetaTagImpl<>("height", 12.0d));
        v.addMetaTag(new MetaTagImpl<>("width", 12.0d));

        // when
        v.removeMetaTag("something");

        // then
        Assert.assertEquals(v.getMetaTags().size(), 2);
    }

    @Test
    public void testRemoveMetaTag3() {
        // given
        MetaTagCollection v = new MetaTagCollectionImpl();
        v.addMetaTag(new MetaTagImpl<>("height", 12.0d));
        v.addMetaTag(new MetaTagImpl<>("height", 12));
        v.addMetaTag(new MetaTagImpl<>("width", 12.0d));

        // when
        v.removeMetaTag("height", Integer.class);

        // then
        Assert.assertEquals(v.getMetaTags().size(), 2);
    }

    @Test
    public void testRemoveMetaTag4() {
        // given
        MetaTagCollection v = new MetaTagCollectionImpl();
        v.addMetaTag(new MetaTagImpl<>("height", 12.0d));
        v.addMetaTag(new MetaTagImpl<>("height", 12));
        v.addMetaTag(new MetaTagImpl<>("width", 12.0d));

        // when
        v.removeMetaTag("height", Double.class, Integer.class);

        // then
        Assert.assertEquals(v.getMetaTags().size(), 1);
    }
}
