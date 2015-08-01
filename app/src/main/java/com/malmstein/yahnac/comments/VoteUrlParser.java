package com.malmstein.yahnac.comments;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class VoteUrlParser {

    private final Document document;

    public VoteUrlParser(Document document) {
        this.document = document;
    }

    public String parse() {
        Elements tableRows = document.select("table tr table tr:has(table)");

        Element voteElement = tableRows.get(0).select("td:eq(1) a").first();
        return parseVoteUrl(voteElement);
    }

    public String parseVoteUrl(Element voteElement) {
        if (voteElement != null) {
            String url = voteElement.attr("href").contains("auth=") ?
                    (voteElement.attr("href")) : null;
            return "/" + url;
        }

        return null;
    }
}

