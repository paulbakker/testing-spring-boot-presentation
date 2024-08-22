package com.netflix.testingdemo.lolomo.ai;

import com.netflix.testingdemo.lolomo.generated.types.Show;
import com.netflix.testingdemo.lolomo.repository.ShowsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OpenAiService {

    private final static Logger LOGGER = LoggerFactory.getLogger(OpenAiService.class);
    private final ChatClient chatClient;
    private final ShowsRepository showsRepository;

    public OpenAiService(ChatClient chatClient, ShowsRepository showsRepository) {
        this.chatClient = chatClient;
        this.showsRepository = showsRepository;
    }

    public List<Show> generateShowData() {
        LOGGER.info("Retrieving show titles from OpenAI");

        var showTitles = chatClient.prompt()
                .system("The following list are the possible show categories: ACTION, DRAMA, DOCUMENTARY, HORROR, SPORT")
                .user("Give me 100 titles and their category with a short description of popular Netflix shows")
                .call().responseEntity(new ParameterizedTypeReference<List<Show>>() {
                })
                .entity();

        LOGGER.info("Found show titles from OpenAI: {}", showTitles);

        return showTitles;
    }

    public List<Long> top10(String country) {
        List<TitleWithId> titles = new ArrayList<>();
        showsRepository.findAll().forEach(s -> titles.add(new TitleWithId(s.getTitle(), s.getId())));

        var titleInput = titles.stream().map(s -> "[identifier: %d] %s".formatted(s.id, s.title)).collect(Collectors.joining(","));
        var top10Titles = chatClient.prompt()
                .system("The following is the list of shows to consider. Each show has an identifier. %s".formatted(titleInput))
                .user("Give me the identifiers and titles of the top 10 shows in %s".formatted(country)).call().entity(new ParameterizedTypeReference<List<TitleWithId>>() {
                });

        LOGGER.info("Top 10: {}", top10Titles);

        return top10Titles.stream().map(TitleWithId::id).toList();
    }

    record TitleWithId(String title, Long id){}
}
