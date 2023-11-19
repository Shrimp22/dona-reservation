<script>
import {fetchAPI} from "@/utils/fetchAPI";
import {userStore} from "@/utils/store";

export default {
  data() {
    return {
      formData: {
        email: '',
        password: ''
      }
    }
  },
  methods: {
    async login() {
      let request = await fetchAPI("/user/login", "POST", this.formData);
      request = await request.json();
      let token = request['detail'];
      this.$store.commit('setToken', token);
    }
  }
}
</script>

<template>
  <h1>Login form</h1>
  <form @submit.prevent="login">
    <label for="email">Email</label>
    <input type="email" v-model="formData.email">
    <br>
    <label for="password">Password</label>
    <input type="password" v-model="formData.password">
    <br>
    <button type="submit">Login</button>
  </form>
</template>

<style scoped>

</style>