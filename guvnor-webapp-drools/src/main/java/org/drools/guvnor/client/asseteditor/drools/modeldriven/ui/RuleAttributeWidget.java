/*
 * Copyright 2005 JBoss Inc
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

package org.drools.guvnor.client.asseteditor.drools.modeldriven.ui;

import org.drools.guvnor.client.common.DirtyableHorizontalPane;
import org.drools.guvnor.client.common.FormStyleLayout;
import org.drools.guvnor.client.common.InfoPopup;
import org.drools.guvnor.client.common.SmallLabel;
import org.drools.guvnor.client.messages.Constants;
import org.drools.guvnor.client.resources.DroolsGuvnorImages;
import org.drools.ide.common.client.modeldriven.brl.RuleAttribute;
import org.drools.ide.common.client.modeldriven.brl.RuleMetadata;
import org.drools.ide.common.client.modeldriven.brl.RuleModel;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Displays a list of rule options (attributes).
 * <p/>
 * Added support for metadata - Michael Rhoden 10/17/08
 */
public class RuleAttributeWidget extends Composite {

    /**
     * These are the names of all of the rule attributes for this widget
     */
    public static final String  SALIENCE_ATTR         = "salience";         // needs to be public
    public static final String  ENABLED_ATTR          = "enabled";
    public static final String  DATE_EFFECTIVE_ATTR   = "date-effective";
    public static final String  DATE_EXPIRES_ATTR     = "date-expires";
    public static final String  NO_LOOP_ATTR          = "no-loop";
    public static final String  AGENDA_GROUP_ATTR     = "agenda-group";
    public static final String  ACTIVATION_GROUP_ATTR = "activation-group";
    public static final String  DURATION_ATTR         = "duration";
    public static final String  TIMER_ATTR            = "timer";
    public static final String  CALENDARS_ATTR        = "calendars";
    public static final String  AUTO_FOCUS_ATTR       = "auto-focus";
    public static final String  LOCK_ON_ACTIVE_ATTR   = "lock-on-active";
    public static final String  RULEFLOW_GROUP_ATTR   = "ruleflow-group";
    public static final String  DIALECT_ATTR          = "dialect";
    public static final String  LOCK_LHS              = "freeze_conditions";
    public static final String  LOCK_RHS              = "freeze_actions";

    /**
     * If the rule attribute is represented visually by a checkbox, these are
     * the values that will be stored in the model when checked/unchecked
     */
    private static final String TRUE_VALUE            = "true";
    private static final String FALSE_VALUE           = "false";

    private RuleModel           model;
    private RuleModeller        parent;

    public RuleAttributeWidget(RuleModeller parent,
                               RuleModel model) {
        this.parent = parent;
        this.model = model;
        FormStyleLayout layout = new FormStyleLayout();
        //Adding metadata here, seems redundant to add a new widget for metadata. Model does handle meta data separate.
        RuleMetadata[] meta = model.metadataList;
        if ( meta.length > 0 ) {
            HorizontalPanel hp = new HorizontalPanel();
            hp.add( new SmallLabel( Constants.INSTANCE.Metadata2() ) );
            layout.addRow( hp );
        }
        for ( int i = 0; i < meta.length; i++ ) {
            RuleMetadata rmd = meta[i];
            layout.addAttribute( rmd.attributeName,
                                 getEditorWidget( rmd,
                                                  i ) );
        }
        RuleAttribute[] attrs = model.attributes;
        if ( attrs.length > 0 ) {
            HorizontalPanel hp = new HorizontalPanel();
            hp.add( new SmallLabel( Constants.INSTANCE.Attributes1() ) );
            layout.addRow( hp );
        }
        for ( int i = 0; i < attrs.length; i++ ) {
            RuleAttribute at = attrs[i];
            layout.addAttribute( at.attributeName,
                                 getEditorWidget( at,
                                                  i ) );
        }

        initWidget( layout );
    }

    /**
     * Return a listbox of choices for rule attributes.
     * 
     * @return
     */
    public static ListBox getAttributeList() {
        ListBox list = new ListBox();
        list.addItem( Constants.INSTANCE.Choose() );

        list.addItem( SALIENCE_ATTR );
        list.addItem( ENABLED_ATTR );
        list.addItem( DATE_EFFECTIVE_ATTR );
        list.addItem( DATE_EXPIRES_ATTR );
        list.addItem( NO_LOOP_ATTR );
        list.addItem( AGENDA_GROUP_ATTR );
        list.addItem( ACTIVATION_GROUP_ATTR );
        list.addItem( DURATION_ATTR );
        list.addItem( TIMER_ATTR );
        list.addItem( CALENDARS_ATTR );
        list.addItem( DURATION_ATTR );
        list.addItem( AUTO_FOCUS_ATTR );
        list.addItem( LOCK_ON_ACTIVE_ATTR );
        list.addItem( RULEFLOW_GROUP_ATTR );
        list.addItem( DIALECT_ATTR );

        return list;
    }

