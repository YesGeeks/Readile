package com.readile.readile.utils;

import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class BookAPIConnector {
    private static final String SEARCH_BASE_URL = "https://openlibrary.org/search.json";
    private static final String COVER_BASE_URL = "https://covers.openlibrary.org/b/id/";

    private static final WebClient webClient = WebClient.builder()
            .exchangeStrategies(ExchangeStrategies.builder()
                    .codecs(configurer -> configurer
                            .defaultCodecs()
                            .maxInMemorySize(32 * 1024 * 1024))
                    .build())
            .baseUrl(SEARCH_BASE_URL).build();

    public static List<ResultBook> getSearchResults(String title) {
        SearchResults searchResults = webClient.get()
                .uri("?title={title}&limit={limit}", title, 20)
                .retrieve()
                .bodyToMono(SearchResults.class)
                .block();

        searchResults.getDocs()
                .stream()
                .forEach(resultBook -> {
                    String coverId = StringUtils.hasText(resultBook.getCover_i()) ?
                            String.format("%s%s%s", COVER_BASE_URL, resultBook.getCover_i(), "-L.jpg") :
                            String.valueOf(BookAPIConnector.class.getResource("/images/no_cover.png"));
                    resultBook.setCover_i(coverId);
                });

        return searchResults.getDocs()
                .stream()
                .filter(resultBook -> !resultBook.getTitle().equals("No title") &&
                        resultBook.getNumber_of_pages_median() != null && resultBook.getAuthor_name() != null)
                .collect(Collectors.toList());
    }

}