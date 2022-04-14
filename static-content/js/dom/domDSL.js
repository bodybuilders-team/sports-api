/**
 * Creates an HTML element.
 *
 * @param tag element tag
 * @param attributes element attributes
 * @param children element children
 * @returns created element
 */
export function createElement(tag, attributes, ...children) {
    const element = document.createElement(tag);

    if (typeof attributes === "object")
        Object.keys(attributes).forEach((attribute) => {
            element.setAttribute(attribute, attributes[attribute])
        });

    else if (attributes !== undefined) {
        if (typeof attributes === "string")
            attributes = document.createTextNode(attributes);

        element.appendChild(attributes);
    }

    children.forEach((child) => {
        if (typeof child === "string")
            child = document.createTextNode(child);

        element.appendChild(child);
    });

    return element;
}