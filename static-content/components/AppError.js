import {div, h1} from "../js/dom/domTags.js";

/**
 * The app error component.
 *
 * @param state The state of the app.
 * @param props The properties of the component.
 */
async function AppError(state, props) {
    return div(
        h1(`Error: ${props.code}`),
        h1(`Name: ${props.name}`),
        h1(`Description: ${props.description}`),
        props.extraInfo != null ? h1(`Extra info: ${props.extraInfo}`) : undefined
    );
}

/**
 * Checks if the given object is an AppError.
 * @param error
 * @returns true if the object is an AppError, false otherwise
 */
export function isAppError(error) {
    if (Object.keys(error).length === 3)
        return error.code !== undefined && error.name !== undefined && error.description !== undefined;
    else if (Object.keys(error).length === 4)
        return error.code !== undefined && error.name !== undefined && error.description !== undefined && error.extraInfo !== undefined;

    return false;
}

export default AppError;