import {userStore} from './store';
export async function fetchAPI(endpoint, method, data = null) {
    let options = {
        method: method
    }
    if(data) {
        options['body'] = JSON.stringify(data);
    }
    if (method !== "GET") {
        options["headers"] = {
            ...options["headers"],
            "Content-Type": "application/json"
        };

    }

    let token = await userStore.getters.getToken;
    console.log("Token " + token);
    if(token !== null) {
        options["headers"] = {
            ...options["headers"],
            "Authorization": token
        }
    }


    let url = "http://localhost:8080/api" + endpoint;
    let resposne = await fetch(url, options);
    return resposne;
}


// Check is saved token valid
export async function auth() {
    let response = await fetchAPI("/user/token", "GET");
    if(response.ok) {
        return true;
    }
    userStore.commit('setToken', null);
    return false;
}