<script>
export default {
  computed: {
    token() {
      return this.$store.getters.getToken;
    }
  }
}
</script>

<template>
  {{token}}
</template>

<style scoped>

</style>