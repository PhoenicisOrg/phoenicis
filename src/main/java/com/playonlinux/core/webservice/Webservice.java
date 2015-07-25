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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.playonlinux.core.dto.AbstractDTO;
import com.playonlinux.core.entities.ProgressStateEntity;
import com.playonlinux.core.services.manager.Service;
import com.playonlinux.core.observer.ObservableDefaultImplementation;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Semaphore;

abstract public class Webservice<T extends AbstractDTO>
        extends ObservableDefaultImplementation<DownloadEnvelope<Collection<T>>> implements Service {
    private static final Logger LOGGER = Logger.getLogger(Webservice.class);

    private final URL url;
    private final Semaphore updateSemaphore = new Semaphore(1);

    private List<T> items;
    private ProgressStateEntity.State state = ProgressStateEntity.State.READY;

    public Webservice(URL url) {
        this.url = url;
    }

    public synchronized void populate() {
        try {
            updateSemaphore.acquire();
            this.state = ProgressStateEntity.State.PROGRESSING;
            this.update();

            try {
                ObjectMapper mapper = new ObjectMapper();
                HTTPDownloader httpDownloader = new HTTPDownloader(this.url);
                String result = httpDownloader.get();
                items = mapper.readValue(result, this.defineTypeReference());
                this.state = ProgressStateEntity.State.SUCCESS;
            } catch(DownloadException e) {
                LOGGER.warn(String.format("Error while downloading %s", url), e);
                this.state = ProgressStateEntity.State.FAILED;
            } catch (IOException e) {
                LOGGER.warn(String.format("IO error while downloading %s", url), e);
                this.state = ProgressStateEntity.State.FAILED;
            } finally {
                this.update();
            }
        } catch (InterruptedException ignored) {
            LOGGER.info(String.format("The download was interrupted: %s", url), ignored);
        } finally {
            updateSemaphore.release();
        }
    }

    protected abstract TypeReference defineTypeReference();


    private synchronized void update() {
        DownloadEnvelope<Collection<T>> envelopeDTO = new DownloadEnvelope<>();
        ProgressStateEntity progressStateEntity = new ProgressStateEntity.Builder().withState(state).build();

        envelopeDTO.setDownloadState(progressStateEntity);
        envelopeDTO.setEnvelopeContent(items);

        this.notifyObservers(envelopeDTO);
    }

    @Override
    public void shutdown() {
        this.deleteObservers();
    }

    @Override
    public synchronized void start() {
        new Thread() {
            @Override
            public void run() {
                populate();
            }
        }.start();
    }
}
