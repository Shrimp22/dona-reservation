<script>
import {fetchAPI} from "@/utils/fetchAPI";

export default {
  data() {
    return {
      formData: {
        email: null,
        password: null
      }
    }
  },
  methods: {
    async login() {
      let request = await fetchAPI("/user/login", "POST", this.formData);
      request = await request.json();
      let token = request['detail'];
      this.$store.commit('setToken', token);
      this.$router.push({name: "Home"});
    }
  }
}
</script>

<template>
  <div class="form-holder">
    <h1>Login form</h1>
    <form @submit.prevent="login">
      <div>
        <label for="email">Email</label>
        <input type="email" v-model="formData.email">
      </div>
      <div>
        <label for="password">Password</label>
        <input type="password" v-model="formData.password">
      </div>
      <router-link to="/forgotpassword" class="forgotpassword">Zaboravili ste lozinku?</router-link>
      <button type="submit">Login</button>
    </form>
  </div>
</template>

<style scoped>
  

  .forgotpassword {
    margin-top: 0.2rem;
  }

  .forgotpassword:hover {
    color: deeppink;
    transition: 300ms ease;
  }
</style>