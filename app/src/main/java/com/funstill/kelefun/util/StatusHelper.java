package com.funstill.kelefun.util;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author mcxiaoke
 * @version 2.0 2012.03.18
 */
public class StatusHelper {

    private static final Pattern PATTERN_HIGHLIGHT = Pattern.compile("<b>(\\w+?)</b>");
    private static final Pattern PATTERN_USER = Pattern.compile("(@.+?)\\s+", Pattern.MULTILINE);
    private static final String SCHEME_USER = "fanfouapp://profile/";
    private static final Pattern PATTERN_SEARCH = Pattern.compile("#\\w+#");
    private static final String SCHEME_SEARCH = "fanfouapp://search/";
    private static Pattern PATTERN_USER_LINK = Pattern
            .compile("<a href=\"http://fanfou\\.com/(.*?)\" class=\"former\">(.*?)</a>");

    public static void linkifyUsers(final Spannable spannable, final HashMap<String, String> mentions) {
        final Linkify.MatchFilter filter = new Linkify.MatchFilter() {
            @Override
            public final boolean acceptMatch(final CharSequence s, final int start,
                                             final int end) {
                String name = s.subSequence(start + 1, end).toString().trim();
                return mentions.containsKey(name);
            }
        };
        final Linkify.TransformFilter transformer = new Linkify.TransformFilter() {

            @Override
            public String transformUrl(Matcher match, String url) {
                String name = url.subSequence(1, url.length()).toString().trim();
                return mentions.get(name);
            }
        };
        Linkify.addLinks(spannable, PATTERN_USER, SCHEME_USER, filter,
                transformer);
    }

    private static List<String> findHighlightWords(final String htmlText) {
        final Matcher m = PATTERN_HIGHLIGHT.matcher(htmlText);
        List<String> words = new ArrayList<>();
        while (m.find()) {
            final String word = m.group(1);
            words.add(word);
        }
        return words;
    }

    private static HashMap<String, String> findMentions(final String htmlText) {
        final HashMap<String, String> map = new HashMap<String, String>();
        final Matcher m = PATTERN_USER_LINK.matcher(htmlText);
        while (m.find()) {
            final String userId = m.group(1);
            final String screenName = Html.fromHtml(m.group(2)).toString();
            map.put(screenName, userId);
        }
        return map;
    }
//TODO 消息链接展示 重点
    public static void setStatus(final TextView textView, final String text) {
        final String htmlText = text + " ";
//        LogUtil.v(TAG, "setStatus:htmlText:" + htmlText);
        final HashMap<String, String> mentions = findMentions(htmlText);
//        LogUtil.v(TAG, "setStatus:mentions:" + mentions);
        final String plainText = Html.fromHtml(htmlText).toString();
//        LogUtil.v(TAG, "setStatus:plainText:" + plainText);
        final SpannableString spannable = new SpannableString(plainText);
        Linkify.addLinks(spannable, Linkify.WEB_URLS);
        linkifyUsers(spannable, mentions);
//        removeUnderLines(spannable);
//        LogUtil.v(TAG, "setStatus:finalText:" + spannable);
        textView.setText(spannable, BufferType.SPANNABLE);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }



}
