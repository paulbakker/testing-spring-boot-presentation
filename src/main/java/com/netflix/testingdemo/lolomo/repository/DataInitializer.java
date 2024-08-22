package com.netflix.testingdemo.lolomo.repository;

import com.netflix.testingdemo.lolomo.ai.OpenAiService;
import com.netflix.testingdemo.lolomo.config.LolomoApplicationConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer {
    private final LolomoApplicationConfig lolomoApplicationConfig;
    private final ShowsRepository showsRepository;
    private final OpenAiService openAiService;

    public DataInitializer(LolomoApplicationConfig lolomoApplicationConfig, ShowsRepository showsRepository, OpenAiService openAiService) {
        this.lolomoApplicationConfig = lolomoApplicationConfig;
        this.showsRepository = showsRepository;
        this.openAiService = openAiService;
    }

    @PostConstruct
    public void initShows() {
        if(lolomoApplicationConfig.isInitializeShows()) {
            showsRepository.deleteAll();

            var titles = openAiService.generateShowData();

            titles.forEach(show -> {
                var showEntity = new ShowEntity();
                showEntity.setTitle(show.getTitle());
                showEntity.setDescription(show.getDescription());
                showEntity.setCategories(Set.copyOf(show.getCategories()));
                showsRepository.save(showEntity);
            });
        }
    }
}
