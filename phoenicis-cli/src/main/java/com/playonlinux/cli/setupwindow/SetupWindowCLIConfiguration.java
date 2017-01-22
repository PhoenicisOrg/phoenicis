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

package com.playonlinux.cli.setupwindow;

import com.playonlinux.scripts.ui.SetupWindowFactory;
import com.playonlinux.scripts.ui.SetupWindowUIConfiguration;
import com.playonlinux.scripts.ui.UIMessageSender;
import com.playonlinux.scripts.ui.UIQuestionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration
public class SetupWindowCLIConfiguration implements SetupWindowUIConfiguration {
    @Override
    @Bean
    public SetupWindowFactory setupWindowFactory() {
        return (title) -> new SetupWindowCLIImplementation(title, true, true);
    }

    @Override
    @Bean
    public UIMessageSender uiMessageSender() {
        return new CLIMessageSender();
    }

    @Override
    @Bean
    public UIQuestionFactory uiQuestionFactory() {
        return (questionText, yesCallback, noCallback) -> {
            String answer = "";

            while (!"yes".equals(answer) && !"no".equals(answer)) {
                System.out.println(questionText);
                System.out.print("Please enter: [yes, no] ");

                Scanner input = new Scanner(System.in);
                answer = input.nextLine();

                switch (answer) {
                    case "yes":
                        yesCallback.run();
                        break;
                    case "no":
                        noCallback.run();
                        break;
                }
            }
        };
    }
}
