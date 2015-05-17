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

package com.playonlinux.services;

import com.playonlinux.app.PlayOnLinuxContext;
import com.playonlinux.common.dtos.*;
import com.playonlinux.domain.PlayOnLinuxError;
import com.playonlinux.injection.Scan;
import com.playonlinux.injection.Inject;
import com.playonlinux.ui.api.RemoteAvailableInstallers;
import com.playonlinux.webservice.RemoteWebService;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Scan
public class RemoteAvailableInstallersPlayOnLinuxImplementation extends Observable
        implements RemoteAvailableInstallers, Observer {
    @Inject
    private static PlayOnLinuxContext playOnLinuxContext;

    @Inject
    private static PlayOnLinuxBackgroundServicesManager playOnLinuxBackgroundServicesManager;

    private ArrayList<CategoryDTO> categoriesDTO;
    private int numberOfCategories;
    private DownloadEnvelopeDTO<AvailableCategoriesDTO> downloadEnvelopeDto;

    RemoteAvailableInstallersPlayOnLinuxImplementation() throws MalformedURLException {
        URL webserviceUrl = playOnLinuxContext.makeWebserviceUrl();
        RemoteWebService remoteWebService = new RemoteWebService(webserviceUrl);
        remoteWebService.addObserver(this);
        playOnLinuxBackgroundServicesManager.register(remoteWebService);
    }

    @Override
    public Iterator<CategoryDTO> iterator() {
        return new ArrayList(categoriesDTO).iterator();
    }

    @Override
    public void addObserver(Observer o) {
        super.addObserver(o);
    }

    @Override
    public void update(Observable o, Object arg) {
        downloadEnvelopeDto = (DownloadEnvelopeDTO<AvailableCategoriesDTO>) arg;

        try {
            if(downloadEnvelopeDto.getEnvelopeContent() != null) {
                ArrayList<CategoryDTO> availableCategories = new ArrayList<>(
                        downloadEnvelopeDto.getEnvelopeContent().getCategories()
                );

                numberOfCategories = availableCategories.size();
                categoriesDTO = availableCategories;
            }
        } finally {
            this.setChanged();
            this.notifyObservers();
        }

    }

    @Override
    public int getNumberOfCategories() {
        return numberOfCategories;
    }

    @Override
    public boolean isUpdating() {
        return downloadEnvelopeDto.getDownloadState().getState() == DownloadStateDTO.State.DOWNLOADING;
    }

    @Override
    public boolean hasFailed() {
        return downloadEnvelopeDto.getDownloadState().getState() == DownloadStateDTO.State.FAILED;
    }

    @Override
    public Iterable<ScriptDTO> getAllScripts() {
        return getAllScripts(null);
    }

    @Override
    public Iterable<ScriptDTO> getAllScripts(String filterText) {
        ArrayList<ScriptDTO> scripts = new ArrayList<>();
        for(CategoryDTO categoryDTO: new ArrayList<>(categoriesDTO)) {
            for(ScriptDTO scriptDTO: new ArrayList<>(categoryDTO.getScripts())) {
                if(filterText == null || scriptDTO.getName().contains(filterText)) {
                    scripts.add(scriptDTO);
                }
            }
        }

        Collections.sort(scripts, new ScriptDTO.alphabeticalOrderComparator());

        return () -> scripts.iterator();
    }

    @Override
    public Iterable<ScriptDTO> getAllScriptsInCategory(String categoryName) throws PlayOnLinuxError {
        for(CategoryDTO categoryDTO: categoriesDTO) {
            if(categoryName.equals(categoryDTO.getName())) {
                return getAllScriptsInCategory(categoryDTO);
            }
        }
        throw new PlayOnLinuxError(String.format("The category %s does not exist!", categoryName));
    }

    @Override
    public ScriptDTO getScriptByName(String scriptName) throws PlayOnLinuxError {
        for(ScriptDTO scriptDTO: this.getAllScripts()) {
            if(scriptName.equals(scriptDTO.getName())) {
                return scriptDTO;
            }
        }

        throw new PlayOnLinuxError(String.format("The script %s does not exist!", scriptName));
    }

    private Iterable<ScriptDTO> getAllScriptsInCategory(CategoryDTO categoryDTO) {
        ArrayList<ScriptDTO> scripts = new ArrayList<>();
        for(ScriptDTO scriptDTO: new ArrayList<>(categoryDTO.getScripts())) {
            scripts.add(scriptDTO);
        }

        Collections.sort(scripts, new ScriptDTO.alphabeticalOrderComparator());

        return () -> scripts.iterator();
    }
}
