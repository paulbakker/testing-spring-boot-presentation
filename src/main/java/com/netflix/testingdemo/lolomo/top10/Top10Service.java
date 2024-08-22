package com.netflix.testingdemo.lolomo.top10;

import com.netflix.testingdemo.lolomo.ai.OpenAiService;
import com.netflix.testingdemo.lolomo.generated.types.Show;
import com.netflix.testingdemo.lolomo.repository.ShowsRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Top10Service {

    private final Logger LOGGER = LoggerFactory.getLogger(Top10Service.class);
    private final OpenAiService openAiService;
    private final ShowsRepository showsRepository;
    private final Map<String, List<Show>> cachedTop10 = new ConcurrentHashMap<>();

    public Top10Service(OpenAiService openAiService, ShowsRepository showsRepository) {
        this.openAiService = openAiService;
        this.showsRepository = showsRepository;
    }

    @PostConstruct
    public void prefetchTop10Data() {

        LOGGER.info("Prefetching top 10");
        var usa = top10("USA");
        if(!usa.isEmpty()) {
            cachedTop10.put("USA", usa);
        }
        var jpn = top10("JPN");
        if(!jpn.isEmpty()) {
            cachedTop10.put("JPN", jpn);
        }
        LOGGER.info("Completed prefetching top 10 for countries: {}", cachedTop10.keySet());
    }

    public List<Show> top10(String country) {
        if(cachedTop10.containsKey(country)) {
            return cachedTop10.get(country);
        } else {
            List<Show> shows = new ArrayList<>();
            var top10ShowsFromAI = openAiService.top10(country);
            showsRepository.findAllById(top10ShowsFromAI).forEach(show -> shows.add(show.asShow()));
            return shows;
        }
    }
}
