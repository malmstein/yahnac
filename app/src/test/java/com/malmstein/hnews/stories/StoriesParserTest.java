package com.malmstein.hnews.stories;

import android.content.ContentValues;

import com.malmstein.hnews.model.Story;

import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;

import static org.junit.Assert.assertEquals;

public class StoriesParserTest {

    private static final String news = "\n" +
            "<html><head><meta name=\"referrer\" content=\"origin\"></meta><link rel=\"stylesheet\" type=\"text/css\" href=\"news.css?U63Elf83xZvQIKnWmYyZ\"></link><link rel=\"shortcut icon\" href=\"favicon.ico\"></link><link rel=\"alternate\" type=\"application/rss+xml\" title=\"RSS\" href=\"rss\"></link><script type=\"text/javascript\">\n" +
            "function byId(id) {\n" +
            "  return document.getElementById(id);\n" +
            "}\n" +
            "\n" +
            "function vote(node) {\n" +
            "  var v = node.id.split(/_/);   // {'up', '123'}\n" +
            "  var item = v[1];\n" +
            "\n" +
            "  // hide arrows\n" +
            "  byId('up_'   + item).style.visibility = 'hidden';\n" +
            "  byId('down_' + item).style.visibility = 'hidden';\n" +
            "\n" +
            "  // ping server\n" +
            "  var ping = new Image();\n" +
            "  ping.src = node.href;\n" +
            "\n" +
            "  return false; // cancel browser nav\n" +
            "} </script><title>Hacker News</title></head><body><center><table id=\"hnmain\" op=\"news\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"85%\" bgcolor=\"#f6f6ef\"><tr><td bgcolor=\"#ff6600\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"padding:2px\"><tr><td style=\"width:18px;padding-right:4px\"><a href=\"http://www.ycombinator.com\"><img src=\"y18.gif\" width=\"18\" height=\"18\" style=\"border:1px #ffffff solid;\"></img></a></td><td style=\"line-height:12pt; height:10px;\"><span class=\"pagetop\"><b><a href=\"news\">Hacker News</a></b><img src=\"s.gif\" height=\"1\" width=\"10\"><a href=\"newest\">new</a> | <a href=\"newcomments\">comments</a> | <a href=\"show\">show</a> | <a href=\"ask\">ask</a> | <a href=\"jobs\">jobs</a> | <a href=\"submit\">submit</a></span></td><td style=\"text-align:right;padding-right:4px;\"><span class=\"pagetop\"><a href=\"login?whence=%6e%65%77%73\">login</a></span></td></tr></table></td></tr><tr style=\"height:10px\"></tr><tr><td><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr><td align=\"right\" valign=\"top\" class=\"title\"><span class=\"rank\">1.</span></td><td><center><a id=\"up_9017719\" href=\"vote?for=9017719&amp;dir=up&amp;whence=%6e%65%77%73\"><div class=\"votearrow\" title=\"upvote\"></div></a><span id=\"down_9017719\"></span></center></td><td class=\"title\"><span class=\"deadmark\"></span><a href=\"https://github.com/ali1234/raspi-teletext\">Teletext for Raspberry Pi</a><span class=\"sitebit comhead\"> (github.com)</span></td></tr><tr><td colspan=\"2\"></td><td class=\"subtext\"><span class=\"score\" id=\"score_9017719\">30 points</span> by <a href=\"user?id=lawl\">lawl</a> 39 minutes ago  | <a href=\"item?id=9017719\">2 comments</a></td></tr><tr style=\"height:5px\"></tr><tr><td align=\"right\" valign=\"top\" class=\"title\"><span class=\"rank\">2.</span></td><td><center><a id=\"up_9017030\" href=\"vote?for=9017030&amp;dir=up&amp;whence=%6e%65%77%73\"><div class=\"votearrow\" title=\"upvote\"></div></a><span id=\"down_9017030\"></span></center></td><td class=\"title\"><span class=\"deadmark\"></span><a href=\"https://www.samsung.com/uk/info/privacy-SmartTV.html\">Samsung Global Privacy Policy - SmartTV Supplement</a><span class=\"sitebit comhead\"> (samsung.com)</span></td></tr><tr><td colspan=\"2\"></td><td class=\"subtext\"><span class=\"score\" id=\"score_9017030\">176 points</span> by <a href=\"user?id=tscherno\">tscherno</a> 5 hours ago  | <a href=\"item?id=9017030\">61 comments</a></td></tr><tr style=\"height:5px\"></tr><tr><td align=\"right\" valign=\"top\" class=\"title\"><span class=\"rank\">3.</span></td><td><center><a id=\"up_9017476\" href=\"vote?for=9017476&amp;dir=up&amp;whence=%6e%65%77%73\"><div class=\"votearrow\" title=\"upvote\"></div></a><span id=\"down_9017476\"></span></center></td><td class=\"title\"><span class=\"deadmark\"></span><a href=\"http://www.cnbc.com/id/102397137\">Ecuador becomes the first country to roll out its own digital durrency</a><span class=\"sitebit comhead\"> (cnbc.com)</span></td></tr><tr><td colspan=\"2\"></td><td class=\"subtext\"><span class=\"score\" id=\"score_9017476\">26 points</span> by <a href=\"user?id=prostoalex\">prostoalex</a> 2 hours ago  | <a href=\"item?id=9017476\">7 comments</a></td></tr><tr style=\"height:5px\"></tr><tr><td align=\"right\" valign=\"top\" class=\"title\"><span class=\"rank\">4.</span></td><td><center><a id=\"up_9017699\" href=\"vote?for=9017699&amp;dir=up&amp;whence=%6e%65%77%73\"><div class=\"votearrow\" title=\"upvote\"></div></a><span id=\"down_9017699\"></span></center></td><td class=\"title\"><span class=\"deadmark\"></span><a href=\"http://blog.expertinamonth.com/post/110437403117/learning-to-love-the-command-line\">One month command line challenge</a><span class=\"sitebit comhead\"> (expertinamonth.com)</span></td></tr><tr><td colspan=\"2\"></td><td class=\"subtext\"><span class=\"score\" id=\"score_9017699\">11 points</span> by <a href=\"user?id=winash\">winash</a> 46 minutes ago  | <a href=\"item?id=9017699\">7 comments</a></td></tr><tr style=\"height:5px\"></tr><tr><td align=\"right\" valign=\"top\" class=\"title\"><span class=\"rank\">5.</span></td><td><center><a id=\"up_9017600\" href=\"vote?for=9017600&amp;dir=up&amp;whence=%6e%65%77%73\"><div class=\"votearrow\" title=\"upvote\"></div></a><span id=\"down_9017600\"></span></center></td><td class=\"title\"><span class=\"deadmark\"></span><a href=\"http://cheapshot.co/\">Cheapshot – A map-based multiplayer shooter game for iPhone</a><span class=\"sitebit comhead\"> (cheapshot.co)</span></td></tr><tr><td colspan=\"2\"></td><td class=\"subtext\"><span class=\"score\" id=\"score_9017600\">16 points</span> by <a href=\"user?id=borodich\">borodich</a> 1 hour ago  | <a href=\"item?id=9017600\">14 comments</a></td></tr><tr style=\"height:5px\"></tr><tr><td align=\"right\" valign=\"top\" class=\"title\"><span class=\"rank\">6.</span></td><td><center><a id=\"up_9015663\" href=\"vote?for=9015663&amp;dir=up&amp;whence=%6e%65%77%73\"><div class=\"votearrow\" title=\"upvote\"></div></a><span id=\"down_9015663\"></span></center></td><td class=\"title\"><span class=\"deadmark\"></span><a href=\"http://www.raspberrypi.org/forums/viewtopic.php?f=28&amp;t=99042\">A Xenon flash will cause the Raspberry Pi 2 to freeze</a><span class=\"sitebit comhead\"> (raspberrypi.org)</span></td></tr><tr><td colspan=\"2\"></td><td class=\"subtext\"><span class=\"score\" id=\"score_9015663\">453 points</span> by <a href=\"user?id=voltagex_\">voltagex_</a> 18 hours ago  | <a href=\"item?id=9015663\">113 comments</a></td></tr><tr style=\"height:5px\"></tr><tr><td align=\"right\" valign=\"top\" class=\"title\"><span class=\"rank\">7.</span></td><td><center><a id=\"up_9017601\" href=\"vote?for=9017601&amp;dir=up&amp;whence=%6e%65%77%73\"><div class=\"votearrow\" title=\"upvote\"></div></a><span id=\"down_9017601\"></span></center></td><td class=\"title\"><span class=\"deadmark\"></span><a href=\"http://tldp.org/LDP/abs/html/process-sub.html\">Bash process substitution</a><span class=\"sitebit comhead\"> (tldp.org)</span></td></tr><tr><td colspan=\"2\"></td><td class=\"subtext\"><span class=\"score\" id=\"score_9017601\">6 points</span> by <a href=\"user?id=geoka9\">geoka9</a> 1 hour ago  | <a href=\"item?id=9017601\">3 comments</a></td></tr><tr style=\"height:5px\"></tr><tr><td align=\"right\" valign=\"top\" class=\"title\"><span class=\"rank\">8.</span></td><td><center><a id=\"up_9017041\" href=\"vote?for=9017041&amp;dir=up&amp;whence=%6e%65%77%73\"><div class=\"votearrow\" title=\"upvote\"></div></a><span id=\"down_9017041\"></span></center></td><td class=\"title\"><span class=\"deadmark\"></span><a href=\"http://c2.com/cgi/wiki?TheKenThompsonHack\">The Ken Thompson Hack</a><span class=\"sitebit comhead\"> (c2.com)</span></td></tr><tr><td colspan=\"2\"></td><td class=\"subtext\"><span class=\"score\" id=\"score_9017041\">43 points</span> by <a href=\"user?id=yla92\">yla92</a> 5 hours ago  | <a href=\"item?id=9017041\">20 comments</a></td></tr><tr style=\"height:5px\"></tr><tr><td align=\"right\" valign=\"top\" class=\"title\"><span class=\"rank\">9.</span></td><td><center><a id=\"up_9016573\" href=\"vote?for=9016573&amp;dir=up&amp;whence=%6e%65%77%73\"><div class=\"votearrow\" title=\"upvote\"></div></a><span id=\"down_9016573\"></span></center></td><td class=\"title\"><span class=\"deadmark\"></span><a href=\"http://www.asianage.com/india/isro-launch-google-satellite-300\">ISRO to launch Google satellite</a><span class=\"sitebit comhead\"> (asianage.com)</span></td></tr><tr><td colspan=\"2\"></td><td class=\"subtext\"><span class=\"score\" id=\"score_9016573\">144 points</span> by <a href=\"user?id=unmole\">unmole</a> 11 hours ago  | <a href=\"item?id=9016573\">28 comments</a></td></tr><tr style=\"height:5px\"></tr><tr><td align=\"right\" valign=\"top\" class=\"title\"><span class=\"rank\">10.</span></td><td><center><a id=\"up_9017051\" href=\"vote?for=9017051&amp;dir=up&amp;whence=%6e%65%77%73\"><div class=\"votearrow\" title=\"upvote\"></div></a><span id=\"down_9017051\"></span></center></td><td class=\"title\"><span class=\"deadmark\"></span><a href=\"http://techcrunch.com/2015/02/08/google-odysee/\">Google Acquires Odysee</a><span class=\"sitebit comhead\"> (techcrunch.com)</span></td></tr><tr><td colspan=\"2\"></td><td class=\"subtext\"><span class=\"score\" id=\"score_9017051\">23 points</span> by <a href=\"user?id=enigami\">enigami</a> 5 hours ago  | <a href=\"item?id=9017051\">13 comments</a></td></tr><tr style=\"height:5px\"></tr><tr><td align=\"right\" valign=\"top\" class=\"title\"><span class=\"rank\">11.</span></td><td><center><a id=\"up_9016427\" href=\"vote?for=9016427&amp;dir=up&amp;whence=%6e%65%77%73\"><div class=\"votearrow\" title=\"upvote\"></div></a><span id=\"down_9016427\"></span></center></td><td class=\"title\"><span class=\"deadmark\"></span><a href=\"http://electronics.stackexchange.com/questions/152090/measuring-feline-capacitance\">Measuring Feline Capacitance</a><span class=\"sitebit comhead\"> (stackexchange.com)</span></td></tr><tr><td colspan=\"2\"></td><td class=\"subtext\"><span class=\"score\" id=\"score_9016427\">99 points</span> by <a href=\"user?id=mmastrac\">mmastrac</a> 13 hours ago  | <a href=\"item?id=9016427\">13 comments</a></td></tr><tr style=\"height:5px\"></tr><tr><td align=\"right\" valign=\"top\" class=\"title\"><span class=\"rank\">12.</span></td><td><center><a id=\"up_9017242\" href=\"vote?for=9017242&amp;dir=up&amp;whence=%6e%65%77%73\"><div class=\"votearrow\" title=\"upvote\"></div></a><span id=\"down_9017242\"></span></center></td><td class=\"title\"><span class=\"deadmark\"></span><a href=\"http://nautil.us/issue/21/information/for-preventing-disease-data-are-the-new-drugs-rp\">For Preventing Disease, Data Are the New Drugs</a><span class=\"sitebit comhead\"> (nautil.us)</span></td></tr><tr><td colspan=\"2\"></td><td class=\"subtext\"><span class=\"score\" id=\"score_9017242\">6 points</span> by <a href=\"user?id=dnetesn\">dnetesn</a> 3 hours ago  | <a href=\"item?id=9017242\">discuss</a></td></tr><tr style=\"height:5px\"></tr><tr><td align=\"right\" valign=\"top\" class=\"title\"><span class=\"rank\">13.</span></td><td><center><a id=\"up_9016669\" href=\"vote?for=9016669&amp;dir=up&amp;whence=%6e%65%77%73\"><div class=\"votearrow\" title=\"upvote\"></div></a><span id=\"down_9016669\"></span></center></td><td class=\"title\"><span class=\"deadmark\"></span><a href=\"http://www.gatesnotes.com/Books/Business-Adventures\">The Best Business Book I’ve Ever Read (2014)</a><span class=\"sitebit comhead\"> (gatesnotes.com)</span></td></tr><tr><td colspan=\"2\"></td><td class=\"subtext\"><span class=\"score\" id=\"score_9016669\">71 points</span> by <a href=\"user?id=denismars\">denismars</a> 10 hours ago  | <a href=\"item?id=9016669\">27 comments</a></td></tr><tr style=\"height:5px\"></tr><tr><td align=\"right\" valign=\"top\" class=\"title\"><span class=\"rank\">14.</span></td><td><center><a id=\"up_9015621\" href=\"vote?for=9015621&amp;dir=up&amp;whence=%6e%65%77%73\"><div class=\"votearrow\" title=\"upvote\"></div></a><span id=\"down_9015621\"></span></center></td><td class=\"title\"><span class=\"deadmark\"></span><a href=\"https://github.com/ryanss/vim-hackernews\">Show HN: vim-hackernews</a><span class=\"sitebit comhead\"> (github.com)</span></td></tr><tr><td colspan=\"2\"></td><td class=\"subtext\"><span class=\"score\" id=\"score_9015621\">171 points</span> by <a href=\"user?id=ryanss\">ryanss</a> 18 hours ago  | <a href=\"item?id=9015621\">32 comments</a></td></tr><tr style=\"height:5px\"></tr><tr><td align=\"right\" valign=\"top\" class=\"title\"><span class=\"rank\">15.</span></td><td><center><a id=\"up_9017037\" href=\"vote?for=9017037&amp;dir=up&amp;whence=%6e%65%77%73\"><div class=\"votearrow\" title=\"upvote\"></div></a><span id=\"down_9017037\"></span></center></td><td class=\"title\"><span class=\"deadmark\"></span><a href=\"http://www.huffingtonpost.com/johann-hari/the-real-cause-of-addicti_b_6506936.html\" rel=\"nofollow\">The Likely Cause of Addiction</a><span class=\"sitebit comhead\"> (huffingtonpost.com)</span></td></tr><tr><td colspan=\"2\"></td><td class=\"subtext\"><span class=\"score\" id=\"score_9017037\">6 points</span> by <a href=\"user?id=jwdunne\">jwdunne</a> 5 hours ago  | <a href=\"item?id=9017037\">2 comments</a></td></tr><tr style=\"height:5px\"></tr><tr><td align=\"right\" valign=\"top\" class=\"title\"><span class=\"rank\">16.</span></td><td><center><a id=\"up_9014020\" href=\"vote?for=9014020&amp;dir=up&amp;whence=%6e%65%77%73\"><div class=\"votearrow\" title=\"upvote\"></div></a><span id=\"down_9014020\"></span></center></td><td class=\"title\"><span class=\"deadmark\"></span><a href=\"http://www.washingtonpost.com/local/at-some-start-ups-fridays-are-so-casual-everyone-can-stay-home/2015/02/06/31e8407e-9d1c-11e4-96cc-e858eba91ced_story.html\">At some startups, Friday is so casual that it’s not even a workday</a><span class=\"sitebit comhead\"> (washingtonpost.com)</span></td></tr><tr><td colspan=\"2\"></td><td class=\"subtext\"><span class=\"score\" id=\"score_9014020\">347 points</span> by <a href=\"user?id=petethomas\">petethomas</a> 1 day ago  | <a href=\"item?id=9014020\">199 comments</a></td></tr><tr style=\"height:5px\"></tr><tr><td align=\"right\" valign=\"top\" class=\"title\"><span class=\"rank\">17.</span></td><td><center><a id=\"up_9017078\" href=\"vote?for=9017078&amp;dir=up&amp;whence=%6e%65%77%73\"><div class=\"votearrow\" title=\"upvote\"></div></a><span id=\"down_9017078\"></span></center></td><td class=\"title\"><span class=\"deadmark\"></span><a href=\"http://www.gwern.net/AB%20testing\" rel=\"nofollow\">A/B testing long-form readability</a><span class=\"sitebit comhead\"> (gwern.net)</span></td></tr><tr><td colspan=\"2\"></td><td class=\"subtext\"><span class=\"score\" id=\"score_9017078\">6 points</span> by <a href=\"user?id=luu\">luu</a> 5 hours ago  | <a href=\"item?id=9017078\">discuss</a></td></tr><tr style=\"height:5px\"></tr><tr><td align=\"right\" valign=\"top\" class=\"title\"><span class=\"rank\">18.</span></td><td><center><a id=\"up_9016949\" href=\"vote?for=9016949&amp;dir=up&amp;whence=%6e%65%77%73\"><div class=\"votearrow\" title=\"upvote\"></div></a><span id=\"down_9016949\"></span></center></td><td class=\"title\"><span class=\"deadmark\"></span><a href=\"http://www.audiovisualonline.co.uk/product/8041/audioquest-diamond-rj-e-ethernet-cable-12m\">AudioQuest Diamond RJ/E Ethernet Cable: £6899</a><span class=\"sitebit comhead\"> (audiovisualonline.co.uk)</span></td></tr><tr><td colspan=\"2\"></td><td class=\"subtext\"><span class=\"score\" id=\"score_9016949\">128 points</span> by <a href=\"user?id=jasoncartwright\">jasoncartwright</a> 6 hours ago  | <a href=\"item?id=9016949\">112 comments</a></td></tr><tr style=\"height:5px\"></tr><tr><td align=\"right\" valign=\"top\" class=\"title\"><span class=\"rank\">19.</span></td><td></td><td class=\"title\"><span class=\"deadmark\"></span><a href=\"http://myvr.com/careers/\" rel=\"nofollow\">MyVR (YC W12) Is Hiring Full Stack Engineers in SF</a><span class=\"sitebit comhead\"> (myvr.com)</span></td></tr><tr><td colspan=\"2\"></td><td class=\"subtext\">2 hours ago</td></tr><tr style=\"height:5px\"></tr><tr><td align=\"right\" valign=\"top\" class=\"title\"><span class=\"rank\">20.</span></td><td><center><a id=\"up_9016885\" href=\"vote?for=9016885&amp;dir=up&amp;whence=%6e%65%77%73\"><div class=\"votearrow\" title=\"upvote\"></div></a><span id=\"down_9016885\"></span></center></td><td class=\"title\"><span class=\"deadmark\"></span><a href=\"http://www.popularmechanics.com/technology/security/a13844/alex-holden-finds-your-passwords/\">Meet the Man Who Finds Your Stolen Passwords</a><span class=\"sitebit comhead\"> (popularmechanics.com)</span></td></tr><tr><td colspan=\"2\"></td><td class=\"subtext\"><span class=\"score\" id=\"score_9016885\">11 points</span> by <a href=\"user?id=sasvari\">sasvari</a> 7 hours ago  | <a href=\"item?id=9016885\">1 comment</a></td></tr><tr style=\"height:5px\"></tr><tr><td align=\"right\" valign=\"top\" class=\"title\"><span class=\"rank\">21.</span></td><td><center><a id=\"up_9015092\" href=\"vote?for=9015092&amp;dir=up&amp;whence=%6e%65%77%73\"><div class=\"votearrow\" title=\"upvote\"></div></a><span id=\"down_9015092\"></span></center></td><td class=\"title\"><span class=\"deadmark\"></span><a href=\"https://libcom.org/files/Bertrand%20Russell%20-%20In%20Praise%20of%20Idleness.pdf\">In Praise of Idleness by Bertrand Russell (1932) [pdf]</a><span class=\"sitebit comhead\"> (libcom.org)</span></td></tr><tr><td colspan=\"2\"></td><td class=\"subtext\"><span class=\"score\" id=\"score_9015092\">134 points</span> by <a href=\"user?id=jacobsimon\">jacobsimon</a> 21 hours ago  | <a href=\"item?id=9015092\">40 comments</a></td></tr><tr style=\"height:5px\"></tr><tr><td align=\"right\" valign=\"top\" class=\"title\"><span class=\"rank\">22.</span></td><td><center><a id=\"up_9014890\" href=\"vote?for=9014890&amp;dir=up&amp;whence=%6e%65%77%73\"><div class=\"votearrow\" title=\"upvote\"></div></a><span id=\"down_9014890\"></span></center></td><td class=\"title\"><span class=\"deadmark\"></span><a href=\"https://dev.opera.com/articles/perfect-javascript-framework/\">In search of the perfect JavaScript framework</a><span class=\"sitebit comhead\"> (opera.com)</span></td></tr><tr><td colspan=\"2\"></td><td class=\"subtext\"><span class=\"score\" id=\"score_9014890\">172 points</span> by <a href=\"user?id=jsargiox\">jsargiox</a> 22 hours ago  | <a href=\"item?id=9014890\">70 comments</a></td></tr><tr style=\"height:5px\"></tr><tr><td align=\"right\" valign=\"top\" class=\"title\"><span class=\"rank\">23.</span></td><td><center><a id=\"up_9015508\" href=\"vote?for=9015508&amp;dir=up&amp;whence=%6e%65%77%73\"><div class=\"votearrow\" title=\"upvote\"></div></a><span id=\"down_9015508\"></span></center></td><td class=\"title\"><span class=\"deadmark\"></span><a href=\"http://aviationweek.com/defense/ground-collision-avoidance-system-saves-first-f-16-syria\">Ground Collision Avoidance System ‘Saves’ First F-16 in Syria</a><span class=\"sitebit comhead\"> (aviationweek.com)</span></td></tr><tr><td colspan=\"2\"></td><td class=\"subtext\"><span class=\"score\" id=\"score_9015508\">123 points</span> by <a href=\"user?id=aerocapture\">aerocapture</a> 19 hours ago  | <a href=\"item?id=9015508\">52 comments</a></td></tr><tr style=\"height:5px\"></tr><tr><td align=\"right\" valign=\"top\" class=\"title\"><span class=\"rank\">24.</span></td><td><center><a id=\"up_9016887\" href=\"vote?for=9016887&amp;dir=up&amp;whence=%6e%65%77%73\"><div class=\"votearrow\" title=\"upvote\"></div></a><span id=\"down_9016887\"></span></center></td><td class=\"title\"><span class=\"deadmark\"></span><a href=\"http://csel.eng.ohio-state.edu/courses/ISE694_SP2010/docs/WOODS_2008_StressStrainPlotsAssessingResilience.pdf\" rel=\"nofollow\">Stress-Strain Plots as a Basis for Assessing System Resilience (2008) [pdf]</a><span class=\"sitebit comhead\"> (ohio-state.edu)</span></td></tr><tr><td colspan=\"2\"></td><td class=\"subtext\"><span class=\"score\" id=\"score_9016887\">7 points</span> by <a href=\"user?id=wallflower\">wallflower</a> 7 hours ago  | <a href=\"item?id=9016887\">discuss</a></td></tr><tr style=\"height:5px\"></tr><tr><td align=\"right\" valign=\"top\" class=\"title\"><span class=\"rank\">25.</span></td><td><center><a id=\"up_9017307\" href=\"vote?for=9017307&amp;dir=up&amp;whence=%6e%65%77%73\"><div class=\"votearrow\" title=\"upvote\"></div></a><span id=\"down_9017307\"></span></center></td><td class=\"title\"><span class=\"deadmark\"></span><a href=\"http://m.bbc.co.uk/news/uk-31188255\" rel=\"nofollow\">Lost typeface printing blocks found in river Thames</a><span class=\"sitebit comhead\"> (bbc.co.uk)</span></td></tr><tr><td colspan=\"2\"></td><td class=\"subtext\"><span class=\"score\" id=\"score_9017307\">3 points</span> by <a href=\"user?id=sjclemmy\">sjclemmy</a> 3 hours ago  | <a href=\"item?id=9017307\">discuss</a></td></tr><tr style=\"height:5px\"></tr><tr><td align=\"right\" valign=\"top\" class=\"title\"><span class=\"rank\">26.</span></td><td><center><a id=\"up_9017769\" href=\"vote?for=9017769&amp;dir=up&amp;whence=%6e%65%77%73\"><div class=\"votearrow\" title=\"upvote\"></div></a><span id=\"down_9017769\"></span></center></td><td class=\"title\"><span class=\"deadmark\"></span><a href=\"http://www.theguardian.com/technology/2014/may/14/firefox-closed-source-drm-video-browser-cory-doctorow\" rel=\"nofollow\">Firefox’s adoption of closed-source DRM breaks my heart</a><span class=\"sitebit comhead\"> (theguardian.com)</span></td></tr><tr><td colspan=\"2\"></td><td class=\"subtext\"><span class=\"score\" id=\"score_9017769\">5 points</span> by <a href=\"user?id=jarsin\">jarsin</a> 21 minutes ago  | <a href=\"item?id=9017769\">discuss</a></td></tr><tr style=\"height:5px\"></tr><tr><td align=\"right\" valign=\"top\" class=\"title\"><span class=\"rank\">27.</span></td><td><center><a id=\"up_9016270\" href=\"vote?for=9016270&amp;dir=up&amp;whence=%6e%65%77%73\"><div class=\"votearrow\" title=\"upvote\"></div></a><span id=\"down_9016270\"></span></center></td><td class=\"title\"><span class=\"deadmark\"></span><a href=\"https://www.scene.org/\">Demoscene news and downloads</a><span class=\"sitebit comhead\"> (scene.org)</span></td></tr><tr><td colspan=\"2\"></td><td class=\"subtext\"><span class=\"score\" id=\"score_9016270\">58 points</span> by <a href=\"user?id=tonteldoos\">tonteldoos</a> 14 hours ago  | <a href=\"item?id=9016270\">8 comments</a></td></tr><tr style=\"height:5px\"></tr><tr><td align=\"right\" valign=\"top\" class=\"title\"><span class=\"rank\">28.</span></td><td><center><a id=\"up_9016627\" href=\"vote?for=9016627&amp;dir=up&amp;whence=%6e%65%77%73\"><div class=\"votearrow\" title=\"upvote\"></div></a><span id=\"down_9016627\"></span></center></td><td class=\"title\"><span class=\"deadmark\"></span><a href=\"http://enterprisemac.bruienne.com/2015/02/07/box-cutting-how-i-stumbled-onto-a-serious-security-flaw-in-box-sync-for-mac/\">How I stumbled onto a security flaw in Box Sync for Mac</a><span class=\"sitebit comhead\"> (bruienne.com)</span></td></tr><tr><td colspan=\"2\"></td><td class=\"subtext\"><span class=\"score\" id=\"score_9016627\">33 points</span> by <a href=\"user?id=zdw\">zdw</a> 10 hours ago  | <a href=\"item?id=9016627\">8 comments</a></td></tr><tr style=\"height:5px\"></tr><tr><td align=\"right\" valign=\"top\" class=\"title\"><span class=\"rank\">29.</span></td><td><center><a id=\"up_9015613\" href=\"vote?for=9015613&amp;dir=up&amp;whence=%6e%65%77%73\"><div class=\"votearrow\" title=\"upvote\"></div></a><span id=\"down_9015613\"></span></center></td><td class=\"title\"><span class=\"deadmark\"></span><a href=\"https://github.com/6to5/6to5/pull/714\">Tail-call optimization added to 6to5 compiler</a><span class=\"sitebit comhead\"> (github.com)</span></td></tr><tr><td colspan=\"2\"></td><td class=\"subtext\"><span class=\"score\" id=\"score_9015613\">106 points</span> by <a href=\"user?id=insertion\">insertion</a> 18 hours ago  | <a href=\"item?id=9015613\">22 comments</a></td></tr><tr style=\"height:5px\"></tr><tr><td align=\"right\" valign=\"top\" class=\"title\"><span class=\"rank\">30.</span></td><td><center><a id=\"up_9015325\" href=\"vote?for=9015325&amp;dir=up&amp;whence=%6e%65%77%73\"><div class=\"votearrow\" title=\"upvote\"></div></a><span id=\"down_9015325\"></span></center></td><td class=\"title\"><span class=\"deadmark\"></span><a href=\"http://www.collectspace.com/news/news-020615b-neil-armstrong-artifacts-purse.html\">Neil Armstrong's purse: First moonwalker had hidden bag of Apollo 11 artifacts</a><span class=\"sitebit comhead\"> (collectspace.com)</span></td></tr><tr><td colspan=\"2\"></td><td class=\"subtext\"><span class=\"score\" id=\"score_9015325\">88 points</span> by <a href=\"user?id=antr\">antr</a> 20 hours ago  | <a href=\"item?id=9015325\">11 comments</a></td></tr><tr style=\"height:5px\"></tr><tr style=\"height:10px\"></tr><tr><td colspan=\"2\"></td><td class=\"title\"><a href=\"news?p=2\" rel=\"nofollow\">More</a></td></tr></table></td></tr><tr><td><img src=\"s.gif\" height=\"10\" width=\"0\"><table width=\"100%\" cellspacing=\"0\" cellpadding=\"1\"><tr><td bgcolor=\"#ff6600\"></td></tr></table><br>\n" +
            "<center><span class=\"yclinks\"><a href=\"newsguidelines.html\">Guidelines</a> | <a href=\"newsfaq.html\">FAQ</a> | <a href=\"mailto:hn@ycombinator.com\">Support</a> | <a href=\"lists\">Lists</a> | <a href=\"bookmarklet.html\">Bookmarklet</a> | <a href=\"dmca.html\">DMCA</a> | <a href=\"http://www.ycombinator.com/\">Y Combinator</a> | <a href=\"http://www.ycombinator.com/apply/\">Apply</a> | <a href=\"http://www.ycombinator.com/contact/\">Contact</a></span><br><br>\n" +
            "<form method=\"get\" action=\"//hn.algolia.com/\">Search: <input type=\"text\" name=\"q\" value=\"\" size=\"17\"></form></center></td></tr></table></center></body></html>\n";

