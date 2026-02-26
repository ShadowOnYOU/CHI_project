<template>
  <el-card shadow="never">
    <div slot="header" class="clearfix">
      <span>调课列表</span>
    </div>
    <div id="course-scheduling-list">
      <el-row>
        <el-form inline>
          <el-form-item label="原日期">
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
          <el-form-item label="原上课时间">
            <el-time-select size="mini" class="form-item" v-model="params.attendTime"
                            :picker-options="timePickerOptions">
            </el-time-select>
          </el-form-item>
          <el-form-item label="原下课时间">
            <el-time-select size="mini" class="form-item" v-model="params.finishTime"
                            :picker-options="timePickerOptions">
            </el-time-select>
          </el-form-item>
          <br/>
          <el-form-item label="现日期">
            <el-date-picker v-model="params.nowdates"
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
          <el-form-item label="现上课时间">
            <el-time-select size="mini" class="form-item" v-model="params.attendTime1"
                            :picker-options="timePickerOptions">
            </el-time-select>
          </el-form-item>
          <el-form-item label="现下课时间">
            <el-time-select size="mini" class="form-item" v-model="params.finishTime1"
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
        <span class="big-bold"> 调课信息</span>
        <br>
        <br>
        <span class="indent"> 调动课程：   {{ exportMessage.name }}</span>
        <br>
        <span class="indent"> 原日期：     {{ exportMessage.old_date }} </span>
        <br>
        <span class="indent"> 现日期：     {{ exportMessage.new_date }}</span>
        <br>
        <span
          class="indent"> 原上课时间：  {{ exportMessage.old_start_time }}    原下课时间：  {{ exportMessage.old_end_time }}  原教室：  {{ exportMessage.old_room }}</span>
        <br>
        <span
          class="indent"> 现上课时间：  {{ exportMessage.new_start_time }}    现下课时间：  {{ exportMessage.new_end_time }}  现教室：  {{ exportMessage.new_room }}</span>
        <br>
        <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="closePop">确定</el-button>
          <el-button type="success" @click="copyToClipboard">复制</el-button>
      </span>
      </el-dialog>
      <el-table stripe border :data="data" @selection-change="handleSelectionChange">
        <el-table-column type="index" label="序号" width="50" align="center" class-name="stop"></el-table-column>
        <el-table-column prop="old_date" label="原日期" width="100" align="center" class-name="stop"></el-table-column>
        <el-table-column prop="old_start_time" label="原上课时间" width="100" align="center" class-name="stop"></el-table-column>
        <el-table-column prop="old_end_time" label="原下课时间" width="100" align="center" class-name="stop"></el-table-column>
        <el-table-column prop="old_classroom_name" label="原教室" width="80" align="center" class-name="stop"></el-table-column>
        <el-table-column prop="new_date" label="现日期" width="100" align="center"
                         class-name="label"></el-table-column>
        <el-table-column prop="new_start_time" label="现上课时间" width="100" align="center"
                         class-name="label"></el-table-column>
        <el-table-column prop="new_end_time" label="现下课时间" width="100" align="center"
                         class-name="label"></el-table-column>
        <el-table-column prop="new_classroom_name" label="现教室" width="80" align="center"
                         class-name="label"></el-table-column>
        <el-table-column prop="course_name" label="课程" class-name="course" align="center"
                         width="120"></el-table-column>
        <el-table-column prop="approval_status" label="调课状态" align="center" class-name="course"></el-table-column>
        <el-table-column label="调课信息导出" align="center" width="110">
          <template slot-scope="scope">
            <div class="back_button">
              <el-button type="primary" size="small" @click="button_export(scope.row)" class="green-button">导出</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
  </el-card>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import moment from 'moment'
import { Message } from 'element-ui'

export default {
  name: 'CourseSchedulingList',
  data () {
    return {
      currentVisible: false,
      data: [],
      classroomData: [],
      courseData: [],
      teacherData: [],
      params: {
      },
      idList: [],
      datePickerOptions: {
        firstDayOfWeek: 1
      },
      timePickerOptions: {
        start: '08:00',
        step: '00:' + '10',
        end: '21:30'
      },
      popVisible: false,
      exportMessage: {
        new_date: '',
        old_date: '',
        new_start_time: '',
        old_start_time: '',
        new_end_time: '',
        old_end_time: '',
        new_room: '',
        old_room: '',
        name: '',
        mergeInfo: ''
      }
    }
  },
  computed: {
    ...mapState(['userName'])
  },
  methods: {
    ...mapActions(['GetClassroomRefList', 'GetCourseRefList', 'GetTeacherRefList', 'GetCourseSchedulingList', 'RemoveCourseSchedulingByIdList', 'GetChangeList']),
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
      this.$set(this.params, 'nowdates', [this.params.startDate, this.params.endDate])
      this.search()
    },
    button_export (row) {
      this.exportMessage.name = row.course_name
      this.exportMessage.new_date = row.new_date
      this.exportMessage.old_date = row.old_date
      this.exportMessage.new_start_time = row.new_start_time
      this.exportMessage.old_start_time = row.old_start_time
      this.exportMessage.new_end_time = row.new_end_time
      this.exportMessage.old_end_time = row.old_end_time
      this.exportMessage.new_room = row.new_classroom_name
      this.exportMessage.old_room = row.old_classroom_name
      this.exportMessage.mergeInfo = '调课信息\n' + '调动课程： ' + row.course_name + '\n' + '原日期： ' + row.old_date + '  ' + '现日期： ' + row.new_date + '\n' + '原上课时间： ' + row.old_start_time + '原下课时间： ' + row.old_end_time + '原教室： ' + row.old_classroom_name + '\n' + '现上课时间： ' + row.new_start_time + '现下课时间： ' + row.new_end_time + '现教室： ' + row.new_classroom_name + '\n'
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
      if (this.params.dates && this.params.dates.length > 0) {
        this.params.old_date = this.params.dates[0]
        this.params.new_date = this.params.dates[1]
      }
      if (this.params.nowdates && this.params.nowdates.length > 0) {
        this.params.old_date1 = this.params.nowdates[0]
        this.params.new_date1 = this.params.nowdates[1]
      }
      if (this.params.attendTime) {
        this.params.old_start_time = moment('2023-12-31' + ' ' + this.params.attendTime).format('HH:mm:ss')
      }
      if (this.params.finishTime) {
        this.params.old_end_time = moment('2023-12-31' + ' ' + this.params.finishTime).format('HH:mm:ss')
      }
      if (this.params.attendTime1) {
        this.params.new_start_time = moment('2023-12-31' + ' ' + this.params.attendTime1).format('HH:mm:ss')
      }
      if (this.params.finishTime1) {
        this.params.new_end_time = moment('2023-12-31' + ' ' + this.params.finishTime1).format('HH:mm:ss')
      }
      this.params.teacherName = this.$store.state.userName
      this.GetChangeList(this.params).then(res => {
        if (res) {
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
      this.$set(this.params, 'nowdates', [this.params.startDate, this.params.endDate])
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
