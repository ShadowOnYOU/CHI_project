登录：
<template>
  <div class="login-container">
    <div class="background-panel">
      <div class="logo-container">
        <!-- 这里添加你的 logo 图片 -->
        <img src="./logo.png" alt="Logo" class="logo-image">
        <div class="title">南京大学软件学院调课系统</div>
      </div>
    <img src="././picture.png" alt="登录图片" class="login-image">
    <el-card class="box-card">
      <h2>登录</h2>
      <el-form
        :model="ruleForm"
        status-icon
        :rules="rules"
        ref="ruleForm"
        label-position="left"
        label-width="70px"
        class="login-from"
      >
        <el-form-item label="用户名" prop="uname" style="width: 350px;">
          <el-input v-model="ruleForm.uname"></el-input>
        </el-form-item>
        <el-form-item label=" 密码 " prop="password" style="width: 350px;">
          <el-input
            type="password"
            v-model="ruleForm.password"
            autocomplete="off"
          ></el-input>
        </el-form-item>
      </el-form>
      <div class="btnGroup">
        <el-button type="primary" @click="submitLogin('ruleForm')">登录</el-button>
        <el-button @click="resetForm('ruleForm')">重置</el-button>
      </div>
    </el-card>
  </div>
  </div>
</template>

<script>
import { Message } from 'element-ui'
import axios from 'axios'

export default {
  data () {
    return {
      ruleForm: {
        uname: '',
        password: ''
      },
      rules: {
        uname: [
          { required: true, message: '用户名不能为空！', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '密码不能为空！', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    async submitLogin (formName) {
      // 执行表单验证
      try {
        await this.$refs[formName].validate()
      } catch (error) {
        Message.error('格式错误')
        return false
      }
      axios.post('http://localhost:12010/user/login', null, {
        params: {
          teacherName: this.ruleForm.uname,
          password: this.ruleForm.password
        }
      })
        .then((response) => {
          console.log(response)
          if (response.data.code === '0') {
            Message.success(response.data.msg)
            console.log('登录成功')
            const teacherId = response.data.data.teacherId
            this.$store.commit('setLoggedIn', true)
            this.$store.commit('setUserName', this.ruleForm.uname)
            this.$store.commit('setTeacherId', teacherId)
            this.$router.push('/home')
          } else {
            Message.error(response.data.msg)
            console.error('登录失败')
          }
        })
        .catch((error) => {
          console.log(error)
        })
    },
    resetForm (formName) {
      this.$refs[formName].resetFields()
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(to right, #409eff 50%, #ecf5ff 50%);
}

.background-panel {
  display: flex;  /* 使用 flex 布局 */
  align-items: center;  /* 垂直居中对齐 */
  background: rgba(255, 255, 255, 0.85); /* 半透明白色背景 */
  border-radius: 10px; /* 圆角 */
  overflow: hidden; /* 隐藏溢出的部分，以确保圆角生效 */
  width: 1300px;  /* 设置宽度为一个具体的值，你可以根据需要调整 */
  height: 650px;
  margin: 0 auto;  /* 居中显示 */
}

.logo-container {
  position: absolute; /* 设置绝对定位 */
  top: 100px; /* 距离顶部的距离，根据需要调整 */
  left: 125px; /* 距离左侧的距离，根据需要调整 */
}

.logo-image {
  width: 50px; /* 调整 logo 的宽度，根据需要调整 */
  height: auto;
}

.title {
  margin-top: -50px;
  margin-left: 80px; /* 根据需要调整标题与 logo 之间的距离 */
  font-size: 30px; /* 根据需要调整标题字体大小 */
  font-weight: bold;
  font-family: 'Arial', sans-serif;
}

.box-card {
  width: 450px;
  height: 400px;
  margin-left: 40px;
}

.login-from {
  margin: auto;
}

h2 {
  text-align: center;
  font-family: Arial, sans-serif;
  font-size: 36px;
}

.btnGroup {
  display: flex;
  justify-content: center;
}

.login-image {
  width: 500px; /* 调整图片的宽度 */
  height: auto; /* 根据需要调整图片的高度 */
  margin-right: 100px;
  margin-left: 100px;
}
</style>
