/******************************************************************************* 
 * Copyright (c) 2016-2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.jst.ui.bot.test.tern;

import java.util.LinkedList;
import java.util.List;

import org.jboss.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.jboss.reddeer.jface.text.contentassist.ContentAssistant;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.workbench.handler.WorkbenchShellHandler;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.jst.ui.bot.test.JSTTestBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TernCodeAssistTest extends JSTTestBase {

	
	@Before
	public void prepare() {
		createJSProject(PROJECT_NAME);
		createJSFile(JS_FILE);
		cleanEditor();
	}

	@After
	public void cleanup() {
		WorkbenchShellHandler.getInstance().closeAllNonWorbenchShells();
		new ProjectExplorer().deleteAllProjects();
	}
	
	@Test
	public void testTernBrowserCodeAssist(){
		ContentAssistant assistant = new DefaultEditor(JS_FILE).openContentAssistant();
		String missingProposals = getMisingString(assistant.getProposals(), getBrowserProposalList());
		assistant.close();
		assertTrue("There are missing Code Assist proposals for 'browser':" + missingProposals, missingProposals.length() == 0);
	}
	
	@Test
	public void testTernEcma5CodeAssist(){
		ContentAssistant assistant = new DefaultEditor(JS_FILE).openContentAssistant();
		String missingProposals = getMisingString(assistant.getProposals(), getEcma5ProposalList());
		assistant.close();
		assertTrue("There are missing Code Assist proposals for 'ecma5':" + missingProposals, missingProposals.length() == 0);
		
	}
	
	@Test
	public void testTernAngularCodeAssist(){
		setTernModule("AngularJS");
		DefaultEditor editor = new DefaultEditor(JS_FILE);
		editor.activate();
		DefaultStyledText text = new DefaultStyledText();
		text.setText("angular."); editor.save();
		text.selectPosition("angular.".length());
		ContentAssistant assistant = editor.openContentAssistant();
		String missingProposals = getMisingString(assistant.getProposals(), getAngularProposalList());
		assistant.close();
		assertTrue("There are missing Code Assist proposals for 'angular':" + missingProposals, missingProposals.length() == 0);
	}
	
	private static List<String> getAngularProposalList() {
		LinkedList<String> result = new LinkedList<String>();
	
		result.add("bind : fn - angular");
		result.add("bind(self, fn) - angular");
		result.add("bind(self, fn, args) - angular");
		result.add("bootstrap : fn - angular");
		result.add("bootstrap(element) : service.$injector - angular");
		result.add("bootstrap(element, modules) : service.$injector - angular");
		result.add("copy : fn - angular");
		result.add("copy(source) - angular");
		result.add("copy(source, target) - angular");
		result.add("element : fn - angular");
		result.add("element(element) - angular");
		result.add("equals : fn - angular");
		result.add("equals(o1, o2) : bool - angular");
		result.add("extend : fn - angular");
		result.add("extend(dst, src) - angular");
		result.add("forEach : fn - angular");
		result.add("forEach(obj, iterator) - angular");
		result.add("forEach(obj, iterator, context) - angular");
		result.add("fromJson : fn - angular");
		result.add("fromJson(json) - angular");
		result.add("identity : fn - angular");
		result.add("identity(val) - angular");
		result.add("injector : fn - angular");
		result.add("injector(modules) : service.$injector - angular");
		result.add("isArray : fn - angular");
		result.add("isArray(val) : bool - angular");
		result.add("isDate : fn - angular");
		result.add("isDate(val) : bool - angular");
		result.add("isDefined : fn - angular");
		result.add("isDefined(val) : bool - angular");
		result.add("isElement : fn - angular");
		result.add("isElement(val) : bool - angular");
		result.add("isFunction : fn - angular");
		result.add("isFunction(val) : bool - angular");
		result.add("isNumber : fn - angular");
		result.add("isNumber(val) : bool - angular");
		result.add("isObject : fn - angular");
		result.add("isObject(val) : bool - angular");
		result.add("isString : fn - angular");
		result.add("isString(val) : bool - angular");
		result.add("isUndefined : fn - angular");
		result.add("isUndefined(val) : bool - angular");
		result.add("lowercase : fn - angular");
		result.add("lowercase(val) : string - angular");
		result.add("module : fn - angular");
		result.add("Module : Module - angular");
		result.add("module(name, deps) - angular");
		result.add("noop : fn - angular");
		result.add("noop() - angular");
		result.add("toJson : fn - angular");
		result.add("toJson(val) : string - angular");
		result.add("uppercase : fn - angular");
		result.add("uppercase() : string - angular");
		result.add("version : angular.version - angular");
		
		return result;
	}
	
	private static List<String> getEcma5ProposalList() {
		LinkedList<String> result = new LinkedList<String>();
	
		result.add("Boolean : fn - ecma5");
		result.add("Boolean(value) : bool - ecma5");
		result.add("decodeURI : fn - ecma5");
		result.add("decodeURI(uri) : string - ecma5");
		result.add("decodeURIComponent : fn - ecma5");
		result.add("decodeURIComponent(uri) : string - ecma5");
		result.add("encodeURI : fn - ecma5");
		result.add("encodeURI(uri) : string - ecma5");
		result.add("encodeURIComponent : fn - ecma5");
		result.add("encodeURIComponent(uri) : string - ecma5");
		result.add("Error : fn - ecma5");
		result.add("Error(message) - ecma5");
		result.add("eval : fn - ecma5");
		result.add("eval(code) - ecma5");
		result.add("EvalError : fn - ecma5");
		result.add("EvalError(message) - ecma5");
		result.add("Function : fn - ecma5");
		result.add("Function(body) : fn() - ecma5");
		result.add("hasOwnProperty : fn - ecma5");
		result.add("hasOwnProperty(prop) : bool - ecma5");
		result.add("Infinity : number - ecma5");
		result.add("isFinite : fn - ecma5");
		result.add("isFinite(value) : bool - ecma5");
		result.add("isNaN : fn - ecma5");
		result.add("isNaN(value) : bool - ecma5");
		result.add("isPrototypeOf : fn - ecma5");
		result.add("isPrototypeOf(obj) : bool - ecma5");
		result.add("JSON : JSON - ecma5");
		result.add("NaN : number - ecma5");
		result.add("parseFloat : fn - ecma5");
		result.add("parseFloat(string) : number - ecma5");
		result.add("parseInt : fn - ecma5");
		result.add("parseInt(string) : number - ecma5");
		result.add("parseInt(string, radix) : number - ecma5");
		result.add("propertyIsEnumerable : fn - ecma5");
		result.add("propertyIsEnumerable(prop) : bool - ecma5");
		result.add("RangeError : fn - ecma5");
		result.add("RangeError(message) - ecma5");
		result.add("ReferenceError : fn - ecma5");
		result.add("ReferenceError(message) - ecma5");
		result.add("SyntaxError : fn - ecma5");
		result.add("SyntaxError(message) - ecma5");
		result.add("toLocaleString : fn - ecma5");
		result.add("toLocaleString() : string - ecma5");
		result.add("toString : fn - ecma5");
		result.add("toString() : string - ecma5");
		result.add("TypeError : fn - ecma5");
		result.add("TypeError(message) - ecma5");
		result.add("undefined : ? - ecma5");
		result.add("URIError : fn - ecma5");
		result.add("URIError(message) - ecma5");
		result.add("valueOf : fn - ecma5");
		result.add("valueOf() : number - ecma5");
		
		return result;
	}
		
		
	private static List<String> getBrowserProposalList() {
		LinkedList<String> result = new LinkedList<String>();
		
		result.add("addEventListener : fn - browser");
		result.add("addEventListener(type, listener, capture) - browser");
		result.add("alert : fn - browser");
		result.add("alert(message) - browser");
		result.add("atob : fn - browser");
		result.add("atob(encoded) : string - browser");
		result.add("Attr : fn - browser");
		result.add("Attr() - browser");
		result.add("BeforeLoadEvent : fn - browser");
		result.add("BeforeLoadEvent() - browser");
		result.add("Blob : fn - browser");
		result.add("Blob(parts) - browser");
		result.add("Blob(parts, properties) - browser");
		result.add("blur : fn - browser");
		result.add("blur() - browser");
		result.add("btoa : fn - browser");
		result.add("btoa(data) : string - browser");
		result.add("CanvasRenderingContext2D : CanvasRenderingContext2D - browser");
		result.add("clearInterval : fn - browser");
		result.add("clearInterval(interval) - browser");
		result.add("clearTimeout : fn - browser");
		result.add("clearTimeout(timeout) - browser");
		result.add("ClientRect : fn - browser");
		result.add("ClientRect() - browser");
		result.add("close : fn - browser");
		result.add("close() - browser");
		result.add("closed : bool - browser");
		result.add("confirm : fn - browser");
		result.add("confirm(message) : bool - browser");
		result.add("console : console - browser");
		result.add("crypto : crypto - browser");
		result.add("CustomEvent : fn - browser");
		result.add("CustomEvent() - browser");
		result.add("devicePixelRatio : number - browser");
		result.add("dispatchEvent : fn - browser");
		result.add("dispatchEvent(event) : bool - browser");
		result.add("document : Document - browser");
		result.add("Document : fn - browser");
		result.add("Document() - browser");
		result.add("DocumentFragment : fn - browser");
		result.add("DocumentFragment() - browser");
		result.add("DOMParser : fn - browser");
		result.add("DOMParser() - browser");
		result.add("DOMTokenList : fn - browser");
		result.add("DOMTokenList() - browser");
		result.add("Element : fn - browser");
		result.add("Element() - browser");
		result.add("ErrorEvent : fn - browser");
		result.add("ErrorEvent() - browser");
		result.add("Event : fn - browser");
		result.add("Event() - browser");
		result.add("File : fn - browser");
		result.add("File() - browser");
		result.add("FileList : fn - browser");
		result.add("FileList() - browser");
		result.add("FileReader : fn - browser");
		result.add("FileReader() - browser");
		result.add("focus : fn - browser");
		result.add("focus() - browser");
		result.add("FormData : fn - browser");
		result.add("FormData() - browser");
		result.add("frameElement : Element - browser");
		result.add("getComputedStyle : fn - browser");
		result.add("getComputedStyle(node) : Element.prototype.style - browser");
		result.add("getComputedStyle(node, pseudo) : Element.prototype.style - browser");
		result.add("getSelection : fn - browser");
		result.add("getSelection() : Selection - browser");
		result.add("HashChangeEvent : fn - browser");
		result.add("HashChangeEvent() - browser");
		result.add("history : history - browser");
		result.add("HTMLAnchorElement : fn - browser");
		result.add("HTMLAnchorElement() - browser");
		result.add("HTMLAreaElement : fn - browser");
		result.add("HTMLAreaElement() - browser");
		result.add("HTMLAudioElement : fn - browser");
		result.add("HTMLAudioElement() - browser");
		result.add("HTMLBaseElement : fn - browser");
		result.add("HTMLBaseElement() - browser");
		result.add("HTMLBodyElement : fn - browser");
		result.add("HTMLBodyElement() - browser");
		result.add("HTMLBRElement : fn - browser");
		result.add("HTMLBRElement() - browser");
		result.add("HTMLButtonElement : fn - browser");
		result.add("HTMLButtonElement() - browser");
		result.add("HTMLCanvasElement : fn - browser");
		result.add("HTMLCanvasElement() - browser");
		result.add("HTMLCollection : fn - browser");
		result.add("HTMLCollection() - browser");
		result.add("HTMLDataElement : fn - browser");
		result.add("HTMLDataElement() - browser");
		result.add("HTMLDataListElement : fn - browser");
		result.add("HTMLDataListElement() - browser");
		result.add("HTMLDivElement : fn - browser");
		result.add("HTMLDivElement() - browser");
		result.add("HTMLDListElement : fn - browser");
		result.add("HTMLDListElement() - browser");
		result.add("HTMLDocument : fn - browser");
		result.add("HTMLDocument() - browser");
		result.add("HTMLElement : fn - browser");
		result.add("HTMLElement() - browser");
		result.add("HTMLEmbedElement : fn - browser");
		result.add("HTMLEmbedElement() - browser");
		result.add("HTMLFieldSetElement : fn - browser");
		result.add("HTMLFieldSetElement() - browser");
		result.add("HTMLFormControlsCollection : fn - browser");
		result.add("HTMLFormControlsCollection() - browser");
		result.add("HTMLFormElement : fn - browser");
		result.add("HTMLFormElement() - browser");
		result.add("HTMLHeadElement : fn - browser");
		result.add("HTMLHeadElement() - browser");
		result.add("HTMLHeadingElement : fn - browser");
		result.add("HTMLHeadingElement() - browser");
		result.add("HTMLHRElement : fn - browser");
		result.add("HTMLHRElement() - browser");
		result.add("HTMLHtmlElement : fn - browser");
		result.add("HTMLHtmlElement() - browser");
		result.add("HTMLIFrameElement : fn - browser");
		result.add("HTMLIFrameElement() - browser");
		result.add("HTMLImageElement : fn - browser");
		result.add("HTMLImageElement() - browser");
		result.add("HTMLInputElement : fn - browser");
		result.add("HTMLInputElement() - browser");
		result.add("HTMLKeygenElement : fn - browser");
		result.add("HTMLKeygenElement() - browser");
		result.add("HTMLLabelElement : fn - browser");
		result.add("HTMLLabelElement() - browser");
		result.add("HTMLLegendElement : fn - browser");
		result.add("HTMLLegendElement() - browser");
		result.add("HTMLLIElement : fn - browser");
		result.add("HTMLLIElement() - browser");
		result.add("HTMLLinkElement : fn - browser");
		result.add("HTMLLinkElement() - browser");
		result.add("HTMLMapElement : fn - browser");
		result.add("HTMLMapElement() - browser");
		result.add("HTMLMediaElement : fn - browser");
		result.add("HTMLMediaElement() - browser");
		result.add("HTMLMetaElement : fn - browser");
		result.add("HTMLMetaElement() - browser");
		result.add("HTMLMeterElement : fn - browser");
		result.add("HTMLMeterElement() - browser");
		result.add("HTMLModElement : fn - browser");
		result.add("HTMLModElement() - browser");
		result.add("HTMLObjectElement : fn - browser");
		result.add("HTMLObjectElement() - browser");
		result.add("HTMLOListElement : fn - browser");
		result.add("HTMLOListElement() - browser");
		result.add("HTMLOptGroupElement : fn - browser");
		result.add("HTMLOptGroupElement() - browser");
		result.add("HTMLOptionElement : fn - browser");
		result.add("HTMLOptionElement() - browser");
		result.add("HTMLOptionsCollection : fn - browser");
		result.add("HTMLOptionsCollection() - browser");
		result.add("HTMLOutputElement : fn - browser");
		result.add("HTMLOutputElement() - browser");
		result.add("HTMLParagraphElement : fn - browser");
		result.add("HTMLParagraphElement() - browser");
		result.add("HTMLParamElement : fn - browser");
		result.add("HTMLParamElement() - browser");
		result.add("HTMLPreElement : fn - browser");
		result.add("HTMLPreElement() - browser");
		result.add("HTMLProgressElement : fn - browser");
		result.add("HTMLProgressElement() - browser");
		result.add("HTMLQuoteElement : fn - browser");
		result.add("HTMLQuoteElement() - browser");
		result.add("HTMLScriptElement : fn - browser");
		result.add("HTMLScriptElement() - browser");
		result.add("HTMLSelectElement : fn - browser");
		result.add("HTMLSelectElement() - browser");
		result.add("HTMLSourceElement : fn - browser");
		result.add("HTMLSourceElement() - browser");
		result.add("HTMLSpanElement : fn - browser");
		result.add("HTMLSpanElement() - browser");
		result.add("HTMLStyleElement : fn - browser");
		result.add("HTMLStyleElement() - browser");
		result.add("HTMLTableCaptionElement : fn - browser");
		result.add("HTMLTableCaptionElement() - browser");
		result.add("HTMLTableCellElement : fn - browser");
		result.add("HTMLTableCellElement() - browser");
		result.add("HTMLTableColElement : fn - browser");
		result.add("HTMLTableColElement() - browser");
		result.add("HTMLTableDataCellElement : fn - browser");
		result.add("HTMLTableDataCellElement() - browser");
		result.add("HTMLTableElement : fn - browser");
		result.add("HTMLTableElement() - browser");
		result.add("HTMLTableHeaderCellElement : fn - browser");
		result.add("HTMLTableHeaderCellElement() - browser");
		result.add("HTMLTableRowElement : fn - browser");
		result.add("HTMLTableRowElement() - browser");
		result.add("HTMLTableSectionElement : fn - browser");
		result.add("HTMLTableSectionElement() - browser");
		result.add("HTMLTextAreaElement : fn - browser");
		result.add("HTMLTextAreaElement() - browser");
		result.add("HTMLTimeElement : fn - browser");
		result.add("HTMLTimeElement() - browser");
		result.add("HTMLTitleElement : fn - browser");
		result.add("HTMLTitleElement() - browser");
		result.add("HTMLTrackElement : fn - browser");
		result.add("HTMLTrackElement() - browser");
		result.add("HTMLUListElement : fn - browser");
		result.add("HTMLUListElement() - browser");
		result.add("HTMLUnknownElement : fn - browser");
		result.add("HTMLUnknownElement() - browser");
		result.add("HTMLVideoElement : fn - browser");
		result.add("HTMLVideoElement() - browser");
		result.add("Image : fn - browser");
		result.add("Image() : Element - browser");
		result.add("Image(height) : Element - browser");
		result.add("Image(width) : Element - browser");
		result.add("Image(width, height) : Element - browser");
		result.add("innerHeight : number - browser");
		result.add("innerWidth : number - browser");
		result.add("KeyboardEvent : fn - browser");
		result.add("KeyboardEvent() - browser");
		result.add("localStorage : localStorage - browser");
		result.add("location : location - browser");
		result.add("MouseEvent : fn - browser");
		result.add("MouseEvent() - browser");
		result.add("name : string - browser");
		result.add("NamedNodeMap : fn - browser");
		result.add("NamedNodeMap() - browser");
		result.add("navigator : navigator - browser");
		result.add("Node : fn - browser");
		result.add("Node() - browser");
		result.add("NodeList : fn - browser");
		result.add("NodeList() - browser");
		result.add("onabort : ? - browser");
		result.add("onbeforeunload : ? - browser");
		result.add("onblur : ? - browser");
		result.add("onchange : ? - browser");
		result.add("onclick : ? - browser");
		result.add("oncontextmenu : ? - browser");
		result.add("ondblclick : ? - browser");
		result.add("ondrag : ? - browser");
		result.add("ondragend : ? - browser");
		result.add("ondragenter : ? - browser");
		result.add("ondragleave : ? - browser");
		result.add("ondragover : ? - browser");
		result.add("ondragstart : ? - browser");
		result.add("ondrop : ? - browser");
		result.add("onerror : ? - browser");
		result.add("onfocus : ? - browser");
		result.add("onhashchange : ? - browser");
		result.add("oninput : ? - browser");
		result.add("onkeydown : ? - browser");
		result.add("onkeypress : ? - browser");
		result.add("onkeyup : ? - browser");
		result.add("onload : ? - browser");
		result.add("onmessage : ? - browser");
		result.add("onmousedown : ? - browser");
		result.add("onmousemove : ? - browser");
		result.add("onmouseout : ? - browser");
		result.add("onmouseover : ? - browser");
		result.add("onmouseup : ? - browser");
		result.add("onmousewheel : ? - browser");
		result.add("onoffline : ? - browser");
		result.add("ononline : ? - browser");
		result.add("onpopstate : ? - browser");
		result.add("onresize : ? - browser");
		result.add("onscroll : ? - browser");
		result.add("onunload : ? - browser");
		result.add("opener : <top> - browser");
		result.add("outerHeight : number - browser");
		result.add("outerWidth : number - browser");
		result.add("pageXOffset : number - browser");
		result.add("pageYOffset : number - browser");
		result.add("parent : <top> - browser");
		result.add("postMessage : fn - browser");
		result.add("postMessage(message, targetOrigin) - browser");
		result.add("prompt : fn - browser");
		result.add("prompt(message, value) : string - browser");
		result.add("Range : fn - browser");
		result.add("Range() - browser");
		result.add("removeEventListener : fn - browser");
		result.add("removeEventListener(type, listener, capture) - browser");
		result.add("screen : screen - browser");
		result.add("screenLeft : number - browser");
		result.add("screenTop : number - browser");
		result.add("screenX : number - browser");
		result.add("screenY : number - browser");
		result.add("scroll : fn - browser");
		result.add("scroll(x, y) - browser");
		result.add("scrollBy : fn - browser");
		result.add("scrollBy(x, y) - browser");
		result.add("scrollTo : fn - browser");
		result.add("scrollTo(x, y) - browser");
		result.add("scrollX : number - browser");
		result.add("scrollY : number - browser");
		result.add("Selection : fn - browser");
		result.add("Selection() - browser");
		result.add("self : <top> - browser");
		result.add("sessionStorage : sessionStorage - browser");
		result.add("setInterval : fn - browser");
		result.add("setInterval(f, ms) : number - browser");
		result.add("setTimeout : fn - browser");
		result.add("setTimeout(f, ms) : number - browser");
		result.add("Text : fn - browser");
		result.add("Text() - browser");
		result.add("top : <top> - browser");
		result.add("TouchEvent : fn - browser");
		result.add("TouchEvent() - browser");
		result.add("URL : URL - browser");
		result.add("WebSocket : fn - browser");
		result.add("WebSocket(url) - browser");
		result.add("WheelEvent : fn - browser");
		result.add("WheelEvent() - browser");
		result.add("window : <top> - browser");
		result.add("Worker : fn - browser");
		result.add("Worker(scriptURL) - browser");
		result.add("XMLDocument : fn - browser");
		result.add("XMLDocument() - browser");
		result.add("XMLHttpRequest : fn - browser");
		result.add("XMLHttpRequest() - browser");
		result.add("XPathResult : fn - browser");
		result.add("XPathResult() - browser");
		
		return result;
	} 
	
}
