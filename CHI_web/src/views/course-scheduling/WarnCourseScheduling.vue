<template>
  <div class="popup">
    <div class="popup-content">
      <p>{{ message }}</p>
      <div class="popup-buttons">
        <button @click="cancel">取消</button>
        <button @click="confirm">确定</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Popup',
  props: {
    message: {
      type: String
    }
  },
  methods: {
    cancel () {
      // 取消按钮点击事件
      // 在这里可以执行取消操作的逻辑
    },
    confirm () {
      // 确定按钮点击事件
      // 在这里可以执行确定操作的逻辑
    }
  }
}
</script>

<style scoped>
.popup {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999; /* 将弹窗组件置于最上层 */
}

.popup-content {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
}

.popup-buttons {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.popup-buttons button {
  margin-left: 10px;
}
</style>
