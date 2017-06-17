package org.phoenicis.configuration.localisation;

@Translatable
public class TreeTranslatableObject {
    private final SimpleTranslatableObject itemToBeTranslated;
    private final SimpleTranslatableObject itemNotToBeTranslated;

    @TranslatableCreator
    public TreeTranslatableObject(@ParameterName("itemToBeTranslated") SimpleTranslatableObject itemToBeTranslated,
                                  @ParameterName("itemNotToBeTranslated") SimpleTranslatableObject itemNotToBeTranslated) {
        this.itemToBeTranslated = itemToBeTranslated;
        this.itemNotToBeTranslated = itemNotToBeTranslated;
    }

    @Translate
    public SimpleTranslatableObject getItemToBeTranslated() {
        return itemToBeTranslated;
    }

    public SimpleTranslatableObject getItemNotToBeTranslated() {
        return itemNotToBeTranslated;
    }
}
