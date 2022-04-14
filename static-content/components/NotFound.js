function NotFound(state) {
    const element = document.createElement('h1');
    element.innerHTML = 'Path ' + state.path + " not found";
    return element
}

export default NotFound;
