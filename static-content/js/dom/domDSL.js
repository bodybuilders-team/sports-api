import {LogError} from "../errorUtils.js";

/**
 * Creates an HTML element.
 *
 * @param {string} tag element tag
 * @param {Object | Promise<HTMLElement> | HTMLElement | string} [attributes] element attributes or an element child
 * @param {Promise<HTMLElement> | HTMLElement | string} [children] element children
 *
 * @returns Promise<HTMLElement>
 */
export async function createElement(tag, attributes, ...children) {
    /** @type {HTMLElement} */
    const element = document.createElement(tag);

    attributes = await attributes;

    if (isElement(attributes) || typeof attributes === "string")
        appendChild(element, attributes);

    else if (attributes != null && typeof attributes === "object")
        setAttributes(element, attributes);

    else if (attributes != null)
        throw new LogError("Invalid attributes for createElement");

    for (let child of children) {
        child = await child;

        if (child != null && (isElement(child) || typeof child === "string"))
            appendChild(element, child);

        else if (child != null)
            throw new LogError("Invalid child:", child, "for element:", element);
    }

    return element;
}

/**
 * @param {HTMLElement} element
 * @param {string | HTMLElement} child
 */
function appendChild(element, child) {
    if (typeof child === "string")
        element.appendChild(document.createTextNode(child));
    else
        element.appendChild(child);
}

/**
 * @param {HTMLElement} element
 * @param {Object} attributes
 */
function setAttributes(element, attributes) {
    for (const attribute in attributes) {
        if (attribute == null)
            continue;

        const value = attributes[attribute];
        if (value == null)
            continue;

        switch (attribute) {
            case "onClick":
                element.addEventListener("click", value);
                break;
            case "onSubmit":
                element.addEventListener("submit", value);
                break;
            default:
                element.setAttribute(attribute, value);
        }
    }
}

/**
 * Checks if an object is a DOM element.
 * @param {any} obj object to check
 * @returns true if it is a DOM element; false otherwise
 */
function isElement(obj) {
    return (
        typeof HTMLElement === "object" ? obj instanceof HTMLElement :
            obj && typeof obj === "object" && obj.nodeType === 1 && typeof obj.nodeName === "string"
    );
}