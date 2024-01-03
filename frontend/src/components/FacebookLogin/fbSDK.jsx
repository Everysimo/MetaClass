const initializeFacebookSDK = () => {
    // Initialize Facebook SDK here
    window.fbAsyncInit = function() {
        window.FB.init({
            appId: '3381145492205390',
            autoLogAppEvents: true,
            xfbml: true,
            version: 'v12.0'
        });
    };

    // Load the SDK asynchronously
    (function(d, s, id) {
        var js, fjs = d.getElementsByTagName(s)[0];
        if (d.getElementById(id)) return;
        js = d.createElement(s); js.id = id;
        js.src = "https://connect.facebook.net/en_US/sdk.js";
        fjs.parentNode.insertBefore(js, fjs);
    }(document, 'script', 'facebook-jssdk'));
};

export default initializeFacebookSDK;
