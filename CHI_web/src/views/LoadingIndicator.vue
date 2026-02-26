<template>
  <div v-if="isLoading" class="loading-indicator">
    <!-- 这里可以使用任何你喜欢的加载动画或文本 -->
    正在加载...
  </div>
</template>

<script>
export default {
  props: {
    isLoading: Boolean
  }
}
</script>

<style scoped>
.loading-indicator {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: rgba(255, 255, 255, 0.8);
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}
</style>
