package es.gobcan.istac.edatos.publisher.core.util.markdown;

public class FormatTag {

    private String tag;
    private String startTag;
    private String endTag;
    private String markdownText;

    public String getTag() {
        return tag;
    }
    protected void setTag(String tag) {
        this.tag = tag;
    }
    public String getStartTag() {
        return startTag;
    }
    protected void setStartTag(String startTag) {
        this.startTag = startTag;
    }
    public String getEndTag() {
        return endTag;
    }
    protected void setEndTag(String endTag) {
        this.endTag = endTag;
    }
    public String getMarkdownText() {
        return markdownText;
    }
    protected void setMarkdownText(String markdownText) {
        this.markdownText = markdownText;
    }

}
