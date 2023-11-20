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

      <button type="submit">Login</button>
    </form>
  </div>
</template>

<style scoped>
  .form-holder {
    width: 75%;
    margin: 0 auto;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }

  .form-holder form {
    width: 65%;
    justify-content: center;
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  .form-holder div {
    display: flex;
    flex-direction: column;
    width: 100%;
    align-items: center;
  }

  .form-holder input {
    width: 50%;
    height: 30px;
    border-radius: 1rem;
    border: 1px solid black;
    padding: 0 1rem;
  }

  .form-holder input:focus {
    outline: none;
    outline: 1px solid deeppink;
  }

  .form-holder button {
    margin-top: 1rem;
    width: 50%;
    border: 1px solid deeppink;
    background: pink;
    color: white;
    height: 30px;
    border-radius: 1rem;
  }

  .form-holder button:hover {
    background: white;
    color:deeppink;
    transition: 300ms ease;
  }
</style>