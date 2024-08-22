package com.netflix.testingdemo;

import com.jayway.jsonpath.TypeRef;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.test.EnableDgsTest;
import com.netflix.testingdemo.lolomo.datafetchers.LolomoDataFetcher;
import com.netflix.testingdemo.lolomo.generated.types.Category;
import com.netflix.testingdemo.lolomo.generated.types.Show;
import com.netflix.testingdemo.lolomo.repository.ShowsRepository;
import com.netflix.testingdemo.lolomo.top10.Top10Service;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {LolomoDataFetcher.class, ShowsRepository.class, Top10Service.class, LolomoTestConfiguration.class})
@EnableDgsTest
@EnableDatabaseTest
public class LolomoDataFetcherSpringTcTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Test
    void testTop10() {
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

        assertThat(shows).hasSize(10);
        assertThat(shows).extracting("title").containsExactly(
                "Stranger Things",
                "The Crown",
                "Tiger King",
                "Narcos",
                "Black Mirror",
                "The Haunting of Hill House",
                "Money Heist",
                "Making a Murderer",
                "13 Reasons Why",
                "The Witcher");

    }

    @Test
    void testShowsInCategory() {
        @Language("GraphQL")
        var query = """
            query {
                action: showsInCategory(category: ACTION) {
                    title
                    categories
                }
        
                sport: showsInCategory(category: SPORT) {
                    title
                    categories
                }
            }
        """;

        var actionShows = dgsQueryExecutor.executeAndExtractJsonPathAsObject(query, "data.action", new TypeRef<List<Show>>() {
        });
        var sportShows = dgsQueryExecutor.executeAndExtractJsonPathAsObject(query, "data.sport", new TypeRef<List<Show>>() {
        });

        assertThat(actionShows).allMatch(s -> s.getCategories().contains(Category.ACTION));
        assertThat(sportShows).allMatch(s -> s.getCategories().contains(Category.SPORT));
    }

}
