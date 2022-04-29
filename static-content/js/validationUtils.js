/**
 * Validates an object based on a shema.
 *
 * @param {Object} object
 * @param {Object} schema
 * @returns {{isValid:boolean, error:string=, details:any=}}
 */
export function validate(object, schema) {
    if (object == null)
        return {isValid: false, error: "Cbject cannot null"};

    if (schema == null)
        return {isValid: false, error: "Schema cannot null"};

    if (typeof schema === "string") {
        if (typeof object !== schema)
            return {isValid: false, error: "Object is not of type " + schema};

        return {isValid: true};
    }

    // Verifies if all object properties exist in schema
    for (const prop in object) {
        if (schema[prop] == null)
            return {isValid: false, error: "Object property " + prop + " does not exist in schema"};
    }

    for (const prop in schema) {
        const propValue = object[prop];
        const propSchema = schema[prop];

        if (propSchema == null)
            return {isValid: false, error: "Schema does not contain property " + prop};

        const {type, required} = propSchema;

        if (required && propValue === undefined)
            return {isValid: false, error: "Property " + prop + " is required"};

        if (!required && propValue === undefined)
            continue;

        if (typeof type === "string") {
            if (typeof propValue !== type)
                return {isValid: false, error: "Property " + prop + " is not of type " + type};

        } else if (typeof type === "object") {
            const result = validate(propValue, type);

            if (!result.isValid)
                return {isValid: false, error: "Property " + prop + " is not valid", details: result};
        } else
            return {isValid: false, error: "Schema property type " + prop + " is not of type string or object"};
    }

    return {isValid: true};
}
