package com.netflix.testingdemo;

import com.netflix.testingdemo.lolomo.datafetchers.LolomoDataFetcher;
import com.netflix.testingdemo.lolomo.generated.types.Category;
import com.netflix.testingdemo.lolomo.generated.types.Show;
import com.netflix.testingdemo.lolomo.repository.ShowEntity;
import com.netflix.testingdemo.lolomo.repository.ShowsRepository;
import com.netflix.testingdemo.lolomo.top10.Top10Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LolomoDataFetcherUnitTest {
    private static final List<Show> SHOW_LIST = List.of(
            Show.newBuilder().title("Show 1").build(),
            Show.newBuilder().title("Show 2").build(),
            Show.newBuilder().title("Show 3").build()
    );
    @Mock
    Top10Service top10Service;

    @Mock
    ShowsRepository showsRepository;

    LolomoDataFetcher dataFetcher;

    @BeforeEach
    void setup() {
        dataFetcher = new LolomoDataFetcher(top10Service, showsRepository);
    }

    @Test
    void testTop10() {
        when(top10Service.top10("USA")).thenReturn(SHOW_LIST);
        var top10 = dataFetcher.top10("USA");

        assertThat(top10).extracting("title").containsExactly("Show 1", "Show 2", "Show 3");
        verify(top10Service).top10("USA");
    }

    @Test
    void testTop10WithNoCountry() {
        when(top10Service.top10("USA")).thenReturn(SHOW_LIST);
        var top10 = dataFetcher.top10(null);

        assertThat(top10).extracting("title").containsExactly("Show 1", "Show 2", "Show 3");
        verify(top10Service).top10("USA");
    }

    @Test
    void testTop10WithEmptyCountry() {
        when(top10Service.top10("USA")).thenReturn(SHOW_LIST);
        var top10 = dataFetcher.top10("");

        assertThat(top10).extracting("title").containsExactly("Show 1", "Show 2", "Show 3");
        verify(top10Service).top10("USA");
    }

    @Test
    void testShowsInCategories() {

        var showEntity1 = new ShowEntity();
        showEntity1.setTitle("Show 1");

        var showEntity2 = new ShowEntity();
        showEntity2.setTitle("Show 2");

        var showEntity3 = new ShowEntity();
        showEntity3.setTitle("Show 3");

        when(showsRepository.findByCategory(Category.ACTION.name())).thenReturn(List.of(showEntity1, showEntity2, showEntity3));

        var shows = dataFetcher.showsInCategory(Category.ACTION);
        assertThat(shows).extracting("title").containsExactly("Show 1", "Show 2", "Show 3");
    }

    @Test
    void testShowsInCategoriesWithoutCategory() {
        assertThrows(IllegalArgumentException.class, () -> dataFetcher.showsInCategory(null));
    }
}