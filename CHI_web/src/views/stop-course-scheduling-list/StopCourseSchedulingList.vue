<template>
  <el-card shadow="never">
    <div slot="header" class="clearfix">
      <span>停课列表</span>
    </div>
    <div>
      <update-stop-course-scheduling :visible="dialogVisible" @on-close="closeDialog" @on-success="handleSuccess"
                                     :id="selectedId"></update-stop-course-scheduling>
    </div>
    <div id="course-scheduling-list">
      <el-row>
        <el-form inline>
          <el-form-item label="停课日期">
            <el-date-picker v-model="params.dates"
                            size="mini"
                            :picker-options="datePickerOptions"
                            type="daterange"
                            value-format="yyyy-MM-dd"
                            range-separator="至"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期"
                            style="width: 240px;">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="上课时间">
            <el-time-select size="mini" class="form-item" v-model="params.attendTime"
                            :picker-options="timePickerOptions">
            </el-time-select>
          </el-form-item>
          <el-form-item label="下课时间">
            <el-time-select size="mini" class="form-item" v-model="params.finishTime"
                            :picker-options="timePickerOptions">
            </el-time-select>
          </el-form-item>
        </el-form>
      </el-row>
    </div>
    <el-row>
      <div style="float: right; margin: 10px 0;">
        <el-button type="primary" size="small" @click="search">查询</el-button>
        <el-button type="primary" size="small" @click="reset">重置</el-button>
      </div>
      <el-dialog :visible="popVisible" @close="closePop">
        <span class="big-bold"> 停课信息</span>
        <br/><br/>
        <span class="indent"> 停课课程：{{ exportMessage.name }}</span>
        <br/>
        <span class="indent"> 原定日期：  {{ exportMessage.date }} </span>
        <br/>
        <span
          class="indent"> 上课时间： {{ exportMessage.attend_time }}   </span>
        <br/>
        <span
          class="indent"> 下课时间： {{ exportMessage.finish_time }}   </span>
        <br/>
        <span
          class="indent"> 原定教室： {{ exportMessage.classroom_name }}   </span>
        <br/>
        <span slot="footer" class="dialog-footer">
          <el-button type="success" @click="copyToClipboard">复制</el-button>
          <el-button type="primary" @click="closePop">返回</el-button>
      </span>
      </el-dialog>
      <el-table stripe border :data="data" @selection-change="handleSelectionChange" header-row-class-name="stop">
        <!--        这边需要对距离进行适当的调整-->
        <el-table-column type="index" label="序号" width="50" align="center"></el-table-column>
        <el-table-column prop="date" label="日期" width="200" align="center"></el-table-column>
        <el-table-column prop="attend_time" label="上课时间" width="170" align="center"></el-table-column>
        <el-table-column prop="finish_time" label="下课时间" width="170" align="center"></el-table-column>
        <el-table-column prop="classroom_name" label="教室" width="120" align="center"></el-table-column>
        <el-table-column prop="course_name" label="课程" width="200" align="center"></el-table-column>
        <el-table-column label="操作"  align="center">
          <template slot-scope="scope">
            <div class="back_button">
              <el-button type="primary" size="small" @click="handleButtonClick(scope.row)">补课</el-button>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="停课信息导出" align="center">
          <template slot-scope="scope">
            <div class="back_button">
              <el-button type="primary" size="small" @click="button_stop_export(scope.row)" class="green-button">导出</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
  </el-card>
</template>
<style>

</style>
<script>
import { mapActions, mapState } from 'vuex'
import moment from 'moment'
import axios from 'axios'
import { Message } from 'element-ui'

