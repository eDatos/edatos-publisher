package es.gobcan.istac.edatos.publisher.core.util.markdown;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;

public class MarkdownCkan {

    public static final String BOLD_TAG       = "b";
    public static final String ITALIC_TAG     = "i";
    public static final String UNDERLINED_TAG = "u";

    public static String stringHtmlToMarkdown(String source) {
        if (source == null || source.isEmpty()) {
            return source;
        }
        String cleanedSource = cleanHtmlTags(source);

        return FlexmarkHtmlConverter.builder().build().convert(cleanedSource);
    }

    private static String cleanHtmlTags(String source) {
        Document doc = Jsoup.parse(source);

        cleanUnderlineHtmlTag(doc);
        cleanEmptyLineHtmlTag(doc, BOLD_TAG);
        cleanEmptyLineHtmlTag(doc, ITALIC_TAG);

        return doc.html();

    }

    // ckan does not support underline and flexmark converts html underline to ++some text++
    private static void cleanUnderlineHtmlTag(Document doc) {
        Elements selector = doc.select(UNDERLINED_TAG);

        for (Element element : selector) {
            element.unwrap();
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
    private static void cleanEmptyLineHtmlTag(Document doc, String tag) {
        Elements selector = doc.select(tag);

        for (Element element : selector) {
            if (!existTextInHtmlTag(element)) {
                element.unwrap();
            }
        }
    }

    private static boolean existTextInHtmlTag(Element htmlTag) {
        return htmlTag != null && !htmlTag.text().isEmpty();
    }
}
