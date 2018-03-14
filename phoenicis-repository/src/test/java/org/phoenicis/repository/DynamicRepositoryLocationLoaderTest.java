package org.phoenicis.repository;

import org.junit.Test;
import org.phoenicis.repository.location.RepositoryLocation;
import org.phoenicis.repository.repositoryTypes.Repository;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class DynamicRepositoryLocationLoaderTest {
    private final RepositoryLocationLoader mockDelegated = mock(RepositoryLocationLoader.class);
    private final DynamicRepositoryLocationLoader dynamicRepositoryLocationLoader = new DynamicRepositoryLocationLoader(
            mockDelegated);

    @Test
    public void loadRepositoryLocations() {
        final List<RepositoryLocation<? extends Repository>> mockRepositoriesToBeLoaded = Arrays.asList(
                mock(RepositoryLocation.class),
                mock(RepositoryLocation.class));

        when(mockDelegated.loadRepositoryLocations()).thenReturn(mockRepositoriesToBeLoaded);
        assertSame(mockRepositoriesToBeLoaded, dynamicRepositoryLocationLoader.loadRepositoryLocations());

        verify(mockDelegated).loadRepositoryLocations();
    }

    @Test
    public void saveRepositories() {
        final List<RepositoryLocation<? extends Repository>> mockRepositoriesToBeSaved = Arrays.asList(
                mock(RepositoryLocation.class),
                mock(RepositoryLocation.class));

        mockDelegated.saveRepositories(mockRepositoriesToBeSaved);

        verify(mockDelegated).saveRepositories(mockRepositoriesToBeSaved);
    }

}