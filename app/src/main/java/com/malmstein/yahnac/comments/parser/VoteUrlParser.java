package com.malmstein.yahnac.comments.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class VoteUrlParser {

    public static final String EMPTY = "";
    private final Document document;
    private final Long storyId;

    public VoteUrlParser(Document document, Long storyId) {
        this.document = document;
        this.storyId = storyId;
    }

    public String parse() {

        Elements links = document.select("a[id=up_" + storyId + "]");
        if (links.size() > 0) {
            Element voteElement = links.get(0).select("a[href^=vote]").first();
            String url = voteElement.attr("href").contains("auth=") ?
                    (voteElement.attr("href")) : null;
            return "/" + url;

        } else {
            return EMPTY;
        }
    }

}

