package com.netflix.testingdemo;

import com.jayway.jsonpath.TypeRef;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.test.EnableDgsTest;
import com.netflix.testingdemo.lolomo.datafetchers.CustomDgsExceptionHandler;
import com.netflix.testingdemo.lolomo.datafetchers.LolomoDataFetcher;
import com.netflix.testingdemo.lolomo.generated.types.Category;
import com.netflix.testingdemo.lolomo.generated.types.Show;
import com.netflix.testingdemo.lolomo.repository.ShowEntity;
import com.netflix.testingdemo.lolomo.repository.ShowsRepository;
import com.netflix.testingdemo.lolomo.top10.Top10Service;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {LolomoDataFetcher.class, Top10Service.class, CustomDgsExceptionHandler.class, LolomoTestConfiguration.class})
@EnableDgsTest
public class LolomoDataFetcherSpringTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @MockBean
    ShowsRepository showsRepository;

    private static final List<Show> SHOW_LIST = List.of(
            Show.newBuilder().title("Show 1").categories(List.of(Category.ACTION, Category.DRAMA)).build(),
            Show.newBuilder().title("Show 2").categories(List.of(Category.SPORT)).build(),
            Show.newBuilder().title("Show 3").categories(List.of(Category.DRAMA)).build()
    );

    @Test
    void testTop10() {

        when(showsRepository.findAllById(any())).thenReturn(SHOW_LIST.stream().map(ShowEntity::fromShow).toList());

        @Language("GraphQL")
        var query = """
                query {
                    top10(countryCode: "USA") {
                        title
                        categories
                    }
                }
                """;

        var shows = dgsQueryExecutor.executeAndExtractJsonPathAsObject(query, "data.top10", new TypeRef<List<Show>>() {
        });

        assertThat(shows).hasSize(3);
    }

    @Test
    void testTop10DefaultCountry() {

        when(showsRepository.findAllById(any())).thenReturn(SHOW_LIST.stream().map(ShowEntity::fromShow).toList());

        @Language("GraphQL")
        var query = """
                query {
                    top10 {
                        title
                        categories
                    }
                }
                """;

        var result = dgsQueryExecutor.execute(query);
        assertThat(result.getErrors()).isEmpty();
    }

    @Test
    void testTop10UnsupportedCountry() {
        @Language("GraphQL")
        var query = """
                query {
                    top10(countryCode: "bogus") {
                        title
                        categories
                    }
                }
                """;

        var result = dgsQueryExecutor.execute(query);
        assertThat(result.getErrors()).extracting("message").containsExactly("Top 10 unavailable");
    }
}
