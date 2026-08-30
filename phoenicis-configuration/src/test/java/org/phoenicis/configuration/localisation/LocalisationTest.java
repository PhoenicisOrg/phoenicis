package org.phoenicis.configuration.localisation;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class LocalisationTest {
    @Test
    public void trWithNullReturnsNull() {
        assertNull(Localisation.tr((String) null));
        assertNull(Localisation.tr((Object) null));
    }

    @Test
    public void isTranslatableDetectsAnnotatedTypes() {
        assertTrue(Localisation.isTranslatable(SimpleTranslatableObject.class));
        assertTrue(Localisation.isTranslatable(SimpleTranslatableObjectBuilder.class));
        assertFalse(Localisation.isTranslatable(String.class));
    }

    @Test
    public void trPreservesCollectionStructure() {
        List<String> list = Arrays.asList("a", "b");
        Set<String> set = new HashSet<>(Arrays.asList("a", "b"));

        List<String> translatedList = Localisation.tr(list);
        Set<String> translatedSet = Localisation.tr(set);

        assertEquals(2, translatedList.size());
        assertEquals(2, translatedSet.size());
        assertTrue(translatedSet.contains("a"));
        assertTrue(translatedSet.contains("b"));
    }
}