export default {
  name: 'StopCourseSchedulingList',
  data () {
    return {
      course_id: -100,
      dialogVisible: false,
      selectedId: null,
      currentVisible: false,
      data: [],
      classroomData: [],
      courseData: [],
      teacherData: [],
      params: {},
      idList: [],
      datePickerOptions: {
        firstDayOfWeek: 1
      },
      select_data: {
        date: '',
        attendTime: '',
        finishTime: '',
        classroom: '',
        course_name: '',
        teacher_name: ''
      },
      timePickerOptions: {
        start: '08:00',
        step: '00:' + '10',
        end: '21:30'
      },
      popVisible: false,
      exportMessage: {
        date: '',
        attend_time: '',
        finish_time: '',
        classroom_name: '',
        name: '',
        mergeInfo: ''
      }
    }
  },
  computed: {
    ...mapState(['userName'])
  },
  methods: {
    ...mapActions(['GetCancleList', 'handleButtonClick', 'GetClassroomRefList', 'GetCourseRefList', 'GetTeacherRefList', 'GetCourseSchedulingList', 'RemoveCourseSchedulingByIdList']),
    showDialog () {
      this.dialogVisible = true
    },
    closeDialog () {
      this.dialogVisible = false
    },
    handleSuccess () {
      // 处理成功后的逻辑
    },
    handleButtonClick (row) {
      axios.post('http://localhost:12010/stop-course-scheduling-list/id', null, {
        params: {
          date: row.date,
          attendTime: row.attend_time,
          classroomName: row.classroom_name
        }
      })
        .then((response) => {
          this.data.course_id = response.data.data
          console.log(this.data.course_id)
          this.$router.push({
            path: '/course-scheduling',
            query: {
              id: this.data.course_id.toString(),
              date: row.date
            }
          })
        })
        .catch((error) => {
          console.log(error)
        })
      this.dialogVisible = false
    },
    init () {
      this.GetClassroomRefList().then(res => {
        if (res) {
          this.classroomData = res
        }
      }).catch(() => {
      })
      this.GetCourseRefList().then(res => {
        if (res) {
          this.courseData = res
        }
      }).catch(() => {
      })
      this.GetTeacherRefList().then(res => {
        if (res) {
          this.teacherData = res
        }
      }).catch(() => {
      })
      this.params.startDate = moment().startOf('month').format('YYYY-MM-DD')
      this.params.endDate = moment().endOf('month').format('YYYY-MM-DD')
      this.$set(this.params, 'dates', [this.params.startDate, this.params.endDate])
      this.search()
    },
    button_stop_export (row) {
      this.exportMessage.name = row.course_name
      this.exportMessage.date = row.date
      this.exportMessage.attend_time = row.attend_time
      this.exportMessage.finish_time = row.finish_time
      this.exportMessage.classroom_name = row.classroom_name
      this.exportMessage.mergeInfo = '停课通知\n' + '停课课程： ' + row.course_name + '\n' + '原定日期： ' + row.date + '  ' + '\n' + '上课时间： ' + row.attend_time + '\n' + '下课时间： ' + ' ' + row.finish_time + '\n' + '原定教室： ' + row.classroom_name + '\n'
      this.popVisible = true
    },
    copyToClipboard () {
      const el = document.createElement('textarea')
      el.value = this.exportMessage.mergeInfo
      document.body.appendChild(el)
      el.select()
      document.execCommand('copy')
      document.body.removeChild(el)
      // 可以添加适当的提示或处理复制成功的逻辑
      Message.success('已复制到剪贴板')
    },
    closePop () {
      this.popVisible = false
    },
    search () {
      this.params.teacherName = this.$store.state.userName
      if (this.params.dates && this.params.dates.length > 0) {
        this.params.date = this.params.dates[0]
        this.params.date1 = this.params.dates[1]
      }
      if (this.params.attendTime) {
        this.params.attend_time = moment('2023-12-31' + ' ' + this.params.attendTime).format('HH:mm:ss')
      }
      if (this.params.finishTime) {
        this.params.finish_time = moment('2023-12-31' + ' ' + this.params.finishTime).format('HH:mm:ss')
      }
      this.GetCancleList(this.params).then(res => {
        if (res) {
          console.log(res)
          this.data = res
        }
      }).catch(() => {
      })
    },
    reset () {
      this.params = {}
      this.params.startDate = moment().startOf('month').format('YYYY-MM-DD')
      this.params.endDate = moment().endOf('month').format('YYYY-MM-DD')
      this.$set(this.params, 'dates', [this.params.startDate, this.params.endDate])
      this.search()
    },
    handleSelectionChange (val) {
      this.idList = []
      if (val && val.length > 0) {
        val.forEach(item => {
          this.idList.push(item.id)
        })
      }
    }
  },
  mounted () {
    this.init()
  }
}
</script>

<style>
.back_button {
  text-align: right;
  margin-right: 0;
}

#course-scheduling-list .el-form-item {
  margin-bottom: 0;
}

#course-scheduling-list .form-item {
  width: 240px;
}

.el-card {
  margin-left: 100px;
}

.indent {
  display: block;
  margin-left: 20px;
}

.big-bold {
  font-size: 18px;
  font-weight: bold;
}

.back_button {
  display: flex;
  justify-content: center;
}

</style>