    private static final String NEWS_STORY_SUBLINE = "30 points by lawl 39 minutes ago | 2 comments";
    private static final String JOB_STORY_SUBLINE = "39 minutes ago";

    private static final String DEFAULT_SUBMITTER = "lawl";
    private static final String EMPTY_SUBMITTER = "";

    private static final String DEFAULT_TIME_AGO = "39 minutes ago";

    private static final String BASE_URI = "https://news.ycombinator.com/news";
    private static final Story.TYPE DEFAULT_TYPE = Story.TYPE.top_story;

    StoriesParser storiesParser;

    @Before
    public void setUp() throws Exception {
        Document doc = Jsoup.parse(news, BASE_URI);
        storiesParser = new StoriesParser(doc);
    }

    @org.junit.Test
    public void returnTimeWhenParsingNewsStory() {
        String timeAgo = StoriesParser.parseAgo(NEWS_STORY_SUBLINE, DEFAULT_SUBMITTER);
        assertEquals(timeAgo, DEFAULT_TIME_AGO);
    }

    @org.junit.Test
    public void returnTimeWhenParsingJobStory() {
        String timeAgo = StoriesParser.parseAgo(JOB_STORY_SUBLINE, EMPTY_SUBMITTER);
        assertEquals(timeAgo, JOB_STORY_SUBLINE);
    }

    @org.junit.Test
    public void returnAllStories() {
        Vector<ContentValues> stories = storiesParser.parse(DEFAULT_TYPE);
        assertEquals(stories.size(), 30);
    }
}