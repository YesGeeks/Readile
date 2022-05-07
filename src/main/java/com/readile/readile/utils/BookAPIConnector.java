package com.readile.readile.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookAPIConnector {

    private static final String SEARCH_BASE_URL = "https://openlibrary.org/search.json";
    private static final String COVER_BASE_URL = "http://covers.openlibrary.org/b/id/";

    private static final WebClient webClient = WebClient.builder()
            .exchangeStrategies(ExchangeStrategies.builder()
                    .codecs(configurer -> configurer
                            .defaultCodecs()
                            .maxInMemorySize(32 * 1024 * 1024))
                    .build())
            .baseUrl(SEARCH_BASE_URL).build();

    private BookAPIConnector() {
    }

    public static List<ResultBook> getSearchResults(String query) {
        String jsonResult =  webClient.get()
                .uri("?q={query}", query)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return parseBooksJSON(jsonResult)
                .stream()
                .limit(20)
                .collect(Collectors.toList());
    }

    private static List<ResultBook> parseBooksJSON(String json) {
        List<ResultBook> books = new ArrayList<>();
        JSONArray booksJsonArray = new JSONObject(json).getJSONArray("docs");

        for (int i = 0; i < booksJsonArray.length(); i++) {
            JSONObject bookJsonObject = booksJsonArray.getJSONObject(i);
            String title = bookJsonObject.optString("title", "No title");
            int length = bookJsonObject.optInt("number_of_pages_median", -1);
            int coverId = bookJsonObject.optInt("cover_i", -1);
            String coverURL = (coverId > 0) ?
                    String.format("%s%d%s", COVER_BASE_URL, coverId, "-L.jpg") :
                    "default-book-image.jpg";
            List<String> authorNames = new ArrayList<>();
            JSONArray authorNamesJsonArray = bookJsonObject.optJSONArray("author_name");
            if(authorNamesJsonArray != null)
                for (int j = 0; j < authorNamesJsonArray.length(); j++)
                    authorNames.add(authorNamesJsonArray.getString(j));

            books.add(new ResultBook(title, coverURL, length, authorNames));
        }
        return books;
    }
}
