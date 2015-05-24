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
import com.playonlinux.utils.OperatingSystem;
import com.playonlinux.webservice.RemoteAvailableInstallers;
import org.apache.commons.lang.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Scan
public class RemoteAvailableInstallersPlayOnLinuxImplementation extends Observable
        implements com.playonlinux.ui.api.RemoteAvailableInstallers, Observer {
    @Inject
    private static PlayOnLinuxContext playOnLinuxContext;

    @Inject
    private static PlayOnLinuxBackgroundServicesManager playOnLinuxBackgroundServicesManager;

    private List<CategoryDTO> categoriesDTO;
    private int numberOfCategories;
    private DownloadEnvelopeDTO<AvailableCategoriesDTO> downloadEnvelopeDto;
    private RemoteAvailableInstallers remoteAvailableInstallers;
    private final URL webserviceUrl;
    private InstallerFilter filter = new InstallerFilter();

    RemoteAvailableInstallersPlayOnLinuxImplementation() throws MalformedURLException {
        webserviceUrl = playOnLinuxContext.makeWebserviceUrl();
        this.refresh();
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
                List<CategoryDTO> availableCategories = new ArrayList<>(
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
    public Iterable<ScriptDTO> getList() {
        List<ScriptDTO> scripts = new ArrayList<>();
        for(CategoryDTO categoryDTO: new ArrayList<>(categoriesDTO)) {
            for(ScriptDTO scriptDTO: new ArrayList<>(categoryDTO.getScripts())) {
                scripts.add(scriptDTO);
            }
        }

        Collections.sort(scripts, new ScriptDTO.AlphabeticalOrderComparator());
        return () -> scripts.iterator();
    }

    @Override
    public Iterable<ScriptDTO> getFilteredList() throws PlayOnLinuxError {
        Iterable<ScriptDTO> scriptsToFilter;
        if(filter.getCategory() == null){
            scriptsToFilter = getList();
        }else{
            scriptsToFilter = getAllScriptsInCategory(filter.getCategory());
        }

        List<ScriptDTO> scripts = new ArrayList<>();
        for(ScriptDTO script : scriptsToFilter){
            if(getFilter().apply(script)){
                scripts.add(script);
            }
        }
        return scripts;
    }


    @Override
    public InstallerFilter getFilter(){
        return this.filter;
    }


    @Override
    public ScriptDTO getByName(String scriptName) throws PlayOnLinuxError {
        for(ScriptDTO scriptDTO: this.getList()) {
            if(scriptName.equals(scriptDTO.getName())) {
                return scriptDTO;
            }
        }

        throw new PlayOnLinuxError(String.format("The script %s does not exist!", scriptName));
    }

    @Override
    public void refresh() {
        if(remoteAvailableInstallers != null) {
            remoteAvailableInstallers.deleteObserver(this);
            playOnLinuxBackgroundServicesManager.unregister(remoteAvailableInstallers);
        }
        remoteAvailableInstallers = new RemoteAvailableInstallers(webserviceUrl);
        remoteAvailableInstallers.addObserver(this);
        playOnLinuxBackgroundServicesManager.register(remoteAvailableInstallers);
    }

    private Iterable<ScriptDTO> getAllScriptsInCategory(String categoryName) throws PlayOnLinuxError {
        for(CategoryDTO categoryDTO: categoriesDTO) {
            if(categoryName.equals(categoryDTO.getName())) {
                return getAllScriptsInCategory(categoryDTO);
            }
        }
        throw new PlayOnLinuxError(String.format("The category %s does not exist!", categoryName));
    }

    private Iterable<ScriptDTO> getAllScriptsInCategory(CategoryDTO categoryDTO) {
        List<ScriptDTO> scripts = new ArrayList<>();
        for(ScriptDTO scriptDTO: new ArrayList<>(categoryDTO.getScripts())) {
            scripts.add(scriptDTO);
        }

        Collections.sort(scripts, new ScriptDTO.AlphabeticalOrderComparator());

        return () -> scripts.iterator();
    }



    public class InstallerFilter extends com.playonlinux.ui.api.RemoteAvailableInstallers.InstallerFilter {

        private final OperatingSystem hostOs;

        public InstallerFilter(){
            //predefine filters
            title = null;
            category = null;
            showTesting = false;
            showNoCd = false;
            showCommercial = true;

            OperatingSystem os;
            try{
                os = OperatingSystem.fetchCurrentOperationSystem();
            }catch (PlayOnLinuxError err){
                os = null;
            }
            this.hostOs = os;
        }

        @Override
        public boolean apply(ScriptDTO script){
            //TODO: write testcases for this method.
            ScriptInformationsDTO scriptInfo = script.getScriptInformations();
            boolean isTesting = scriptInfo.getTestingOperatingSystems().contains(hostOs);

            if(isTesting && !showTesting){ return false; }
            if(scriptInfo.isRequiresNoCD() && !showNoCd){ return false; }
            if(!scriptInfo.isFree() && !showCommercial){ return false; }
            if(StringUtils.isNotBlank(title) && !script.getName().contains(title)){
                return false;
            }

            return true;
        }
    }


}
