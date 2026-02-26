<template>
  <el-container>
    <el-header>
      <div>
        <span id="sgh">南京大学软件学院的调课系统</span>
        <div class="login-status">
          <span v-if="!isLoggedIn" class="welcome-message">请登录</span>
          <span v-if="isLoggedIn" class="welcome-message">欢迎，{{this.$store.state.userName}}{{ username }}</span>
          <img class="user-avatar" :src="userAvatar" alt="User Avatar" @click="toggleDropdown">
          <el-dropdown-menu :placement="dropdownPlacement" v-show="isDropdownVisible" @hide="closeDropdown" style="position: absolute; top: 55px; left: 1280px;">
            <el-dropdown-item v-if="isLoggedIn">用户名: {{this.$store.state.userName}}</el-dropdown-item>
            <el-dropdown-item v-if="!isLoggedIn">尚未登录</el-dropdown-item>
            <el-dropdown-item divided></el-dropdown-item>
            <el-dropdown-item v-if="!isLoggedIn" command="login" icon="el-icon-switch-button">
              <el-button class="transparent-button" @click="login">登录</el-button>
            </el-dropdown-item>
            <el-dropdown-item v-if="isLoggedIn" command="logout" icon="el-icon-switch-button">
              <el-button class="transparent-button" @click="logout">退出</el-button>
            </el-dropdown-item>
          </el-dropdown-menu>
        </div>
      </div>
    </el-header>
    <el-aside width="150px" class="el-aside">
      <div class="image-container">
        <img src="../resource/final_logo.png" alt="Your Image" />
      </div>
      <el-menu
        router
        :default-active="currentNavName"

        active-text-color="#668ca3"
        :unique-opened="true"
        @select="handleSelect"
        class="menu">
        <el-menu-item v-for="item in navData"
                      :key="item.code"
                      :index="item.path"
                      :route="item.path"

                      :class="{'is-active': item.isActive}">
          {{ item.name }}
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-main style="padding: 20px 100px;">
      <router-view/>
    </el-main>
  </el-container>
</template>

<script>
import Vue from 'vue'
import { mapState } from 'vuex'
import { Message } from 'element-ui'

export default {
  name: 'Main',
  components: {
  },
  data () {
    return {
      dropdownPlacement: 'bottom-end',
      // TODO bottomend需要修改
      userAvatar: 'https://raw.githubusercontent.com/ShadowOnYOU/images/main/test202312280046806.png', // 根据实际情况设置用户头像路径
      isDropdownVisible: false, // 控制下拉窗口的显示状态
      navData: [
        {
          code: '01',
          path: '/home',
          name: '主页',
          isActive: false
        },
        {
          code: '02',
          path: '/course-scheduling',
          name: '调课日历',
          isActive: true
        },
        {
          code: '03',
          path: '/course-scheduling-list',
          name: '调课列表',
          isActive: false
        },
        {
          code: '04',
          path: '/stop-course-scheduling-list',
          name: '停课列表',
          isActive: false
        }
      ],
      currentNavName: ''
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
    },
    handleSelect (key, keyPath) {
      this.currentNavName = key
      this.setInactiveStateForTabs()
      const selectedRoute = this.navData.find(item => item.code === key)
      if (selectedRoute) {
        Vue.set(selectedRoute, 'isActive', true)
      }
    },
    setInactiveStateForTabs () {
      this.navData.forEach(item => {
        Vue.set(item, 'isActive', false)
      })
    }
  },
  created () {
    this.currentNavName = this.$route.path
    this.navData.forEach((item) => {
      Vue.set(item, 'isActive', item.path === this.$route.path)
    })
  },
  watch: {
    '$route' (to, from) {
      this.currentNavName = to.path
    }
  }
}
</script>
<style scoped>
.menu {
  margin-top: 150px; /* 调整上边距的值 */
  background-color: #EAEDF1;
}
.el-aside {
  position: fixed;
  background-color: #668ca3;
  top: 0;
  left: 0;
  bottom: 0;
  z-index: 2; /* 调整侧边栏的 z-index 值 */
}
.el-menu-item.is-active {
  color: #fff !important;
  background: #668ca3 !important;

}
.el-header{
  background-color: #668ca3;
  display: flex;
  justify-content: space-between;
  padding-left: 170px;
  align-items:center;
  color:#fff;
  font-size: 20px;
  >div{
    display:flex;
    align-items:center;
    >span{
      margin-right: 70ch;
    }
  }
}
.image-container {
  position: absolute;
  top: 10px;
  left: 10px;
  width: 80%;
  height: 120px; /* 调整图片容器的高度 */
  z-index: 1; /* 图片容器的 z-index 值较低，以便在侧边栏之下 */
}
.image-container img {
  width: 100%;
  height: 100%;
  object-fit: cover; /* 调整图片的适应方式 */
}
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
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0);
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
