<script>
import Register from "@/views/Register.vue";
import {auth} from "@/utils/fetchAPI";

const navItems = [
  {
    name: "Home",
    path: "/"
  },
  {
    name: "About us",
    path: "/aboutus"
  }
];

const adminNav = [
  {
    name: "Reservation",
    path: "/reservation",
  }

];

export default {
  computed: {
    isAuth() {
      return this.$store.getters.isAuthenticated
    }
  },
  data() {
    return {
      navItems: navItems,
      activePage: '',
      adminNav
    }
  },
  async created() {
    await auth();
  }
}

</script>

<template>
  <div class="navbar">
    <router-link  v-for="item in navItems" :to="item.path">{{item.name}}</router-link>
    <template v-if="isAuth === false">
      <router-link to="/login">Login</router-link>
      <router-link to="/register">Register</router-link>
    </template>
    <template v-else>
      <router-link to="/reservation">Reservation</router-link>
    </template>
  </div>
{{isAuth}}
</template>

<style scoped>
  .navbar {
    display: flex;
    justify-content: space-between;
    padding: 0 1rem;
  }

  .navbar a {
    color: black;
    text-decoration: none;
    padding: 1rem;
    border-radius: 1rem;
  }

  .navbar a:hover {
    color: white;
    background: pink;
    transition: 300ms ease;
  }

  .active {
    color: white !important;
    background: pink;
  }

</style>