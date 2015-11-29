/*
 * Copyright (C) 2015 Markus Ebner
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

package com.playonlinux.qt.common;

import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QPixmap;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class displaying an easy-to-use single-frame based animation on a passed QLabel
 */
public class AnimationHelper {

    private Timer animationTimer;

    private QLabel viewport;

    private int delay;
    private int currentFrame = 0;
    private List<QPixmap> frames = new ArrayList<>();

    public AnimationHelper(QLabel viewport, int delay){
        this.viewport = viewport;
        this.delay = delay;
    }

    public void addFrame(QPixmap pixmap){
        if(!pixmap.isNull()){
            frames.add(pixmap);
        }
    }

    public void start(){
        if(frames.size() < 1){
            throw new RuntimeException("No Frames have been added to this animation");
        }
        animationTimer = new Timer();
        animationTimer.schedule(animateTask, 0, delay);
    }

    public void stop(){
        animationTimer.cancel();
        animationTimer = null;
    }


    /* ANIMATING */
    private TimerTask animateTask = new TimerTask() {
        @Override
        public void run() {
            QApplication.invokeAndWait(() -> {
                viewport.setPixmap(frames.get(currentFrame++));
                currentFrame %= frames.size();
            });
        }
    };

}
