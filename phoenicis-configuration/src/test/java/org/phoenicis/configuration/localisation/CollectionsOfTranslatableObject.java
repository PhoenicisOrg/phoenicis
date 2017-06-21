package org.phoenicis.configuration.localisation;

import java.util.List;

@Translatable
public class CollectionsOfTranslatableObject {
    private final List<String> itemToBeTranslated;
    private final List<String> itemNotToBeTranslated;

    @TranslatableCreator
    public CollectionsOfTranslatableObject(@ParameterName("itemToBeTranslated") List<String> itemToBeTranslated,
            @ParameterName("itemNotToBeTranslated") List<String> itemNotToBeTranslated) {
        this.itemToBeTranslated = itemToBeTranslated;
        this.itemNotToBeTranslated = itemNotToBeTranslated;
    }

    @Translate
    public List<String> getItemToBeTranslated() {
        return itemToBeTranslated;
    }

    public List<String> getItemNotToBeTranslated() {
        return itemNotToBeTranslated;
    }
}
