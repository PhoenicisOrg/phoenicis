package org.phoenicis.configuration.localisation;

@Translatable
public class SimpleTranslatableObject {
    private final String itemToBeTranslated;
    private final String itemNotToBeTranslated;

    @TranslatableCreator
    public SimpleTranslatableObject(@ParameterName("itemToBeTranslated") String itemToBeTranslated,
            @ParameterName("itemNotToBeTranslated") String itemNotToBeTranslated) {
        this.itemToBeTranslated = itemToBeTranslated;
        this.itemNotToBeTranslated = itemNotToBeTranslated;
    }

    @Translate
    public String getItemToBeTranslated() {
        return itemToBeTranslated;
    }

    public String getItemNotToBeTranslated() {
        return itemNotToBeTranslated;
    }
}
