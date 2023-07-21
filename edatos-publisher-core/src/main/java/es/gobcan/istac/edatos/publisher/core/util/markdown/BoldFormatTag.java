package es.gobcan.istac.edatos.publisher.core.util.markdown;

public class BoldFormatTag extends FormatTag {

    public static final String BOLD_START_TAG = "<b>";
    public static final String BOLD_END_TAG   = "</b>";
    public static final String TAG            = "b";

    BoldFormatTag() {
        setTag(TAG);
        setStartTag(BOLD_START_TAG);
        setEndTag(BOLD_END_TAG);
        setMarkdownText("**");
    }
}
