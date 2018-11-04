package org.phoenicis.configuration.localisation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.phoenicis.configuration.localisation.Localisation.tr;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(PowerMockRunner.class)

public class LocalisationTest {
    private static I18n mockI18n;

    @Before
    public void setUp() {
        mockI18n = mock(I18n.class);
        PowerMockito.doReturn("output").when(mockI18n).tr("Input");
        PowerMockito.mockStatic(I18nFactory.class);
        PowerMockito.when(I18nFactory.getI18n(any(), anyString())).thenReturn(mockI18n);
    }

    @PrepareForTest({ I18nFactory.class, I18n.class })
    @Test
    public synchronized void testTrWithAString() {
        assertEquals("output", tr("Input"));
    }

    @PrepareForTest({ I18nFactory.class, I18n.class })
    @Test
    public synchronized void testTrWithNull() {
        assertEquals(null, tr(null));
    }

    @PrepareForTest({ I18nFactory.class, I18n.class })
    @Test
    public synchronized void testSimpleTranslatableObject() {
        final SimpleTranslatableObject translatableObject = new SimpleTranslatableObject("input", "input");
        assertEquals("input", translatableObject.getItemNotToBeTranslated());
        assertEquals("input", translatableObject.getItemToBeTranslated());

        final SimpleTranslatableObject translatedObject = tr(translatableObject);
        assertEquals("input", translatedObject.getItemNotToBeTranslated());
        assertEquals("output", translatedObject.getItemToBeTranslated());
    }

    @PrepareForTest({ I18nFactory.class, I18n.class })
    @Test
    public synchronized void testSimpleTranslatableObjectWithBuilder() {
        final SimpleTranslatableObjectBuilder translatableObject = new SimpleTranslatableObjectBuilder.Builder()
                .withItemNotToBeTranslated("input").withItemToBeTranslated("input").build();
        assertEquals("input", translatableObject.getItemNotToBeTranslated());
        assertEquals("input", translatableObject.getItemToBeTranslated());

        final SimpleTranslatableObjectBuilder translatedObject = tr(translatableObject);
        assertEquals("input", translatedObject.getItemNotToBeTranslated());
        assertEquals("output", translatedObject.getItemToBeTranslated());
    }

    @PrepareForTest({ I18nFactory.class, I18n.class })
    @Test
    public synchronized void testTreeTranslatableObject() {
        final TreeTranslatableObject treeTranslatableObject = new TreeTranslatableObject(
                new SimpleTranslatableObject("input", "input"), new SimpleTranslatableObject("input", "input"));
        assertEquals("input", treeTranslatableObject.getItemNotToBeTranslated().getItemToBeTranslated());
        assertEquals("input", treeTranslatableObject.getItemNotToBeTranslated().getItemNotToBeTranslated());
        assertEquals("input", treeTranslatableObject.getItemToBeTranslated().getItemToBeTranslated());
        assertEquals("input", treeTranslatableObject.getItemToBeTranslated().getItemNotToBeTranslated());

        final TreeTranslatableObject translatedObject = tr(treeTranslatableObject);
        assertEquals("input", translatedObject.getItemNotToBeTranslated().getItemToBeTranslated());
        assertEquals("input", translatedObject.getItemNotToBeTranslated().getItemNotToBeTranslated());
        assertEquals("output", translatedObject.getItemToBeTranslated().getItemToBeTranslated());
        assertEquals("input", translatedObject.getItemToBeTranslated().getItemNotToBeTranslated());

    }

    @PrepareForTest({ I18nFactory.class, I18n.class })
    @Test
    public synchronized void testTreeTranslatableCollectionObject() {
        final CollectionsOfTranslatableObject collectionsOfTranslatableObject = new CollectionsOfTranslatableObject(
                Arrays.asList("input", "input"), Arrays.asList("input", "input"));

        final CollectionsOfTranslatableObject translatedObject = tr(collectionsOfTranslatableObject);
        assertEquals(Arrays.asList("input", "input"), translatedObject.getItemNotToBeTranslated());
        assertEquals(Arrays.asList("output", "output"), translatedObject.getItemToBeTranslated());
    }

}
