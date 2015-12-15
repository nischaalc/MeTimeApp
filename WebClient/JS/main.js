function onSuccess(googleUser) {
    "use strict";
    console.log(googleUser);
    window.localStorage.setItem("googleUser.object", googleUser.Ka.access_token);
    window.location.href = "http://nischaalc.github.io/MeTime/users?name=" + googleUser.getBasicProfile().getName();
}

function renderButton() {
    "use strict";
    gapi.signin2.render('googleIn', {
        'scope': 'https://www.googleapis.com/auth/calendar profile',
        'width': 200,
        'height': 50,
        'longtitle': true,
        'theme': 'dark',
        'cookiepolicy': 'single_host_origin',
        'onsuccess': onSuccess
    });
}
