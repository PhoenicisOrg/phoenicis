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

package com.playonlinux.wine.registry;

import java.util.ArrayList;
import java.util.List;

public class RegistryKey extends AbstractRegistryNode {
    List<AbstractRegistryNode> children;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("+ " + this.name + "\n");
        for(AbstractRegistryNode child: children) {
            for(String line: child.toString().split("\n")) {
                stringBuilder.append("| " +  line + "\n");
            }
        }
        return stringBuilder.toString();
    }

    RegistryKey(String name) {
        super(name);
        children = new ArrayList<>();
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

}
