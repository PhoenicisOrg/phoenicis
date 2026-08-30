package org.phoenicis.javafx.components.application.skin;

import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertTrue;

public class ApplicationInformationPanelSkinSourceTest {
    @Test
    public void testUsesPlayOnLinuxWineBinariesUrl() throws IOException {
        final Path sourcePath = Path.of(
                "src/main/java/org/phoenicis/javafx/components/application/skin/ApplicationInformationPanelSkin.java");
        final String content = Files.readString(sourcePath, StandardCharsets.UTF_8);

        assertTrue(content.contains("http://www.playonlinux.com/wine/binaries/"));
    }

    @Test
    public void testHandlesNullScripts() throws IOException {
        final Path sourcePath = Path.of(
                "src/main/java/org/phoenicis/javafx/components/application/skin/ApplicationInformationPanelSkin.java");
        final String content = Files.readString(sourcePath, StandardCharsets.UTF_8);

        assertTrue(content.contains("if (source == null)"));
    }
}
