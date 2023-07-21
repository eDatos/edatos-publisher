package es.gobcan.istac.edatos.publisher.core.util.markdown;

public class LinkFormatTag extends FormatTag {

    public static final String LINK_START_TAG = "<a";
    public static final String LINK_END_TAG   = "</a>";
    public static final String TAG            = "a";

    LinkFormatTag() {
        setTag(TAG);
        setStartTag(LINK_START_TAG);
        setEndTag(LINK_END_TAG);
        setMarkdownText(null);
    }
}
