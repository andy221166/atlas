<template>
  <div :class="`alert alert-${type}`" role="alert">
    {{ message }}
  </div>
</template>

<script>
export default {
  name: 'Alert',
  props: {
    message: {
      type: String,
      required: true
    },
    type: {
      type: String,
      default: 'info' // Sets a default alert type
    }
  }
}
</script>

<style scoped>
/* Add any additional styling if needed */
</style>