    private Widget getEditorWidget(final RuleAttribute at,
                                   final int idx) {
        Widget editor;

        if ( at.attributeName.equals( ENABLED_ATTR ) || at.attributeName.equals( AUTO_FOCUS_ATTR ) || at.attributeName.equals( LOCK_ON_ACTIVE_ATTR ) || at.attributeName.equals( NO_LOOP_ATTR ) ) {
            editor = checkBoxEditor( at );
        } else {
            editor = textBoxEditor( at );
        }

        DirtyableHorizontalPane horiz = new DirtyableHorizontalPane();
        horiz.add( editor );
        horiz.add( getRemoveIcon( idx ) );

        return horiz;
    }

    private Widget getEditorWidget(final RuleMetadata rm,
                                   final int idx) {
        Widget editor;

        if ( rm.attributeName.equals( LOCK_LHS ) || rm.attributeName.equals( LOCK_RHS ) ) {
            editor = new InfoPopup( Constants.INSTANCE.FrozenAreas(),
                                    Constants.INSTANCE.FrozenExplanation() );
        } else {
            editor = textBoxEditor( rm );
        }

        DirtyableHorizontalPane horiz = new DirtyableHorizontalPane();
        horiz.add( editor );
        horiz.add( getRemoveMetaIcon( idx ) );

        return horiz;
    }

    private Widget checkBoxEditor(final RuleAttribute at) {
        final CheckBox box = new CheckBox();
        if ( at.value == null ) {
            box.setValue( true );
            at.value = TRUE_VALUE;
        } else {
            box.setValue( (at.value.equals( TRUE_VALUE )) );
        }

        box.addClickHandler( new ClickHandler() {
            public void onClick(ClickEvent event) {
                at.value = (box.getValue()) ? TRUE_VALUE : FALSE_VALUE;
            }
        } );
        return box;
    }

    private TextBox textBoxEditor(final RuleAttribute at) {
        final TextBox box = new TextBox();
        box.setVisibleLength( (at.value.length() < 3) ? 3 : at.value.length() );
        box.setText( at.value );
        box.addChangeHandler( new ChangeHandler() {
            public void onChange(ChangeEvent event) {
                at.value = box.getText();
            }
        } );

        if ( at.attributeName.equals( DATE_EFFECTIVE_ATTR ) || at.attributeName.equals( DATE_EXPIRES_ATTR ) ) {
            if ( at.value == null || "".equals( at.value ) ) box.setText( "" );

            box.setVisibleLength( 10 );
        }

        box.addKeyUpHandler( new KeyUpHandler() {
            public void onKeyUp(KeyUpEvent event) {
                int length = box.getText().length();
                box.setVisibleLength( length > 0 ? length : 1 );
            }
        } );
        return box;
    }

    private TextBox textBoxEditor(final RuleMetadata rm) {
        final TextBox box = new TextBox();
        box.setVisibleLength( (rm.value.length() < 3) ? 3 : rm.value.length() );
        box.setText( rm.value );
        box.addChangeHandler( new ChangeHandler() {
            public void onChange(ChangeEvent event) {
                rm.value = box.getText();
            }
        } );

        box.addKeyUpHandler( new KeyUpHandler() {
            public void onKeyUp(KeyUpEvent event) {
                box.setVisibleLength( box.getText().length() );
            }
        } );
        return box;
    }

    private Image getRemoveIcon(final int idx) {
        Image remove = new Image( DroolsGuvnorImages.INSTANCE.deleteItemSmall() );
        remove.addClickHandler( new ClickHandler() {
            public void onClick(ClickEvent event) {
                if ( Window.confirm( Constants.INSTANCE.RemoveThisRuleOption() ) ) {
                    model.removeAttribute( idx );
                    parent.refreshWidget();
                }
            }
        } );
        return remove;
    }

    private Image getRemoveMetaIcon(final int idx) {
        Image remove = new Image( DroolsGuvnorImages.INSTANCE.deleteItemSmall() );
        remove.addClickHandler( new ClickHandler() {
            public void onClick(ClickEvent event) {
                if ( Window.confirm( Constants.INSTANCE.RemoveThisRuleOption() ) ) {
                    model.removeMetadata( idx );
                    parent.refreshWidget();
                }
            }
        } );
        return remove;
    }
}
