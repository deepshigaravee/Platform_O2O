package com.o2o.clothing.service;

import com.o2o.clothing.entity.Store;
import com.o2o.clothing.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SearchServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private SearchService searchService;

    @Test
    void findNearbyStores_ReturnsStoresWithinRadius() {
        Store store1 = new Store();
        store1.setName("Close Store");
        store1.setLatitude(40.7128);
        store1.setLongitude(-74.0060); // NY

        Store store2 = new Store();
        store2.setName("Far Store");
        store2.setLatitude(34.0522);
        store2.setLongitude(-118.2437); // LA

        when(storeRepository.findAllStores()).thenReturn(Arrays.asList(store1, store2));

        // NY Coordinates
        double userLat = 40.7128;
        double userLon = -74.0060;

        List<Store> result = searchService.findNearbyStores(userLat, userLon, 5.0);

        assertEquals(1, result.size());
        assertEquals("Close Store", result.get(0).getName());
    }
}
