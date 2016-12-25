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

package com.playonlinux.win32.registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegistryKey extends AbstractRegistryNode {
    private final List<AbstractRegistryNode> children;

    public RegistryKey(String name) {
        super(name);
        children = new ArrayList<>();
    }
    
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("+ ")
                .append(this.name).append("\n");
        for(AbstractRegistryNode child: children) {
            for(String line: child.toString().split("\n")) {
                stringBuilder.append("| ").append(line).append("\n");
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public String toRegString() {
        final List<String> registryPath = new ArrayList<>();
        for(AbstractRegistryNode registryNode = this; registryNode != null;
            registryNode = registryNode.parent) {
            registryPath.add(0, registryNode.getName());
        }

        return "\n"+"["+String.join("\\", registryPath)+"]";
    }

    public void addChild(AbstractRegistryNode newChildren) {
        children.add(newChildren);
        newChildren.setParent(this);
    }


    public List<AbstractRegistryNode> getChildren() {
        return children;
    }

    public AbstractRegistryNode getChild(int i) {
        return children.get(i);
    }

    public AbstractRegistryNode getChild(String childName) {
        for(AbstractRegistryNode child: this.children) {
            if(childName.equals(child.getName())) {
                return child;
            }
        }
        return null;
    }

    public AbstractRegistryNode getChild(List<String> childrenNames) {
        RegistryKey currentLevel = this;
        for(String child: childrenNames) {
            AbstractRegistryNode nextLevel = currentLevel.getChild(child);
            if(nextLevel instanceof RegistryKey) {
                currentLevel = (RegistryKey) nextLevel;
            } else {
                return nextLevel;
            }
        }
        return currentLevel;
    }

    public AbstractRegistryNode getChild(String... childrenNames) {
        return getChild(Arrays.asList(childrenNames));
    }

    public void addChildren(AbstractRegistryNode... nodes) {
        for(AbstractRegistryNode node: nodes) {
            addChild(node);
        }
    }
}
