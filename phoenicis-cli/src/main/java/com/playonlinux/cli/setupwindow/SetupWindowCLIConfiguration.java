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
