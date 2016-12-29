package com.playonlinux.apps;

import com.playonlinux.apps.dto.CategoryDTO;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class BackgroundApplicationsSourceTest {
    private final ExecutorService mockExecutor = mock(ExecutorService.class);
    private final ApplicationsSource mockApplicationSource = mock(ApplicationsSource.class);
    private final BackgroundApplicationsSource backgroundApplicationsSource = new BackgroundApplicationsSource(mockApplicationSource, mockExecutor);
    private List<CategoryDTO> mockResults = Arrays.asList(mock(CategoryDTO.class), mock(CategoryDTO.class));

    @Test
    public void testFetchInstallableApplications_taskIsPassed() {
        doAnswer(invocation -> {
            ((Runnable) invocation.getArguments()[0]).run();
            return null;
        }).when(mockExecutor).submit(any(Runnable.class));

        when(mockApplicationSource.fetchInstallableApplications()).thenReturn(mockResults);
        backgroundApplicationsSource.fetchInstallableApplications(categoryDTOs -> {}, e -> {});

        verify(mockApplicationSource).fetchInstallableApplications(any(), any());
    }
}