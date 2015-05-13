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

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import com.playonlinux.common.dtos.AvailableCategoriesDTO;
import com.playonlinux.common.dtos.CategoryDTO;
import com.playonlinux.common.dtos.ScriptDTO;


import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;


public class RemoteScriptsTest {

    private static String mockServerURL;
    private static ClientAndServer mockServer;
    private static int MOCKSERVER_PORT = 3343;
    private RemoteScripts remoteScripts;
    private MockObserver observer;

    @BeforeClass
    public static void setUp() {
        mockServer = new ClientAndServer(MOCKSERVER_PORT);
        mockServerURL = "http://localhost:"+MOCKSERVER_PORT+"/categories";
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
                        .withBody("{\"categories\": [\n" +
                                "    {\n" +
                                "        \"scripts\": [\n" +
                                "            {\n" +
                                "                \"description\": \"\",\n" +
                                "                \"id\": 373,\n" +
                                "                \"scriptInformations\": {\n" +
                                "                    \"compatiblesOperatingSystems\" : [\"LINUX\", \"MACOSX\"],\n" +
                                "                    \"testingOperatingSystems\" : [],\n" +
                                "                    \"free\": true,\n" +
                                "                    \"hasIcon\": true,\n" +
                                "                    \"requiresNoCD\": false\n" +
                                "                },\n" +
                                "                \"name\": \"7-Zip\"\n" +
                                "            },\n" +
                                "            {\n" +
                                "                \"description\": \"Description FluidMark\",\n" +
                                "                \"id\": 830,\n" +
                                "                \"infos\": {\n" +
                                "                    \"compatiblesOperatingSystems\" : [\"LINUX\"],\n" +
                                "                    \"testingOperatingSystems\" : [\"LINUX\"],\n" +
                                "                    \"free\": true,\n" +
                                "                    \"icon\": true,\n" +
                                "                    \"requires_nocd\": false\n" +
                                "                },\n" +
                                "                \"name\": \"FluidMark 1.3.1\"\n" +
                                "            }\n" +
                                "        ],\n" +
                                "        \"id\": 2,\n" +
                                "        \"name\": \"Accessories\",\n" +
                                "        \"type\": \"INSTALLERS\"\n" +
                                "    }\n" +
                                "]}\n")
        );


        remoteScripts = new RemoteScripts(mockServerURL);
        observer = new MockObserver();
        remoteScripts.addObserver(observer);
    }

    @Test
    public void testScriptFetcher_MockWebServer_CategoryDTOIsPopulated() {
        remoteScripts.fetchCategories();

        assertEquals("Accessories", observer.getDTO().getCategories().get(0).getName());
        assertEquals(2, observer.getDTO().getCategories().get(0).getId());
        assertEquals(CategoryDTO.CategoryType.INSTALLERS, observer.getDTO().getCategories().get(0).getType());
    }

    @Test
    public void testScriptFetcher_MockWebServer_ScriptDTOIsPopulated() {
        remoteScripts.fetchCategories();

        ArrayList<ScriptDTO> scripts = observer.getDTO().getCategories().get(0).getScripts();

        assertEquals("", scripts.get(0).getDescription());
        assertEquals(373, scripts.get(0).getId());
        assertEquals("7-Zip", scripts.get(0).getName());

        assertEquals("Description FluidMark", scripts.get(1).getDescription());
        assertEquals(830, scripts.get(1).getId());
        assertEquals("FluidMark 1.3.1", scripts.get(1).getName());
    }

    private class MockObserver implements Observer {
        public AvailableCategoriesDTO categoryDto;

        @Override
        public void update(Observable o, Object arg) {
            this.categoryDto = (AvailableCategoriesDTO) arg;
        }

        public AvailableCategoriesDTO getDTO() {
            return categoryDto;
        }
    }
}