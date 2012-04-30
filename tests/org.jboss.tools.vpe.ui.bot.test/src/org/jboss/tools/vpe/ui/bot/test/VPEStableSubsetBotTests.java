/******************************************************************************* 
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.jboss.tools.vpe.ui.bot.test.editor.BlockCommentTest;
import org.jboss.tools.vpe.ui.bot.test.editor.JspFileEditingTest;
import org.jboss.tools.vpe.ui.bot.test.editor.MinMaxPanesTest;
import org.jboss.tools.vpe.ui.bot.test.editor.MultiSelectionTest;
import org.jboss.tools.vpe.ui.bot.test.editor.StylesOnThePageTest;
import org.jboss.tools.vpe.ui.bot.test.editor.TextSelectionTest;
import org.jboss.tools.vpe.ui.bot.test.editor.ToggleCommentTest;
import org.jboss.tools.vpe.ui.bot.test.editor.VerificationOfNameSpacesTest;
import org.jboss.tools.vpe.ui.bot.test.editor.XhtmlFilePerformanceTest;
import org.jboss.tools.vpe.ui.bot.test.editor.pagedesign.AddSubstitutedELExpressionFolderScopeTest;
import org.jboss.tools.vpe.ui.bot.test.editor.pagedesign.EditingELValueTest;
import org.jboss.tools.vpe.ui.bot.test.editor.pagedesign.IncludedCssFilesJSPTest;
import org.jboss.tools.vpe.ui.bot.test.editor.pagedesign.IncludedCssFilesTest;
import org.jboss.tools.vpe.ui.bot.test.editor.pagedesign.IncludedTagLibsTest;
import org.jboss.tools.vpe.ui.bot.test.editor.pagedesign.ManipulatingELValueTest;
import org.jboss.tools.vpe.ui.bot.test.editor.pagedesign.SubstitutedELExressionsTest;
import org.jboss.tools.vpe.ui.bot.test.editor.pagedesign.ToolbarTextFormattingTest;
import org.jboss.tools.vpe.ui.bot.test.editor.preferences.AlwaysHideSelectionBarWithoutPromptTest;
import org.jboss.tools.vpe.ui.bot.test.editor.preferences.BorderForUnknownTagsTest;
import org.jboss.tools.vpe.ui.bot.test.editor.preferences.ChangeEditorTabForTheFirstOpenPageTest;
import org.jboss.tools.vpe.ui.bot.test.editor.preferences.GlobalELVariablesTest;
import org.jboss.tools.vpe.ui.bot.test.editor.preferences.PromptForTagAttributesDuringTagInsertTest;
import org.jboss.tools.vpe.ui.bot.test.editor.preferences.ShowNonVisualTagsTest;
import org.jboss.tools.vpe.ui.bot.test.editor.preferences.ShowResourceBundlesUsageasELexpressionsTest;
import org.jboss.tools.vpe.ui.bot.test.editor.preferences.ShowSelectionTagBarTest;
import org.jboss.tools.vpe.ui.bot.test.editor.selectionbar.SelectionBarTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.ActionParamTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.AjaxInvisibleTagsTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.AjaxValidatorTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.BeanValidatorTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.CalendarTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.ColumnGroupTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.ColumnTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.ColumnsTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.ComboBoxTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.CommandButtonTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.CommandLinkTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.CoreHTMLTagsTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.DataDefinitionTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.DataGridTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.DataListTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.DataOrderedListTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.DataScrollerTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.DataTableTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.EditorTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.ExtendedDataTableTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.FileUploadTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.HtmlCommandLinkTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.IncludeTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.InplaceInputTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.InplaceSelectInputTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.JSFTagsTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.ListShuttleTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.LogTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.PanelMenuTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.PanelTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.PickListTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.ProgressTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.SpacerTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.TogglePanelAndToogleControlTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.ToolbarAndToolbarGroupTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.TreeTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.VirtualEarthTagTest;
import org.jboss.tools.vpe.ui.bot.test.jbide.JBIDE4556Test;
import org.jboss.tools.vpe.ui.bot.test.jbide.JBIDE9445Test_DuplicateSourceMenu;
import org.jboss.tools.vpe.ui.bot.test.palette.CancelTagLibDefenitionTest;
import org.jboss.tools.vpe.ui.bot.test.palette.ImportTagsFromTLDFileTest;
import org.jboss.tools.vpe.ui.bot.test.palette.ManagePaletteGroupsTest;
import org.jboss.tools.vpe.ui.bot.test.palette.PaletteEditorTest;
import org.jboss.tools.vpe.ui.bot.test.smoke.CodeCompletionTest;
import org.jboss.tools.vpe.ui.bot.test.smoke.EditorSynchronizationTest;
import org.jboss.tools.vpe.ui.bot.test.smoke.JSPPageCreationTest;
import org.jboss.tools.vpe.ui.bot.test.smoke.MarkersTest;
import org.jboss.tools.vpe.ui.bot.test.smoke.OpenOnTest;
import org.jboss.tools.vpe.ui.bot.test.smoke.RenameFacesConfigFileTest;
import org.jboss.tools.vpe.ui.bot.test.smoke.RenameJSPFileTest;
import org.jboss.tools.vpe.ui.bot.test.smoke.RenameXHTMLFileTest;
import org.jboss.tools.vpe.ui.bot.test.smoke.XHTMLPageCreationTest;
import org.jboss.tools.vpe.ui.bot.test.wizard.ExternalizeStringsDialogTest;
import org.jboss.tools.vpe.ui.bot.test.wizard.ImportUnknownTagsWizardTest;
import org.jboss.tools.vpe.ui.bot.test.wizard.NewXHTMLPageWizardTest;
import org.jboss.tools.vpe.ui.bot.test.wizard.VPESourceCodeTemplatesPreferencePageTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;
/**
 * Test Suite containing stable SWT Bot test i. e. test which should always pass
 * and are not firing events in Visual part of VPE.
 * Main purpose of this sub suite of VPEAllBotTests suite is to be used by
 * Continuous Integration Tools for testing VPE component 
 * 
 * Can be deleted once we have stable tests firing events in Visual part of VPE.
 *  
 * @author vpakan
 *
 */
