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

package org.phoenicis.tools.system;

import org.phoenicis.configuration.security.Safe;
import org.phoenicis.tools.system.GraphicsProperties;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.vulkan.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.glfw.GLFWVulkan.*;

// Code from LWJGL tutorial
@Safe
public class GraphicsPropertiesFetcher {
    private long window = NULL;

    /**
     * Create an invisible glfx window and context, from which info will be retrieved
     */
    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

        this.window = glfwCreateWindow(300, 300, "Test Window", NULL, NULL);
        if (this.window == NULL)
            throw new RuntimeException("Failed to create the GLFW window for testing graphic card capabilities");
    }

    /**
     * Destroy glfw window and context
     */
    private void terminate() {
        glfwFreeCallbacks(this.window);
        glfwDestroyWindow(this.window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
        this.window = NULL;
    }

    /**
     * Fetch graphics card vendor and OpenGL version
     */
    private void fetchVendorRendererOpenGLVersion(GraphicsProperties graphicsProperties) {
        glfwMakeContextCurrent(this.window);

        GL.createCapabilities();

        graphicsProperties.vendor = glGetString(GL_VENDOR);
        graphicsProperties.renderer = glGetString(GL_RENDERER);
        graphicsProperties.openGLVersion = glGetString(GL_VERSION);
        graphicsProperties.openGLVersion = graphicsProperties.openGLVersion.substring(0,
                graphicsProperties.openGLVersion.indexOf(' '));
    }

    /**
     * Fetch Vulkan version
     */
    private void fetchVulkanVersion(GraphicsProperties graphicsProperties) {
        if (!glfwVulkanSupported()) {
            return;
        }

        int version = VK.getInstanceVersionSupported();

        graphicsProperties.vulkanVersion = String.valueOf(version >> 22) + "." +
                String.valueOf((version >> 12) & 0x3ff) + "." +
                String.valueOf(version & 0xfff);
    }

    /**
     * Fetch the current graphics properties (vendor, OpenGL version, Vulkan version) of the system
     * 
     * @return The current graphics properties inside a GraphicsProperties object
     */
    public GraphicsProperties getProperties() {
        GraphicsProperties graphicsProperties = new GraphicsProperties();

        init();

        fetchVendorRendererOpenGLVersion(graphicsProperties);
        fetchVulkanVersion(graphicsProperties);

        terminate();

        return graphicsProperties;
    }
}
