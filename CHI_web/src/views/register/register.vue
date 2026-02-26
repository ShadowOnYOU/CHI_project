<template>
  <div class="register-container">
    <el-card class="box-card">
      <h2>注册</h2>
      <el-form
        :model="ruleForm"
        status-icon
        :rules="rules"
        ref="ruleForm"
        label-position="left"
        label-width="80px"
        class="demo-ruleForm"
      >
        <el-form-item label="用户名" prop="uname">
          <el-input v-model="ruleForm.uname"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="pass">
          <el-input
            type="password"
            v-model="ruleForm.pass"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="password">
          <el-input
            type="password"
            v-model="ruleForm.password"
            autocomplete="off"
          ></el-input>
        </el-form-item>
      </el-form>
      <div class="btnGroup">
        <el-button type="primary" @click="submitRegistration('ruleForm')">提交</el-button>
        <el-button @click="resetForm('ruleForm')">重置</el-button>
        <el-button @click="goBack">返回</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>

import axios from 'axios'
import { Message } from 'element-ui'

export default {
  data () {
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        if (this.ruleForm.checkPass !== '') {
          this.$refs.ruleForm.validateField('checkPass')
        }
        callback()
      }
    }
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.ruleForm.pass) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      ruleForm: {
        uname: '',
        pass: '',
        password: ''
      },
      rules: {
        uname: [
          {
            required: true,
            message: '用户名不能为空！',
            trigger: 'blur'
          }
        ],
        pass: [{
          required: true,
          validator: validatePass,
          trigger: 'blur'
        }],
        password: [
          {
            required: true,
            validator: validatePass2,
            trigger: 'blur'
          }
        ]
      }
    }
  },
  methods: {

    async submitRegistration (formName) {
      // 执行表单验证
      try {
        await this.$refs[formName].validate()
      } catch (error) {
        Message.error('格式错误')
        return false
      }
      axios.post('http://localhost:12010/user/register', {
        teacherName: this.ruleForm.uname,
        password: this.ruleForm.password
      })
        .then((response) => {
          console.log(response)
          if (response.data.code === '0') {
            Message.success(response.data.msg)
            console.log('注册成功')
            const teacherId = response.data.data.teacherId
            this.$store.commit('setLoggedIn', true)
            this.$store.commit('setUserName', this.ruleForm.uname)
            this.$store.commit('setTeacherId', teacherId)
            this.$router.push('/home')
          } else {
            Message.error(response.data.msg)
            console.error('注册失败')
          }
        })
        .catch((error) => {
          console.log(error)
        })
    },
    resetForm (formName) {
      this.$refs[formName].resetFields()
    },
    goBack () {
      this.$router.go(-1)
    }
  }
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.box-card {
  width: 400px;
  margin: auto;
}

.demo-ruleForm {
  margin: auto;
}
</style>
