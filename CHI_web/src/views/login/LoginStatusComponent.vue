<template>
  <el-row>
    <el-col>
      <!-- 在这里添加显示登录情况的状态栏    -->
      <div class="login-status">
        <span v-if="!isLoggedIn" class="welcome-message">请登录</span>
        <span v-if="isLoggedIn" class="welcome-message">欢迎，{{this.$store.state.userName}}{{ username }}</span>
        <img class="user-avatar" :src="userAvatar" alt="User Avatar" @click="toggleDropdown">
        <el-dropdown-menu :placement="dropdownPlacement" v-show="isDropdownVisible" @hide="closeDropdown" >
          <el-dropdown-item v-if="isLoggedIn">用户名: {{this.$store.state.userName}}</el-dropdown-item>
          <el-dropdown-item v-if="!isLoggedIn">尚未登录</el-dropdown-item>
          <el-dropdown-item divided></el-dropdown-item>
          <el-dropdown-item v-if="!isLoggedIn" command="login" icon="el-icon-switch-button">
            <el-button class="transparent-button" @click="login">登录</el-button>
          </el-dropdown-item>
          <el-dropdown-item v-if="isLoggedIn" command="logout" icon="el-icon-switch-button" style="position: absolute; top: 3000ch; left: 1190ch;">
            <el-button class="transparent-button" @click="logout">退出</el-button>
          </el-dropdown-item>
        </el-dropdown-menu>
      </div>
    </el-col>
  </el-row>
</template>
<style>
.login-status {
  text-align: right;
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.transparent-button {
  border: 1px solid transparent;
  background-color: transparent;
  color: #333;
}

.login-status {
  text-align: right;
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.welcome-message {
  font-weight: bold;
  margin-right: 10px;
}

.user-avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
}

</style>

<script>
// import Avatar from 'vue-avatar'
import { mapState } from 'vuex'
import { Message } from 'element-ui'

export default {
  data () {
    return {
      dropdownPlacement: 'bottom-end',
      // TODO bottomend需要修改
      userAvatar: 'https://raw.githubusercontent.com/ShadowOnYOU/images/main/setting.png', // 根据实际情况设置用户头像路径
      isDropdownVisible: false // 控制下拉窗口的显示状态
    }
  },
  computed: {
    ...mapState(['isLoggedIn'], ['userName'])
  },
  methods: {
    toggleDropdown () {
      this.isDropdownVisible = !this.isDropdownVisible
    },
    closeDropdown () {
      this.isDropdownVisible = false
    },
    login () {
      this.$router.push('/login').catch(err => {
        console.error('路由跳转失败:', err)
      })
    },
    logout () {
      this.$store.commit('setLoggedIn', false)
      this.$router.push('/login').catch(err => {
        console.error('路由跳转失败:', err)
      })
      Message.info('已退出')
    }

  }
}
</script>
