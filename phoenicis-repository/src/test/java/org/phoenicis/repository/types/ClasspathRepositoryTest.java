/*
 * Copyright (C) 2015-2017 PÂRIS Quentin
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.phoenicis.repository.types;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.phoenicis.repository.dto.CategoryDTO;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import static org.junit.Assert.assertEquals;

public class ClasspathRepositoryTest {
    private final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
    private final Repository repository = new ClasspathRepository("/org/phoenicis/repository/repositoryTest",
            resourceResolver, new ObjectMapper());

    @Test
    public void fetchInstallableApplicationsNumberOfCategories() {
        assertEquals(2, repository.fetchInstallableApplications().getTypes().get(0).getCategories().size());
    }

    @Test
    public void fetchInstallableApplicationsCategoriesNames() {
        assertEquals("Development",
                repository.fetchInstallableApplications().getTypes().get(0).getCategories().get(0).getName());
        assertEquals("Graphics",
                repository.fetchInstallableApplications().getTypes().get(0).getCategories().get(1).getName());
    }

    @Test
    public void fetchInstallableApplicationsGraphics() {
        assertEquals(1, repository.fetchInstallableApplications().getTypes().get(0).getCategories().get(1)
                .getApplications().size());
    }

    @Test
    public void fetchInstallableApplicationsGraphicsPhotofiltre() {
        assertEquals("Photofiltre",
                repository.fetchInstallableApplications().getTypes().get(0).getCategories().get(1).getApplications()
                        .get(0).getName());
    }

    @Test
    public void fetchInstallableApplicationsGraphicsPhotofiltreScripts() {
        assertEquals(1, repository.fetchInstallableApplications().getTypes().get(0).getCategories().get(1)
                .getApplications().get(0)
                .getScripts().size());
    }

    @Test
    public void fetchInstallableApplicationsGraphicsPhotofiltreScriptName() {
        assertEquals("Online", repository.fetchInstallableApplications().getTypes().get(0).getCategories().get(1)
                .getApplications().get(0)
                .getScripts().get(0).getScriptName());
    }

    @Test
    public void fetchInstallableApplicationsGraphicsPhotofiltreScriptContent() {
        assertEquals("include([\"Utils\", \"Functions\", \"QuickScript\", \"OnlineInstallerScript\"]);\n" + "\n"
                + "new OnlineInstallerScript()\n" + "    .name(\"Photofiltre\")\n"
                + "    .editor(\"Antonio Da Cruz\")\n" + "    .applicationHomepage(\"http://photofiltre.free.fr\")\n"
                + "    .author(\"Quentin PÂRIS\")\n"
                + "    .url(\"http://photofiltre.free.fr/utils/pf-setup-fr-652.exe\")\n"
                + "    .checksum(\"dc965875d698cd3f528423846f837d0dcf39616d\")\n" + "    .category(\"Graphics\")\n"
                + "    .executable(\"PhotoFiltre.exe\")\n" + "    .go();",
                repository.fetchInstallableApplications().getTypes().get(0).getCategories().get(1).getApplications()
                        .get(0).getScripts()
                        .get(0).getScript().trim());
    }

    @Test
    public void fetchInstallableApplicationsGraphicsPhotofiltreMiniatures() {
        assertEquals(1, repository.fetchInstallableApplications().getTypes().get(0).getCategories().get(1)
                .getApplications().get(0)
                .getMiniatures().size());
    }

    @Test
    public void fetchInstallableApplicationsCategoriesTypes() {
        assertEquals(CategoryDTO.CategoryType.INSTALLERS,
                repository.fetchInstallableApplications().getTypes().get(0).getCategories().get(0).getType());
        assertEquals(CategoryDTO.CategoryType.FUNCTIONS,
                repository.fetchInstallableApplications().getTypes().get(0).getCategories().get(1).getType());
    }

}