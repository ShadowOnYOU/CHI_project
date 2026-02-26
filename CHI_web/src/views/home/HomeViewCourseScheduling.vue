<template>
  <div>
    <el-dialog title="详情" width="600px"
               :visible.sync="dialogVisible"
               class="view-title"
               :before-close="handleClose" >
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
        <el-popconfirm title="确定要进行修改吗？" @onConfirm="route">
          <el-button type="danger" slot="reference" style="margin-right: 10px;">修改</el-button>
        </el-popconfirm>
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
      form: {}
    }
  },
  methods: {
    ...mapActions(['GetCourseSchedulingById']),
    search () {
      this.GetCourseSchedulingById(this.id).then((res) => {
        this.form = res
        this.form.attendTime = moment(res.attendTime, 'HH:mm:ss').format('HH:mm')
        this.form.finishTime = moment(res.finishTime, 'HH:mm:ss').format('HH:mm')
      }).catch(() => {
      })
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
      this.$router.push({
        path: '/course-scheduling',
        query: {
          id: this.form.id,
          date: this.form.date
        }
      })
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
