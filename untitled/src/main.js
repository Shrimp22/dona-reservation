import './assets/main.css'

import { createApp } from 'vue';
import { createRouter, createWebHistory } from 'vue-router';
import App from './App.vue';
import Vuex from "vuex";

import Register from "@/views/Register.vue";
import Login from "@/views/Login.vue";
import Home from "@/views/Home.vue";
import Reservation from '@/views/Reservation.vue';
import {userStore} from "@/utils/store";
import AboutUs from "@/views/AboutUs.vue";

const router = createRouter({
    history: createWebHistory(),
    routes: [
        { path: '/register', component: Register },
        {path: '/login', component: Login},
        { path: '/reservation', component: Reservation },
        { path: '/', component: Home},
        {path: '/aboutus', component: AboutUs}
    ]
});

const app = createApp(App)
app.use(router);
app.use(userStore);
app.mount("#app");
