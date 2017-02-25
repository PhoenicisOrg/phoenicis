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

package org.phoenicis.tests;

import org.phoenicis.cli.setupwindow.CliMessageSender;
import org.phoenicis.scripts.ui.SetupUiFactory;
import org.phoenicis.scripts.ui.UiConfiguration;
import org.phoenicis.scripts.ui.UiMessageSender;
import org.phoenicis.scripts.ui.UiQuestionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class TestUiConfiguration implements UiConfiguration {
    @Override
    @Bean
    public SetupUiFactory setupUiFactory() {
        return title -> new TestSetupUi();
    }

    @Override
    @Bean
    public UiMessageSender uiMessageSender() {
        return new CliMessageSender();
    }

    @Override
    @Bean
    public UiQuestionFactory uiQuestionFactory() {
        return (questionText, yesCallback, noCallback) -> yesCallback.run();
    }
}
