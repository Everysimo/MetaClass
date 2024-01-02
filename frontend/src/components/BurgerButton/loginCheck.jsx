// loginCheck.js
const isLoggedIn = () => {
    return localStorage.getItem('isLoggedIn') === 'true';
};

export default isLoggedIn;
