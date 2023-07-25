package es.gobcan.istac.edatos.publisher.core.util.markdown;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;

public class MarkdownCkan {

    private static final Whitelist WHITELIST           = Whitelist.none().addTags(new String[]{"br"});

    public static final String     LINE_BREAK          = "<BR />";
    public static final String     UNDERLINE_START_TAG = "<u>";
    public static final String     UNDERLINE_END_TAG   = "</u>";

    public static String stringHtmlToMarkdown(String source) {
        if (source == null || source.isEmpty()) {
            return source;
        }
        String cleanedSource = cleanHtmlTags(source);

        return FlexmarkHtmlConverter.builder().build().convert(cleanedSource);
    }

    private static String cleanHtmlTags(String source) {

        String target = cleanUnderlineHtmlTag(source);
        target = htmlFormatToMarkDownCkan(target, new BoldFormatTag());
        target = htmlFormatToMarkDownCkan(target, new ItalicFormatTag());

        return target;

    }

    // ckan does not support underline and flexmark converts html underline to ++some text++
    private static String cleanUnderlineHtmlTag(String source) {
        return source.replace(UNDERLINE_START_TAG, "").replace(UNDERLINE_END_TAG, "");
    }

    // if text is empty remove innecessary format html tag like <b/</b> and <i></i>
    private static void htmlFormatEmptyTextToMarkDownCkan(String textElement, Elements htmlElement, FormatTag formatTag, StringBuilder markDownText) {
        if (htmlElement != null && !htmlElement.isEmpty() && !htmlElement.text().isEmpty()) {
            markDownText.append(textElement);
        } else if (textElement.toUpperCase().indexOf(LINE_BREAK) > -1) {
            String labelTarget = Jsoup.clean(textElement, WHITELIST);
            labelTarget = StringEscapeUtils.unescapeHtml(labelTarget);
            markDownText.append(labelTarget);
        }
    }

    /*
     * Some html is not recognised by converter tools to markdown.
     * Specifically, in eDatos some RichTextEditor put this type of html:
     * <b> some bold text</b><b><br /></b>some another bold text<br />
     * <i> some italic text </i><i><br /></i>some italic text<br />
     * tools remove the empty line that are marked with format like <b><br /></b> or <i><br /></i>
     * this behaviour converts the previous text like this:
     * ** some bold text**some another bold text\n\n
     * *some italic text * some italic text\n\n
     * where the break line between texts disappears.
     * This function remove previously the empty format text to avoid this behaviour.
     * The output text will be:
     * <b> some bold text</b><br />some another bold text<br />
     * <i> some italic text </i><br />some italic text<br />
     */
    private static String htmlFormatToMarkDownCkan(String source, FormatTag formatTag) {
        boolean pending = true;
        StringBuilder markDownText = new StringBuilder();

        while (pending) {

            int pos = source.indexOf(formatTag.getStartTag());
            int posEnd = source.indexOf(formatTag.getEndTag());

            if (pos > -1 && posEnd > -1) {

                markDownText.append(source.substring(0, pos));

                String textElement = source.substring(pos, posEnd + formatTag.getEndTag().length());
                Document doc = Jsoup.parse(textElement);
                Elements matchElements = doc.select(formatTag.getTag());

                htmlFormatEmptyTextToMarkDownCkan(textElement, matchElements, formatTag, markDownText);

                source = source.substring(posEnd + formatTag.getEndTag().length(), source.length());

            } else {
                pending = false;
            }
        }
        markDownText.append(source);
        return markDownText.toString();
    }
}