@RunWith(RequirementAwareSuite.class)
@SuiteClasses ({
    NewXHTMLPageWizardTest.class,
    CodeCompletionTest.class,
    ExternalizeStringsDialogTest.class,
    CancelTagLibDefenitionTest.class,
    ImportTagsFromTLDFileTest.class,
    ToggleCommentTest.class,
    BlockCommentTest.class,
    ChangeEditorTabForTheFirstOpenPageTest.class,
    JBIDE4556Test.class,
    VerificationOfNameSpacesTest.class,
    BorderForUnknownTagsTest.class,
    ShowResourceBundlesUsageasELexpressionsTest.class,
    ShowSelectionTagBarTest.class,
    AlwaysHideSelectionBarWithoutPromptTest.class,
    ShowNonVisualTagsTest.class,
    AddSubstitutedELExpressionFolderScopeTest.class,
    EditorSynchronizationTest.class,
    JSPPageCreationTest.class,
    XHTMLPageCreationTest.class,
    RenameFacesConfigFileTest.class,
    RenameJSPFileTest.class,
    RenameXHTMLFileTest.class,
    ImportUnknownTagsWizardTest.class,
    VPESourceCodeTemplatesPreferencePageTest.class,
    JspFileEditingTest.class,
    ManagePaletteGroupsTest.class,
    PaletteEditorTest.class,
    ToolbarTextFormattingTest.class,
    PromptForTagAttributesDuringTagInsertTest.class,
    IncludedTagLibsTest.class,
    SubstitutedELExressionsTest.class,
    MinMaxPanesTest.class,
    EditingELValueTest.class,
    ManipulatingELValueTest.class,
    SelectionBarTest.class,
    IncludedCssFilesTest.class,
    GlobalELVariablesTest.class,
    IncludedCssFilesJSPTest.class,
    StylesOnThePageTest.class,
    TextSelectionTest.class,
    CoreHTMLTagsTest.class,
    JSFTagsTest.class,
    ColumnsTagTest.class,
    ComboBoxTagTest.class,
    FileUploadTagTest.class,
    InplaceInputTagTest.class,
    InplaceSelectInputTagTest.class,
    PickListTagTest.class,
    ProgressTagTest.class,
    PanelMenuTagTest.class,
    ListShuttleTagTest.class,
    DataDefinitionTagTest.class,
    EditorTagTest.class,
    TreeTagTest.class,
    CalendarTagTest.class,
    PanelTagTest.class,
    DataTableTagTest.class,
    SpacerTagTest.class,
    DataScrollerTagTest.class,
    ColumnTagTest.class,
    ActionParamTagTest.class,
    AjaxValidatorTagTest.class,
    BeanValidatorTagTest.class,
    ColumnGroupTagTest.class,
    DataGridTagTest.class,
    VirtualEarthTagTest.class,
    DataListTagTest.class,
    DataOrderedListTagTest.class,
    ExtendedDataTableTagTest.class,
    ToolbarAndToolbarGroupTagTest.class,
    TogglePanelAndToogleControlTagTest.class,
    CommandButtonTagTest.class,
    CommandLinkTagTest.class,
    HtmlCommandLinkTagTest.class,
    IncludeTagTest.class,
    AjaxInvisibleTagsTest.class,
    LogTagTest.class,
    OpenOnTest.class,
    XhtmlFilePerformanceTest.class,
    MarkersTest.class,
    JBIDE9445Test_DuplicateSourceMenu.class,
    MultiSelectionTest.class
})
public class VPEStableSubsetBotTests extends SWTBotTestCase {

}
