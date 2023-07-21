package es.gobcan.istac.edatos.publisher.core.util.markdown;

public class ItalicFormatTag extends FormatTag {

    public static final String ITALIC_START_TAG = "<i>";
    public static final String ITALIC_END_TAG   = "</i>";
    public static final String TAG              = "i";

    ItalicFormatTag() {
        setTag(TAG);
        setStartTag(ITALIC_START_TAG);
        setEndTag(ITALIC_END_TAG);
        setMarkdownText("*");
    }
}
