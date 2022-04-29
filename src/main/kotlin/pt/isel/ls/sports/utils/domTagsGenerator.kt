package pt.isel.ls.sports.utils

import java.io.File

/**
 * Generates a file named domTags.js which exports a function for each HTML tag.
 * These functions are used in the static-content part of the project.
 */
fun main() {
    val htmlTags = listOf(
        "a",
        "abbr",
        "acronym",
        "address",
        "applet",
        "area",
        "article",
        "aside",
        "audio",
        "b",
        "base",
        "basefont",
        "bdi",
        "bdo",
        "bgsound",
        "big",
        "blink",
        "blockquote",
        "body",
        "br",
        "button",
        "canvas",
        "caption",
        "center",
        "cite",
        "code",
        "col",
        "colgroup",
        "content",
        "data",
        "datalist",
        "dd",
        "decorator",
        "del",
        "details",
        "dfn",
        "dir",
        "div",
        "dl",
        "dt",
        "element",
        "em",
        "embed",
        "fieldset",
        "figcaption",
        "figure",
        "font",
        "footer",
        "form",
        "frame",
        "frameset",
        "h1",
        "h2",
        "h3",
        "h4",
        "h5",
        "h6",
        "head",
        "header",
        "hgroup",
        "hr",
        "html",
        "i",
        "iframe",
        "img",
        "input",
        "ins",
        "isindex",
        "kbd",
        "keygen",
        "label",
        "legend",
        "li",
        "link",
        "listing",
        "main",
        "map",
        "mark",
        "marquee",
        "menu",
        "menuitem",
        "meta",
        "meter",
        "nav",
        "nobr",
        "noframes",
        "noscript",
        "object",
        "ol",
        "optgroup",
        "option",
        "output",
        "p",
        "param",
        "plaintext",
        "pre",
        "progress",
        "q",
        "rp",
        "rt",
        "ruby",
        "s",
        "samp",
        "script",
        "section",
        "select",
        "shadow",
        "small",
        "source",
        "spacer",
        "span",
        "strike",
        "strong",
        "style",
        "sub",
        "summary",
        "sup",
        "table",
        "tbody",
        "td",
        "template",
        "textarea",
        "tfoot",
        "th",
        "thead",
        "time",
        "title",
        "tr",
        "track",
        "tt",
        "u",
        "ul",
        "video",
        "wbr",
        "xmp"
    )

    File("static-content/js/dom/domTags.js").printWriter().use { printer ->
        printer.println("import { createElement } from \"./domDSL.js\"\n")

        htmlTags.forEach { tag ->
            printer.print(
                """
                /**
                 * Creates a '$tag' HTML element.
                 *
                 * @param {Object | Promise<HTMLElement> | HTMLElement | string} [attributes] element attributes or an element child
                 * @param {Promise<HTMLElement> | HTMLElement | string} [children] element children
                 * 
                 * @returns Promise<HTMLElement>
                 */
                export function $tag(attributes, ...children) {
                    return createElement("$tag", attributes, ...children);
                }
                
                
                """.trimIndent()
            )
        }
    }
}
