<script>
import {fetchAPI} from "@/utils/fetchAPI";

export default {
  data() {
    return {
      formData: {
        firstName: '',
        lastName: '',
        phone: '',
        email: '',
        confirmPassword: '',
        password: ''
      }
    }
  },
  methods: {
    async register() {
        if(this.formData.password != this.formData.confirmPassword) {
            return false;
        }
        let response = await fetchAPI("/user", "POST", this.formData);
        response = await response.json();
        return true;
    }
  }
}

</script>

<template>
<h1>Register Form</h1>
  <form @submit.prevent="register">
    <label for="firstName">First Name</label>
    <input type="text" v-model="formData.firstName">
    <br>
    <label for="lastName">Last Name</label>
    <input type="text" v-model="formData.lastName">
    <br>
    <label for="phone">Phone</label>
    <input type="text" v-model="formData.phone">
    <br>
    <label for="email">Email</label>
    <input type="text" v-model="formData.email">
    <br>
    <label for="password">password</label>
    <input type="password" v-model="formData.password">
    <br>
    <label for="confirmPassword">Confirm Password</label>
    <input type="password" v-model="formData.confirmPassword">
    <br>
    <button type="submit">Submit</button>

  </form>
</template>

<style scoped>

</style>