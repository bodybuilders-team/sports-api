function User(state) {
    const element = document.createElement('h1');
    element.innerHTML = 'User ' + state.params.id;

    return element
}

export default User;
