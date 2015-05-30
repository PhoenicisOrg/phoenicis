/*
 * Copyright (C) 2015 PÂRIS Quentin
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

package com.playonlinux.webservice;

import com.playonlinux.common.dto.ApplicationDTO;
import com.playonlinux.common.dto.DownloadEnvelopeDTO;
import com.playonlinux.common.dto.ScriptDTO;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import com.playonlinux.common.dto.CategoryDTO;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static org.junit.Assert.assertEquals;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;


public class InstallerSourceWebserviceImplementationTest {

    private static URL mockServerURL;
    private static ClientAndServer mockServer;
    private static int MOCKSERVER_PORT = 3343;
    private InstallerSourceWebserviceImplementation remoteAvailableInstallers;
    private MockObserver observer;

    @BeforeClass
    public static void setUp() throws MalformedURLException {
        mockServer = new ClientAndServer(MOCKSERVER_PORT);
        mockServerURL = new URL("http://localhost:"+MOCKSERVER_PORT+"/categories");
    }

    @Before
    public void prepareMockServer() {
        mockServer.when(
                request()
                        .withMethod("GET")
                        .withPath("/categories")
        ).respond(
                response()
                        .withStatusCode(200)
                        .withHeaders(
                                new Header("Content-Type", "application/json")
                        )
                        .withBody("[\n" +
                                "    {\n" +
                                "        \"applications\": [\n" +
                                "                {\n" +
                                "                    \"id\": 373,\n" +
                                "                    \"name\": \"7-Zip\",\n" +
                                "                    \"description\": \"\",\n" +
                                "                    \"iconUrl\": \"http:\\/\\/files.playonlinux.com\\/resources\\/icones_install\\/7-Zip\",\n" +
                                "                    \"miniaturesUrls\": [],\n" +
                                "                    \"scripts\": [\n" +
                                "                        {\n" +
                                "                            \"scriptName\": \"7-Zip\",\n" +
                                "                            \"compatiblesOperatingSystems\": [\n" +
                                "                                \"LINUX\",\n" +
                                "                                \"MACOSX\"\n" +
                                "                            ],\n" +
                                "                            \"testingOperatingSystems\": [],\n" +
                                "                            \"free\": true,\n" +
                                "                            \"requiresNoCD\": false\n" +
                                "                        }\n" +
                                "                    ]\n" +
                                "                },\n" +
                                "                {\n" +
                                "                    \"id\": 1265,\n" +
                                "                    \"name\": \"Amazon Kindle\",\n" +
                                "                    \"description\": \"Known problems:\\r<br \\/>- Displays a warning about a new version being available, you can disregard the warning.\\r<br \\/>\",\n" +
                                "                    \"iconUrl\": \"http:\\/\\/files.playonlinux.com\\/resources\\/icones_install\\/Amazon Kindle\",\n" +
                                "                    \"miniaturesUrls\": [],\n" +
                                "                    \"scripts\": [\n" +
                                "                        {\n" +
                                "                            \"scriptName\": \"Amazon Kindle\",\n" +
                                "                            \"compatiblesOperatingSystems\": [\n" +
                                "                                \"LINUX\",\n" +
                                "                                \"MACOSX\"\n" +
                                "                            ],\n" +
                                "                            \"testingOperatingSystems\": [],\n" +
                                "                            \"free\": true,\n" +
                                "                            \"requiresNoCD\": false\n" +
                                "                        }\n" +
                                "                    ]\n" +
                                "                }\n" +
                                "        ],\n" +
                                "        \"id\": 2,\n" +
                                "        \"name\": \"Accessories\",\n" +
                                "        \"type\": \"INSTALLERS\"\n" +
                                "    }\n" +
                                "]\n")
        );


        remoteAvailableInstallers = new InstallerSourceWebserviceImplementation(mockServerURL);
        observer = new MockObserver();
        remoteAvailableInstallers.addObserver(observer);
    }

    @Test
    public void testScriptFetcher_MockWebServer_CategoryDTOIsPopulated() {
        remoteAvailableInstallers.populate();

        assertEquals("Accessories", observer.getDTO().get(0).getName());
        assertEquals(2, observer.getDTO().get(0).getId());
        assertEquals(CategoryDTO.CategoryType.INSTALLERS, observer.getDTO().get(0).getType());
    }


    @Test
    public void testScriptFetcher_MockWebServer_ApplicationsDTOIsPopulated() {
        remoteAvailableInstallers.populate();

        List<ApplicationDTO> applications = observer.getDTO().get(0).getApplications();

        assertEquals("", applications.get(0).getDescription());
        assertEquals(373, applications.get(0).getId());
        assertEquals("http://url1", applications.get(0).getIconUrl());
        assertEquals(0, applications.get(0).getMiniaturesUrls().size());
        assertEquals("7-Zip", applications.get(0).getName());

        assertEquals("Description FluidMark", applications.get(1).getDescription());
        assertEquals(830, applications.get(1).getId());
        assertEquals("http://url2", applications.get(1).getIconUrl());
        assertEquals(2, applications.get(1).getMiniaturesUrls().size());
        assertEquals("http://Miniature1", applications.get(1).getMiniaturesUrls().get(0));
        assertEquals("http://Miniature2", applications.get(1).getMiniaturesUrls().get(1));
        assertEquals("FluidMark 1.3.1", applications.get(1).getName());
    }


    private class MockObserver implements Observer {
        public List<CategoryDTO> categoryDto;

        @Override
        public void update(Observable o, Object arg) {
            System.out.println(arg);
            this.categoryDto = (List<CategoryDTO>) ((DownloadEnvelopeDTO) arg).getEnvelopeContent();
        }

        public List<CategoryDTO> getDTO() {
            return categoryDto;
        }
    }

    @AfterClass
    public static void tearDown() {
        mockServer.stop();
    }
}