<template>
  <div>
    <el-dialog title="详情" width="600px"
               :visible.sync="dialogVisible"
               class="view-title"
               :before-close="handleClose">
      <el-form ref="form" :model="form" inline label-width="80px" class="tams-form-container">
        <el-form-item label="授课日期">
          <el-input v-model="form.date" class="tams-form-item gray-input" readonly></el-input>
        </el-form-item>
        <el-form-item label="所在教室">
          <el-input v-model="form.classroomName" class="tams-form-item gray-input" readonly></el-input>
        </el-form-item>
        <el-form-item label="教学课程">
          <el-input v-model="form.courseName" class="tams-form-item gray-input" readonly></el-input>
        </el-form-item>
        <el-form-item label="授课老师">
          <el-input v-model="form.teacherName" class="tams-form-item gray-input" readonly></el-input>
        </el-form-item>
        <el-form-item label="上课时间">
          <el-input v-model="form.attendTime" class="tams-form-item gray-input" readonly></el-input>
        </el-form-item>
        <el-form-item label="下课时间">
          <el-input v-model="form.finishTime" class="tams-form-item gray-input" readonly></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="stopClass" style="margin-right: 10px;">停课</el-button>
        <el-button type="danger" slot="reference" style="margin-right: 10px;" @click="showupdate" >修改</el-button>
        <el-button type="primary" @click="close">返回</el-button>
      </div>
    </el-dialog>
    <UpdateCourseScheduling :visible="updateCourseSchedulingVisible"
                            :id="id"
                            @on-success="updateSuccess"
                            @on-close="updateCourseSchedulingVisible=false"></UpdateCourseScheduling>
  </div>
</template>
<style>
.gray-input .el-input__inner {
  border-color: white;
}
.view-title {
  font-weight: bold;
}
</style>
<script>
import moment from 'moment'
import { mapActions } from 'vuex'
import UpdateCourseScheduling from '@/views/course-scheduling/UpdateCourseScheduling'
import axios from 'axios'
import { Message } from 'element-ui'

export default {
  name: 'ViewCourseScheduling',
  components: { UpdateCourseScheduling },
  props: {
    visible: {
      type: Boolean
    },
    id: [Number, String]
  },
  data () {
    return {
      dialogVisible: false,
      updateCourseSchedulingVisible: false,
      form: {},
      select_data: {
        date: '',
        attendTime: '',
        finishTime: '',
        classroom_name: '',
        course_name: '',
        teacherName: ''
      },
      dataNewDTO: {
        course_name: '',
        teacherName: '',
        date: '',
        attend_time: '',
        finish_time: '',
        classroom_name: ''
      }
    }
  },
  methods: {
    ...mapActions(['GetCourseSchedulingById']),
    search () {
      this.GetCourseSchedulingById(this.id).then((res) => {
        this.form = res
        this.select_data.date = this.form.date
        this.select_data.attendTime = this.form.attendTime
        this.select_data.finishTime = this.form.finishTime
        this.select_data.classroom_name = this.form.classroomName
        this.select_data.course_name = this.form.courseName
        this.select_data.teacherName = this.form.teacherName
        this.form.attendTime = moment(res.attendTime, 'HH:mm:ss').format('HH:mm')
        this.form.finishTime = moment(res.finishTime, 'HH:mm:ss').format('HH:mm')
      }).catch(() => {
      })
    },
    stopClass () {
      console.log('****************************')
      this.dataNewDTO.date = this.select_data.date
      this.dataNewDTO.attend_time = this.select_data.attendTime
      this.dataNewDTO.finish_time = this.select_data.finishTime
      this.dataNewDTO.classroom_name = this.select_data.classroom_name
      this.dataNewDTO.teacherName = this.select_data.teacherName
      this.dataNewDTO.course_name = this.select_data.course_name
      console.log('*******************')
      console.log(this.dataNewDTO)
      this.addData(this.dataNewDTO)
    },
    async addData (dataNewDTO) {
      try {
        const service = axios.create({
          baseURL: 'http://localhost:12011', // 后端的基础地址
          timeout: 5000, // 请求超时时间
          headers: {
            'Content-Type': 'application/json' // 请求头的类型
          }
        })
        // 发送 post 请求，传递 changeNewDTO 对象
        const response = await service.post('/cancle/saveData', dataNewDTO)
        // 获取后端返回的数据
        const data = response.data
        // 判断请求是否成功
        if (data.code === 200) {
          // 请求成功，打印数据或做其他操作
          console.log(data.data)
        } else {
          // 请求失败，抛出错误或做其他操作
          console.log(Error(data.message))
        }
      } catch (error) {
        // 捕获异常，打印错误或做其他操作
        console.error(error)
      }
      this.dialogVisible = false
      Message.success('停课已提交审批')
    },
    showupdate () {
      this.updateCourseSchedulingVisible = true
    },
    handleClose (done) {
      this.$refs.form.resetFields()
      this.$emit('on-close')
      done()
    },
    close () {
      this.$refs.form.resetFields()
      this.$emit('on-close')
      this.dialogVisible = false
    },
    updateSuccess () {
      this.$emit('on-success')
      this.search()
      this.updateCourseSchedulingVisible = false
    },
    route () {
      this.$router.push('/course-scheduling')
    }
  },
  watch: {
    visible (val) {
      if (val) {
        this.search()
        this.dialogVisible = val
      }
    }
  }
}
</script>
