package com.malmstein.yahnac.views.fonts;

import android.content.Context;
import android.content.res.Resources;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;

import com.malmstein.yahnac.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommentTextView extends YahnacTextView {

    public static final Pattern PATTERN_HASHTAGS = Pattern.compile("\\B(#\\w{2,})");
    public static final Pattern PATTERN_USER_HANDLES = Pattern.compile("\\B(@\\w{2,})");
    public static final Pattern PATTERN_URLS =
            Pattern.compile("\\b((?:(?:https?|ftp)://)[\\w\\-+&@#/%=~_|$?!:,.]*[\\w+&@#/%=~_|\\$])\\b");

    private int mHighlightColor, mUrlHighlightColor;

    public CommentTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        if (isInEditMode()) {
            // PREVIEW-ONLY hardcoded values (for in-IDE usage)
            mHighlightColor = 0xff4d90fe;
            mUrlHighlightColor = 0xff000000;
        } else {
            final Resources resources = getResources();
            mHighlightColor = resources.getColor(R.color.dark_orange);
            mUrlHighlightColor = resources.getColor(R.color.dark_orange);
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        // We better use a copy of the text, to avoid messing up with its spans (if any)
        Spannable textSpannable = Spannable.Factory.getInstance().newSpannable(text);
        textSpannable = highlightHashtags(textSpannable);
        textSpannable = highlightUserHandles(textSpannable);
        textSpannable = highlightUrls(textSpannable);

        super.setText(textSpannable, BufferType.SPANNABLE);
    }

    /**
     * Highlights all the hashtag in the passed text.
     *
     * @param text The text to highlight the hashtags within.
     *             Must have already been "cleaned up" from spans.
     */
    private Spannable highlightHashtags(Spannable text) {
        if (text == null) {
            return null;
        }

        // Note that this assumes the View's text has already been cleaned up
        // and that the text passed along is already a working copy
        // (see TextView#setText() javadocs...)
        final Matcher matcher = PATTERN_HASHTAGS.matcher(text);

        while (matcher.find()) {
            final int start = matcher.start(1);
            final int end = matcher.end(1);

            // We have to create a new span for each token we want to highlight
            // or it will just be moved around. No optimization possible?
            text.setSpan(new ForegroundColorSpan(mHighlightColor), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            text.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return text;
    }

    /**
     * Highlights all the user handles in the passed text.
     *
     * @param text The text to highlight the user handles within.
     *             Must have already been "cleaned up" from spans.
     */
    private Spannable highlightUserHandles(Spannable text) {
        if (text == null) {
            return null;
        }

        // Note that this assumes the View's text has already been cleaned up
        // and that the text passed along is already a working copy
        // (see TextView#setText() javadocs...)
        final Matcher matcher = PATTERN_USER_HANDLES.matcher(text);

        while (matcher.find()) {
            final int start = matcher.start(1);
            final int end = matcher.end(1);

            // We have to create a new span for each token we want to highlight
            // or it will just be moved around. No optimization possible?
            text.setSpan(new ForegroundColorSpan(mHighlightColor), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            text.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return text;
    }

    /**
     * Highlights all the URLs in the passed text.
     *
     * @param text The text to highlight the URLs within.
     *             Must have already been "cleaned up" from spans.
     */
    private Spannable highlightUrls(Spannable text) {
        if (text == null) {
            return null;
        }

        // Note that this assumes the View's text has already been cleaned up
        // and that the text passed along is already a working copy
        // (see TextView#setText() javadocs...)
        final Matcher matcher = PATTERN_URLS.matcher(text);

        while (matcher.find()) {
            final int start = matcher.start(1);
            final int end = matcher.end(1);

            // We have to create a new span for each token we want to highlight
            // or it will just be moved around. No optimization possible?
            text.setSpan(new ForegroundColorSpan(mUrlHighlightColor), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return text;
    }
}
