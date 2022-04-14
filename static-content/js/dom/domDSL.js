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

    if (isElement(attributes) || typeof attributes === "string") {
        if (typeof attributes === "string")
            attributes = document.createTextNode(attributes);

        element.appendChild(attributes);
    } else if (attributes != null && typeof attributes === "object") {
        for (const attribute in attributes) {
            switch (attribute) {
                case "onClick":
                    element.addEventListener("click", attributes[attribute]);
                    break;
                default:
                    element.setAttribute(attribute, attributes[attribute]);
            }
        }
    }

    children.forEach((child) => {
        if (typeof child === "string")
            child = document.createTextNode(child);

        element.appendChild(child);
    });

    return element;
}

/**
 * Checks if an object is a DOM element.
 * @param obj object to check
 * @returns true if it is a DOM element; false otherwise
 */
function isElement(obj) {
    return (
        typeof HTMLElement === "object" ? obj instanceof HTMLElement :
            obj && typeof obj === "object" && obj !== null && obj.nodeType === 1 && typeof obj.nodeName === "string"
    );
}