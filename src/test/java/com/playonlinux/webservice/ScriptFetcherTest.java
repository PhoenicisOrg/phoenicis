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
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import com.playonlinux.webservice.dto.AvailableCategories;
import com.playonlinux.webservice.dto.Category;
import com.playonlinux.webservice.dto.Script;


import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;


public class ScriptFetcherTest {

    private static String mockServerURL;
    private static ClientAndServer mockServer;
    private static int MOCKSERVER_PORT = 3343;

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
    }

    @Test
    public void testScriptFetcher_MockWebServer_CategoryDTOIsPopulated() {
        ScriptFetcher scriptFetcher = new ScriptFetcher(mockServerURL);
        AvailableCategories categories = scriptFetcher.fetchCategories();

        assertEquals("Accessories", categories.getCategories().get(0).getName());
        assertEquals(2, categories.getCategories().get(0).getId());
        assertEquals(Category.CategoryType.INSTALLERS, categories.getCategories().get(0).getType());
    }

    @Test
    public void testScriptFetcher_MockWebServer_ScriptDTOIsPopulated() {
        ScriptFetcher scriptFetcher = new ScriptFetcher(mockServerURL);
        AvailableCategories categories = scriptFetcher.fetchCategories();

        ArrayList<Script> scripts = categories.getCategories().get(0).getScripts();

        assertEquals("", scripts.get(0).getDescription());
        assertEquals(373, scripts.get(0).getId());
        assertEquals("7-Zip", scripts.get(0).getName());

        assertEquals("Description FluidMark", scripts.get(1).getDescription());
        assertEquals(830, scripts.get(1).getId());
        assertEquals("FluidMark 1.3.1", scripts.get(1).getName());
    }

}