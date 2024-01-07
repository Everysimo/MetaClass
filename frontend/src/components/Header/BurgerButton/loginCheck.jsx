// loginCheck.js
const isLoggedIn = () => {
    return sessionStorage.getItem('isLoggedIn') === 'true';
};

export default isLoggedIn;
