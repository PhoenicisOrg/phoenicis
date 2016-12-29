package com.playonlinux.apps;

import com.playonlinux.apps.dto.ApplicationDTO;
import com.playonlinux.apps.dto.CategoryDTO;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;


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


    @Test
    public void testFetchInstallableApplications_categoriesAndAppsCollapse_numberOfResultIsCorrect() {
        final ApplicationsSource leftSource = () -> Arrays.asList(
                new CategoryDTO.Builder()
                        .withName("Category 1")
                        .build(),
                new CategoryDTO.Builder()
                        .withName("Category 2")
                        .withApplications(
                                Arrays.asList(
                                        new ApplicationDTO.Builder()
                                                .withName("Application 1")
                                                .build(),
                                        new ApplicationDTO.Builder()
                                                .withName("Application 2")
                                                .build()
                                ))
                        .build()
        );

        final ApplicationsSource rightSource = () -> Arrays.asList(
                new CategoryDTO.Builder()
                        .withName("Category 2")
                        .withApplications(
                                Arrays.asList(
                                        new ApplicationDTO.Builder()
                                                .withName("Application 1")
                                                .build(),
                                        new ApplicationDTO.Builder()
                                                .withName("Application 3")
                                                .build()
                                ))
                        .build(),
                new CategoryDTO.Builder()
                        .withName("Category 4")
                        .build(),
                new CategoryDTO.Builder()
                        .withName("Category 5")
                        .build()
        );

        final ApplicationsSource teeSource = new TeeApplicationsSource(leftSource, rightSource);
        assertEquals(3, teeSource.getCategory(Collections.singletonList("Category 2")).getApplications().size());
    }


}