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

import org.phoenicis.repository.RepositorySource;
import org.phoenicis.repository.dto.ApplicationCategoryDTO;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.ScriptDTO;
import org.phoenicis.multithreading.ControlledThreadPoolExecutorServiceCloser;
import org.phoenicis.scripts.interpreter.ScriptInterpreter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class PhoenicisTestsApp {
    private ApplicationContext applicationContext;
    private List<String> workingScripts = new ArrayList<>();
    private List<String> failingScripts = new ArrayList<>();

    public static void main(String[] args) {
        final PhoenicisTestsApp phoenicisTestsApp = new PhoenicisTestsApp();
        phoenicisTestsApp.run(args);
    }

    private void run(String[] args) {
        try(final ConfigurableApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(PhoenicisTestsConfiguration.class)) {

            final RepositorySource repositorySource = applicationContext.getBean("mockedApplicationSource", RepositorySource.class);
            this.applicationContext = applicationContext;

            repositorySource.fetchInstallableApplications(categoryDTOS -> {
                categoryDTOS.forEach(this::testCategory);
            }, e -> {
                throw new IllegalStateException(e);
            });

            applicationContext.getBean(ControlledThreadPoolExecutorServiceCloser.class).close();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void testCategory(ApplicationCategoryDTO applicationCategoryDTO) {
        System.out.println("+ " + applicationCategoryDTO.getName());
        applicationCategoryDTO.getApplications().forEach(applicationDTO -> this.testApplication(applicationCategoryDTO, applicationDTO));
    }

    private void testApplication(ApplicationCategoryDTO applicationCategoryDTO, ApplicationDTO applicationDTO) {
        System.out.println("|-+ " + applicationDTO.getName());
        applicationDTO.getScripts().forEach(scriptDTO -> testScript(applicationCategoryDTO, applicationDTO, scriptDTO));
        System.out.println("|");
    }

    private void testScript(ApplicationCategoryDTO applicationCategoryDTO, ApplicationDTO applicationDTO, ScriptDTO scriptDTO) {
        final ScriptInterpreter scriptInterpreter = applicationContext.getBean("nashornInterprpeter", ScriptInterpreter.class);
        System.out.print("| |-- " + scriptDTO.getName());
        try {
            scriptInterpreter.runScript(scriptDTO.getScript(), e -> {
                throw new TestException(e);
            });
            System.out.println(" [OK] ");
        } catch(TestException e) {
            e.printStackTrace();
            System.out.println(" [KO] ");
        }
    }
}
