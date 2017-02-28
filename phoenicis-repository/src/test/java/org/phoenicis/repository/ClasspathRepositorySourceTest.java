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

package org.phoenicis.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.phoenicis.repository.dto.CategoryDTO;
import org.junit.Test;
import org.phoenicis.repository.dto.RepositoryDTO;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import static org.junit.Assert.assertEquals;


public class ClasspathRepositorySourceTest {
    private final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
    private final RepositorySource repositorySource = new ClasspathRepositorySource("/org/phoenicis/repository/repositoryTest", resourceResolver, new ObjectMapper());

    @Test
    public void fetchInstallableApplications_numberOfCategories() {
        assertEquals(2, repositorySource.fetchRepositories().get(0).getCategories().size());
    }

    @Test
    public void fetchInstallableApplications_categoriesNames() {
        assertEquals("Development", repositorySource.fetchRepositories().get(0).getCategories().get(0).getName());
        assertEquals("Graphics", repositorySource.fetchRepositories().get(0).getCategories().get(1).getName());
    }

    @Test
    public void fetchInstallableApplications_Graphics() {
        assertEquals(1, repositorySource.fetchRepositories().get(0).getCategories().get(1).getApplications().size());
    }

    @Test
    public void fetchInstallableApplications_Graphics_Photofiltre() {
        assertEquals("Photofiltre", repositorySource.fetchRepositories().get(0).getCategories().get(1).getApplications().get(0).getName());
    }

    @Test
    public void fetchInstallableApplications_Graphics_Photofiltre_scripts() {
        assertEquals(1, repositorySource.fetchRepositories().get(0).getCategories().get(1).getApplications().get(0).getScripts().size());
    }

    @Test
    public void fetchInstallableApplications_Graphics_Photofiltre_scriptName() {
        assertEquals("Online", repositorySource.fetchRepositories().get(0).getCategories().get(1).getApplications().get(0).getScripts().get(0).getScriptName());
    }

    @Test
    public void fetchInstallableApplications_Graphics_Photofiltre_scriptContent() {
        assertEquals("include([\"Functions\", \"Functions\",  \"QuickScript\", \"OnlineInstallerScript\"]);\n" +
                "\n" +
                "new OnlineInstallerScript()\n" +
                "    .name(\"Photofiltre\")\n" +
                "    .editor(\"Antonio Da Cruz\")\n" +
                "    .applicationHomepage(\"http://photofiltre.free.fr\")\n" +
                "    .author(\"Quentin PÂRIS\")\n" +
                "    .url(\"http://photofiltre.free.fr/utils/pf-setup-fr-652.exe\")\n" +
                "    .checksum(\"dc965875d698cd3f528423846f837d0dcf39616d\")\n" +
                "    .category(\"Graphics\")\n" +
                "    .executable(\"PhotoFiltre.exe\")\n" +
                "    .go();", repositorySource.fetchRepositories().get(0).getCategories().get(1).getApplications().get(0).getScripts().get(0).getScript().trim());
    }

    @Test
    public void fetchInstallableApplications_Graphics_Photofiltre_miniatures() {
        assertEquals(1, repositorySource.fetchRepositories().get(0).getCategories().get(1).getApplications().get(0).getMiniatures().size());
    }

    @Test
    public void fetchInstallableApplications_categoriesTypes() {
        assertEquals(RepositoryDTO.RepositoryType.APPLICATIONS, repositorySource.fetchRepositories().get(0).getType());
    }

}