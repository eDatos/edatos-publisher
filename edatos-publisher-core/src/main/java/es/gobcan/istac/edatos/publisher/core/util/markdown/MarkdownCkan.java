package es.gobcan.istac.edatos.publisher.core.util.markdown;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

public class MarkdownCkan {

    private static final Whitelist WHITELIST       = Whitelist.none().addTags(new String[]{"br", "a", "b", "i"}).addAttributes("a", "href");
    public static final String     BR_TAG          = "<br />";
    public static final String     DIV_TAG         = "<div>";
    public static final String     DIV_END_TAG     = "</div>";
    public static final String     LINE_BREAK_CKAN = "\n\n";
    public static final String     LINE_BREAK      = "\n";
    public static final String     LINK_END_TAG    = "</a>";

    public static String stringHtmlToTextPlain(String source) {
        if (source == null) {
            return source;
        }

        String labelTarget = Jsoup.clean(source, WHITELIST);
        labelTarget = StringEscapeUtils.unescapeHtml(labelTarget);
        return htmlToMarkdownCkan(labelTarget);
    }

    private static String htmlToMarkdownCkan(String source) {
        String target = htmlLineBreakToMarkdownCkan(source);

        target = htmlFormatToMarkDownCkan(target, new LinkFormatTag());
        target = htmlFormatToMarkDownCkan(target, new BoldFormatTag());
        target = htmlFormatToMarkDownCkan(target, new ItalicFormatTag());

        return target;

    }

    /*
     * break line in ckan are with double break line. Because of this a double \n character must be send
     * this method transform <br /> and <div> tags in \n\n.
     * Remove </div> tag and additional \n tags.
     */
    private static String htmlLineBreakToMarkdownCkan(String source) {
        String target = source.replace(LINE_BREAK, "");
        return target.replace(BR_TAG, LINE_BREAK_CKAN).replace(DIV_END_TAG, "").replace(DIV_TAG, LINE_BREAK_CKAN);
    }

    /*
     * links in ckan are like -> [text](link) Example: [visible text for user](https://url.org)
     * this method receive an html link like <a href="https://url.org">visible text for user</a> and is transformed to ckan markdown format.
     */
    private static void htmlLinkToMarkDownCkan(Elements htmlElement, StringBuilder markDownText) {
        if (htmlElement != null && !htmlElement.isEmpty()) {
            Element link = htmlElement.first();
            markDownText.append("[" + link.text() + "]" + "(" + link.attr("abs:href") + ")");
        }
    }

    /*
     * transform bold an italic text to ckan markdown
     * bold text in ckan like this: **text**
     * italic text in ckan like this *text*
     * Problem: markdown without text is not recognised. Example: "**\n\n** in ckan is visible like **  ** because of this the markdown "**" must not be send in these cases.
     * this method transform html like this:
     * - <b>text</b> in **text**
     * - <i>text</i> in *text*
     */
    private static void htmlFormatTextToMarkDownCkan(String textElement, Elements htmlElement, FormatTag formatTag, StringBuilder markDownText) {
        if (htmlElement != null && !htmlElement.isEmpty() && !htmlElement.text().isEmpty()) {
            markDownText.append(formatTag.getMarkdownText() + htmlElement.text() + formatTag.getMarkdownText());
        } else {
            if (textElement.indexOf(LINE_BREAK) > -1) {
                textElement = textElement.substring(formatTag.getStartTag().length(), textElement.length());
                markDownText.append(textElement);
            }
        }
    }

    private static String htmlFormatToMarkDownCkan(String source, FormatTag formatTag) {
        boolean pending = true;
        StringBuilder markDownText = new StringBuilder();

        while (pending) {

            int pos = source.indexOf(formatTag.getStartTag());
            int posEnd = source.indexOf(formatTag.getEndTag());

            if (pos > -1 && posEnd > -1) {

                markDownText.append(source.substring(0, pos));

                String textElement = source.substring(pos, posEnd);
                Document doc = Jsoup.parse(textElement);
                Elements matchElements = doc.select(formatTag.getTag());

                if (formatTag instanceof LinkFormatTag) {
                    htmlLinkToMarkDownCkan(matchElements, markDownText);
                } else {
                    htmlFormatTextToMarkDownCkan(textElement, matchElements, formatTag, markDownText);
                }

                source = source.substring(posEnd + formatTag.getEndTag().length(), source.length());

            } else {
                pending = false;
            }
        }
        markDownText.append(source);
        return markDownText.toString();
    }
}
