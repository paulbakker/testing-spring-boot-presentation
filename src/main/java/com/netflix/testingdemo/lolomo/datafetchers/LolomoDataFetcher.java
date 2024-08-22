package com.netflix.testingdemo.lolomo.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.netflix.testingdemo.lolomo.generated.types.Category;
import com.netflix.testingdemo.lolomo.generated.types.Show;
import com.netflix.testingdemo.lolomo.repository.ShowEntity;
import com.netflix.testingdemo.lolomo.repository.ShowsRepository;
import com.netflix.testingdemo.lolomo.top10.Top10Service;

import java.util.List;
import java.util.Set;

@DgsComponent
public class LolomoDataFetcher {

    private final ShowsRepository showsRepository;
    private final Top10Service top10Service;
    private final Set<String> supportedCountryCodes = Set.of("USA", "JPN", "NLD", "GBR");

    public LolomoDataFetcher(Top10Service top10Service, ShowsRepository showsRepository) {
        this.top10Service = top10Service;
        this.showsRepository = showsRepository;
    }

    @DgsQuery
    public List<Show> top10(@InputArgument String countryCode) {
        countryCode = countryCode != null  && !countryCode.isEmpty() ? countryCode: "USA";

        if(!supportedCountryCodes.contains(countryCode)) {
            throw new Top10NotAvailableException(countryCode);
        }

        return top10Service.top10(countryCode);
    }

    @DgsQuery
    public List<Show> showsInCategory(@InputArgument Category category) {
        if(category == null) {
            throw new IllegalArgumentException("Required argument category is not provided");
        }

        var shows = showsRepository.findByCategory(category.name());
        return shows.stream().map(ShowEntity::asShow).toList();
    }

}



