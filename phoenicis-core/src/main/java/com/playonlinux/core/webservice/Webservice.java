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

package com.playonlinux.core.webservice;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.playonlinux.core.dto.DTO;
import com.playonlinux.core.entities.ProgressEntity;
import com.playonlinux.core.entities.ProgressState;
import com.playonlinux.core.observer.ObservableDefaultImplementation;
import com.playonlinux.core.services.manager.Service;

public abstract class Webservice<T extends DTO> extends ObservableDefaultImplementation<DownloadEnvelope<Collection<T>>>
        implements Service {
    private static final Logger LOGGER = Logger.getLogger(Webservice.class);

    private final URL url;
    private final Semaphore updateSemaphore = new Semaphore(1);

    private List<T> items;

    public Webservice(URL url) {
        this.url = url;
    }

    public synchronized void populate() {
        try {
            updateSemaphore.acquire();
            update(ProgressState.PROGRESSING);

            try {
                final ObjectMapper mapper = new ObjectMapper();
                final HTTPDownloader httpDownloader = new HTTPDownloader(this.url);
                final String result = httpDownloader.get();
                items = mapper.readValue(result, this.defineTypeReference());
                update(ProgressState.SUCCESS);
            } catch (DownloadException e) {
                LOGGER.warn(String.format("Error while downloading %s", url), e);
                update(ProgressState.FAILED);
            } catch (IOException e) {
                LOGGER.warn(String.format("IO error while downloading %s", url), e);
                update(ProgressState.FAILED);
            }
        } catch (InterruptedException e) {
            LOGGER.info(String.format("The download was interrupted: %s", url), e);
        } finally {
            updateSemaphore.release();
        }
    }

    protected abstract TypeReference<List<T>> defineTypeReference();

    private synchronized void update(ProgressState state) {
        DownloadEnvelope<Collection<T>> envelopeDTO = new DownloadEnvelope<>();
        ProgressEntity progressStateEntity = new ProgressEntity(state);

        envelopeDTO.setDownloadState(progressStateEntity);
        envelopeDTO.setEnvelopeContent(items);

        this.notifyObservers(envelopeDTO);
    }

    @Override
    public void shutdown() {
        this.deleteObservers();
    }

    @Override
    public synchronized void init() {
        new Thread() {
            @Override
            public void run() {
                populate();
            }
        }.start();
    }
}
