/**
 * Creates an HTML element.
 *
 * @param tag element tag
 * @param attributes element attributes
 * @param children element children
 * @returns created element
 */
export async function createElement(tag, attributes, ...children) {
    const element = document.createElement(tag);

    attributes = await attributes;

    if (isElement(attributes) || typeof attributes === "string")
        appendChild(element, attributes);
    else if (attributes != null && typeof attributes === "object")
        setAttributes(element, attributes);

    for (let child of children) {
        if (child != null)
            appendChild(element, await child);
    }

    return element;
}


function appendChild(element, child) {
    if (typeof child === "string")
        child = document.createTextNode(child);

    element.appendChild(child);
}

function setAttributes(element, attributes) {
    for (const attribute in attributes) {
        switch (attribute) {
            case "onClick":
                element.addEventListener("click", attributes[attribute]);
                break;
            case "onSubmit":
                element.addEventListener("submit", attributes[attribute]);
                break;
            default:
                element.setAttribute(attribute, attributes[attribute]);
        }
    }
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