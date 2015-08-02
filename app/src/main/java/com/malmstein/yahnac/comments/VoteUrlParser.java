package com.malmstein.yahnac.comments;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class VoteUrlParser {

    private final Document document;

    public VoteUrlParser(Document document) {
        this.document = document;
    }

    private static Elements getStoryRows(Document doc) {
        return doc.select("td.subtext").first().parent().siblingElements();
    }

    public String parse() {

        Elements tableRows = document.select("table tr table tr:has(table)");
        Element voteElement = tableRows.get(0).select("td:eq(1) a").first();

        Elements storyRows = getStoryRows(document);
        Element line1 = storyRows.first();
        Element line2 = document.select("td.subtext").first();
        Element voteAnchor = line1.select("a[href^=vote]")
                .first();

        if (voteAnchor != null) {
            String[] voteHref = voteAnchor.attr("href")
                    .split("[=&]");
            String whence = voteHref[voteHref.length - 1];
            String auth = voteHref[7];
        }

        return "/vote?for=9990160&dir=up&auth=8456646fcc085cb860fbdb98c6d238debe09f511&goto=item%3Fid%3D9990160";

//        Element voteElement = tableRows.get(0).select("td:eq(1) a").first();
//        return parseVoteUrl(voteElement);
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

