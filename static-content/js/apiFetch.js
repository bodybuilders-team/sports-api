import {API_BASE_URL} from "./config.js";
import {isAppError} from "../components/AppError.js";


async function apiFetch(endpoint) {
    try {
        const res = await fetch(`${API_BASE_URL}/${endpoint}`);

        const json = await res.json();

        if (res.ok)
            return json;

        throw json;
    } catch (err) {
        if (isAppError(err))
            throw err;

        throw new Error(err)
    }
}

export default apiFetch;