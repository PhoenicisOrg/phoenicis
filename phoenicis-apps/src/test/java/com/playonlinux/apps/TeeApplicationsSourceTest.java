package com.playonlinux.apps;

import com.playonlinux.apps.dto.CategoryDTO;
import javafx.application.Application;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class TeeApplicationsSourceTest {
    @Test
    public void testFetchInstallableApplications_onlyCategoriesNoCollapse_numberOfResultIsCorrect() {
        final ApplicationsSource leftSource = () -> Arrays.asList(
                new CategoryDTO.Builder()
                        .withName("Category 1")
                        .build(),
                new CategoryDTO.Builder()
                        .withName("Category 2")
                        .build()
        );

        final ApplicationsSource rightSource = () -> Arrays.asList(
                new CategoryDTO.Builder()
                        .withName("Category 3")
                        .build(),
                new CategoryDTO.Builder()
                        .withName("Category 4")
                        .build(),
                new CategoryDTO.Builder()
                        .withName("Category 5")
                        .build()
        );

        final ApplicationsSource teeSource = new TeeApplicationsSource(leftSource, rightSource);
        assertEquals(5, teeSource.fetchInstallableApplications().size());
    }


    @Test
    public void testFetchInstallableApplications_onlyCategoriesCollapse_numberOfResultIsCorrect() {
        final ApplicationsSource leftSource = () -> Arrays.asList(
                new CategoryDTO.Builder()
                        .withName("Category 1")
                        .build(),
                new CategoryDTO.Builder()
                        .withName("Category 2")
                        .build()
        );

        final ApplicationsSource rightSource = () -> Arrays.asList(
                new CategoryDTO.Builder()
                        .withName("Category 2")
                        .build(),
                new CategoryDTO.Builder()
                        .withName("Category 4")
                        .build(),
                new CategoryDTO.Builder()
                        .withName("Category 5")
                        .build()
        );

        final ApplicationsSource teeSource = new TeeApplicationsSource(leftSource, rightSource);
        assertEquals(4, teeSource.fetchInstallableApplications().size());
    }




}