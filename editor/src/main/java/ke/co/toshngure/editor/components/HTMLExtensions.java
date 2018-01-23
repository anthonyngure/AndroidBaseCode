package ke.co.toshngure.editor.components;

import android.widget.TableLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import ke.co.toshngure.editor.EditorCore;
import ke.co.toshngure.editor.models.EditorContent;
import ke.co.toshngure.editor.models.EditorTextStyle;
import ke.co.toshngure.editor.models.EditorType;
import ke.co.toshngure.editor.models.HtmlTag;
import ke.co.toshngure.editor.models.Node;

/**
 * Created by mkallingal on 5/25/2016.
 */
public class HTMLExtensions {
    EditorCore editorCore;

    public HTMLExtensions(EditorCore editorCore) {
        this.editorCore = editorCore;
    }

    private static boolean matchesTag(String test) {
        for (HtmlTag tag : HtmlTag.values()) {
            if (tag.name().equals(test)) {
                return true;
            }
        }
        return false;
    }

    public void parseHtml(String htmlString) {
        Document doc = Jsoup.parse(htmlString);
        for (Element element : doc.body().children()) {
            if (!matchesTag(element.tagName().toLowerCase()))
                continue;
            buildNode(element);
        }
    }

    private void buildNode(Element element) {
        String text;
        HtmlTag tag = HtmlTag.valueOf(element.tagName().toLowerCase());
        int count = editorCore.getParentView().getChildCount();
        if ("<br>".equals(element.html().replaceAll("\\s+", "")) || "<br/>".equals(element.html().replaceAll("\\s+", ""))) {
            editorCore.getInputExtensions().insertEditText(count, null, null);
            return;
        } else if ("<hr>".equals(element.html().replaceAll("\\s+", "")) || "<hr/>".equals(element.html().replaceAll("\\s+", ""))) {
            editorCore.getDividerExtensions().insertDivider();
            return;
        }
        switch (tag) {
            case p:
                text = element.html();
                editorCore.getInputExtensions().insertEditText(count, null, text);
                break;
            case ul:
            case ol:
                RenderList(tag == HtmlTag.ol, element);
                break;
            case div:
                renderDiv(element);
        }
    }

    private void renderDiv(Element element) {
        String tag = element.attr("data-tag");
    }

    private void RenderList(boolean isOrdered, Element element) {
        if (element.children().size() > 0) {
            Element li = element.child(0);
            String text = getHtmlSpan(li);
            TableLayout layout = editorCore.getListItemExtensions().insertList(editorCore.getParentChildCount(), isOrdered, text);
            for (int i = 1; i < element.children().size(); i++) {
                li = element.child(i);
                text = getHtmlSpan(li);
                editorCore.getListItemExtensions().AddListItem(layout, isOrdered, text);
            }
        }
    }

    private String getHtmlSpan(Element element) {
        Element el = new Element(Tag.valueOf("span"), "");
        el.attributes().put("style", element.attr("style"));
        el.html(element.html());
        return el.toString();
    }

    private boolean hasChildren(Element element) {
        return element.getAllElements().size() > 0;
    }

    private String getTemplateHtml(EditorType child) {
        String template = null;
        switch (child) {
            case INPUT:
                template = "<{{$tag}} data-tag=\"input\" {{$style}}>{{$content}}</{{$tag}}>";
                break;
            case hr:
                template = "<hr data-tag=\"hr\"/>";
                break;
            case ol:
                template = "<ol data-tag=\"ol\">{{$content}}</ol>";
                break;
            case ul:
                template = "<ul data-tag=\"ul\">{{$content}}</ul>";
                break;
            case OL_LI:
            case UL_LI:
                template = "<li>{{$content}}</li>";
                break;
        }
        return template;
    }

    private String getInputHtml(Node item) {
        boolean isParagraph = true;
        String tmpl = getTemplateHtml(EditorType.INPUT);
        //  CharSequence content= android.text.Html.fromHtml(item.content.get(0)).toString();
        //  CharSequence trimmed= editorCore.getInputExtensions().noTrailingwhiteLines(content);
        String trimmed = Jsoup.parse(item.content.get(0)).body().select("p").html();
        if (item.contentStyles.size() > 0) {
            for (EditorTextStyle style : item.contentStyles) {
                switch (style) {
                    case BOLD:
                        tmpl = tmpl.replace("{{$content}}", "<b>{{$content}}</b>");
                        break;
                    case BOLDITALIC:
                        tmpl = tmpl.replace("{{$content}}", "<b><i>{{$content}}</i></b>");
                        break;
                    case ITALIC:
                        tmpl = tmpl.replace("{{$content}}", "<i>{{$content}}</i>");
                        break;
                    case INDENT:
                        tmpl = tmpl.replace("{{$style}}", "style=\"margin-left:25px\"");
                        break;
                    case OUTDENT:
                        tmpl = tmpl.replace("{{$style}}", "style=\"margin-left:0px\"");
                        break;
                    case NORMAL:
                        tmpl = tmpl.replace("{{$tag}}", "p");
                        isParagraph = true;
                        break;
                }
            }
            if (isParagraph) {
                tmpl = tmpl.replace("{{$tag}}", "p");
            }
            tmpl = tmpl.replace("{{$content}}", trimmed);
            tmpl = tmpl.replace("{{$style}}", "");
            return tmpl;
        }
        tmpl = tmpl.replace("{{$tag}}", "p");
        tmpl = tmpl.replace("{{$content}}", trimmed);
        tmpl = tmpl.replace(" {{$style}}", "");
        return tmpl;
    }

    public String getContentAsHTML() {
        StringBuilder htmlBlock = new StringBuilder();
        String html;
        EditorContent content = editorCore.getContent();
        return getContentAsHTML(content);
    }

    public String getContentAsHTML(EditorContent content) {
        StringBuilder htmlBlock = new StringBuilder();
        String html;
        for (Node item : content.nodes) {
            switch (item.type) {
                case INPUT:
                    html = getInputHtml(item);
                    htmlBlock.append(html);
                    break;
                case hr:
                    htmlBlock.append(getTemplateHtml(item.type));
                    break;
                case ul:
                case ol:
                    htmlBlock.append(getListAsHtml(item));
                    break;
            }
        }
        return htmlBlock.toString();
    }


    private String getListAsHtml(Node item) {
        int count = item.content.size();
        String tmpl_parent = getTemplateHtml(item.type);
        StringBuilder childBlock = new StringBuilder();
        for (int i = 0; i < count; i++) {
            String tmpl_li = getTemplateHtml(item.type == EditorType.ul ? EditorType.UL_LI : EditorType.OL_LI);
            String trimmed = Jsoup.parse(item.content.get(i)).body().select("p").html();
            tmpl_li = tmpl_li.replace("{{$content}}", trimmed);
            childBlock.append(tmpl_li);
        }
        tmpl_parent = tmpl_parent.replace("{{$content}}", childBlock.toString());
        return tmpl_parent;
    }
}