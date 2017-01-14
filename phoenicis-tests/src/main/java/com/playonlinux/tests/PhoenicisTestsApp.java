package com.playonlinux.tests;

import com.playonlinux.apps.ApplicationsSource;
import com.playonlinux.apps.dto.ApplicationDTO;
import com.playonlinux.apps.dto.CategoryDTO;
import com.playonlinux.apps.dto.ScriptDTO;
import com.playonlinux.multithreading.ControlledThreadPoolExecutorServiceCloser;
import com.playonlinux.scripts.interpreter.ScriptInterpreter;
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

            final ApplicationsSource applicationsSource = applicationContext.getBean("mockedApplicationSource", ApplicationsSource.class);
            this.applicationContext = applicationContext;

            applicationsSource.fetchInstallableApplications(categoryDTOS -> {
                categoryDTOS.forEach(this::testCategory);
            }, e -> {
                throw new IllegalStateException(e);
            });

            applicationContext.getBean(ControlledThreadPoolExecutorServiceCloser.class).close();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void testCategory(CategoryDTO categoryDTO) {
        System.out.println("+ " + categoryDTO.getName());
        categoryDTO.getApplications().forEach(applicationDTO -> this.testApplication(categoryDTO, applicationDTO));
    }

    private void testApplication(CategoryDTO categoryDTO, ApplicationDTO applicationDTO) {
        System.out.println("|-+ " + applicationDTO.getName());
        applicationDTO.getScripts().forEach(scriptDTO -> testScript(categoryDTO, applicationDTO, scriptDTO));
        System.out.println("|");
    }

    private void testScript(CategoryDTO categoryDTO, ApplicationDTO applicationDTO, ScriptDTO scriptDTO) {
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
