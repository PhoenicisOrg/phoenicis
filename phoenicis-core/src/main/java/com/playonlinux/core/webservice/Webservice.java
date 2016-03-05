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
import java.util.function.Consumer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.playonlinux.core.dto.DTO;
import com.playonlinux.core.entities.ProgressEntity;
import com.playonlinux.core.entities.ProgressState;
import com.playonlinux.core.services.manager.Service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class Webservice<T extends DTO> implements Service {
    private final URL url;
    private final Semaphore updateSemaphore = new Semaphore(1);

    private List<T> items;
    @Setter
    private Consumer<DownloadEnvelope<Collection<T>>> onDownloadUpdate;

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
                log.warn(String.format("Error while downloading %s", url), e);
                update(ProgressState.FAILED);
            } catch (IOException e) {
                log.warn(String.format("IO error while downloading %s", url), e);
                update(ProgressState.FAILED);
            }
        } catch (InterruptedException e) {
            log.info(String.format("The download was interrupted: %s", url), e);
        } finally {
            updateSemaphore.release();
        }
    }

    protected abstract TypeReference<List<T>> defineTypeReference();

    private synchronized void update(ProgressState state) {
        DownloadEnvelope<Collection<T>> downloadEnvelope = new DownloadEnvelope<>();
        ProgressEntity progressStateEntity = new ProgressEntity.Builder().withState(state).build();

        downloadEnvelope.setDownloadState(progressStateEntity);
        downloadEnvelope.setEnvelopeContent(items);

        if (this.onDownloadUpdate != null) {
            this.onDownloadUpdate.accept(downloadEnvelope);
        }
    }

    @Override
    public void shutdown() {
        // Nothing to do
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
