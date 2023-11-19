<script>
import NavBar from "@/components/NavBar.vue";
export default {
  components: {
    NavBar
  }
}
</script>

<template>
  <div class="container">
    <NavBar/>
  </div>

  <router-view></router-view>
</template>

<style scoped>

.container {
    width: 75%;
    margin: 0 auto;
}
</style>
