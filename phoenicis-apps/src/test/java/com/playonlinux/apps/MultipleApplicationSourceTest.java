package com.playonlinux.apps;

import com.playonlinux.apps.dto.CategoryDTO;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class MultipleApplicationSourceTest {
    @Test
    public void testWithEmptyList_emptySetIsReturned() {
        final MultipleApplicationSource multipleApplicationSource = new MultipleApplicationSource();
        assertEquals(0, multipleApplicationSource.fetchInstallableApplications().size());
    }

    @Test
    public void testWithThreeSources_threeResults() {
        final ApplicationsSource firstSource = () -> Collections.singletonList(
                new CategoryDTO.Builder()
                        .withName("Category 1")
                        .build()
        );

        final ApplicationsSource secondSource = () -> Collections.singletonList(
                new CategoryDTO.Builder()
                        .withName("Category 2")
                        .build()
        );

        final ApplicationsSource thirdSource = () -> Collections.singletonList(
                new CategoryDTO.Builder()
                        .withName("Category 3")
                        .build()
        );


        final MultipleApplicationSource multipleApplicationSource = new MultipleApplicationSource(firstSource, secondSource, thirdSource);
        assertEquals(3, multipleApplicationSource.fetchInstallableApplications().size());
    }
}