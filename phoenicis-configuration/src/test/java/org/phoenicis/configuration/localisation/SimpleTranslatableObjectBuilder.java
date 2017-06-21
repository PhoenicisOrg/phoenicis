package org.phoenicis.configuration.localisation;

@Translatable
public class SimpleTranslatableObjectBuilder {
    private final String itemToBeTranslated;
    private final String itemNotToBeTranslated;

    private SimpleTranslatableObjectBuilder(Builder builder) {
        this.itemNotToBeTranslated = builder.itemNotToBeTranslated;
        this.itemToBeTranslated = builder.itemToBeTranslated;
    }

    @Translate
    public String getItemToBeTranslated() {
        return itemToBeTranslated;
    }

    public String getItemNotToBeTranslated() {
        return itemNotToBeTranslated;
    }

    @TranslatableBuilder
    public static class Builder {
        private String itemToBeTranslated;
        private String itemNotToBeTranslated;

        public Builder withItemToBeTranslated(String itemToBeTranslated) {
            this.itemToBeTranslated = itemToBeTranslated;
            return this;
        }

        public Builder withItemNotToBeTranslated(String itemNotToBeTranslated) {
            this.itemNotToBeTranslated = itemNotToBeTranslated;
            return this;
        }

        public SimpleTranslatableObjectBuilder build() {
            return new SimpleTranslatableObjectBuilder(this);
        }
    }
}
