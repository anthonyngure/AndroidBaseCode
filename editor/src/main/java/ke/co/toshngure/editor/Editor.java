/*
 * Copyright (C) 2016 Muhammed Irshad
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ke.co.toshngure.editor;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

import java.util.Map;

import ke.co.toshngure.editor.components.CustomEditText;
import ke.co.toshngure.editor.models.EditorContent;
import ke.co.toshngure.editor.models.EditorTextStyle;
import ke.co.toshngure.editor.models.RenderType;

public class Editor extends EditorCore {
    public Editor(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setEditorListener(null);
        //  initialize(context,parentView,renderType,_PlaceHolderText);
    }

    public void setEditorListener(EditorListener _listener) {
        super.setEditorListener(_listener);
    }

    public EditorContent getContent() {
        return super.getContent();
    }


    public String getContentAsHTML() {
        return getHtmlExtensions().getContentAsHTML();
    }

    public void render(String HtmlString) {
        renderEditorFromHtml(HtmlString);
    }

    public void render() {
        if (getRenderType() == RenderType.Editor) {
            getInputExtensions().insertEditText(0, this.placeHolder, null);
        }
    }

    public void clearAllContents() {
        super.clearAllContents();
        if (getRenderType() == RenderType.Editor) {
            getInputExtensions().insertEditText(0, this.placeHolder, null);
        }
    }


    public void updateTextStyle(EditorTextStyle style) {
        getInputExtensions().UpdateTextStyle(style, null);
    }

    public void insertLink() {
        getInputExtensions().insertLink();
    }

    public void insertLink(String link) {
        getInputExtensions().insertLink(link);
    }

    public Map<Integer, String> getContentTypeface() {
        return getInputExtensions().getContentTypeface();
    }

    /**
     * setup the fontfaces for editor content
     * For eg:
     * Map<Integer, String> typefaceMap = new HashMap<>();
     * typefaceMap.put(Typeface.NORMAL,"fonts/GreycliffCF-Medium.ttf");
     * typefaceMap.put(Typeface.BOLD,"fonts/GreycliffCF-Bold.ttf");
     * typefaceMap.put(Typeface.ITALIC,"fonts/GreycliffCF-Medium.ttf");
     * typefaceMap.put(Typeface.BOLD_ITALIC,"fonts/GreycliffCF-Medium.ttf");
     *
     * @param map
     */

    public void setContentTypeface(Map<Integer, String> map) {
        getInputExtensions().setContentTypeface(map);
    }


        /*
         *
         * Divider extension
         *
         */

    public void setDividerLayout(int layout) {
        this.getDividerExtensions().setDividerLayout(layout);
    }

    public void insertDivider() {
        getDividerExtensions().insertDivider();
    }

    /*
     *
     *List Item extension
     *
     */
    public void setListItemLayout(int layout) {
        this.getListItemExtensions().setListItemTemplate(layout);
    }

    public void insertList(boolean isOrdered) {
        this.getListItemExtensions().insertlist(isOrdered);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event, CustomEditText editText) {
        boolean onKey = super.onKey(v, keyCode, event, editText);
        if (getParentChildCount() == 0)
            render();
        return onKey;
    }
}
