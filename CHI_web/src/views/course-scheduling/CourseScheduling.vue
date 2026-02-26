<!--这个是主界面-->

<template>
  <div id="course-scheduling">
    <div>
      <el-dialog :visible="isPopupVisible" @close="closePopup">
        <span>{{ message }}</span>
        <span slot="footer" class="dialog-footer">
        <el-button @click="cancel">取消</el-button>
        <el-button type="primary" @click="confirm">确定</el-button>
      </span>
      </el-dialog>
      <el-row style="padding-left: 100px;">
        <el-col>
          <el-form inline>
          </el-form>
        </el-col>
      </el-row>
      <button v-on:click="tour" style="position : absolute; top: 85px; left: 1100px; border: none; background: white;">
        <img src="../../resource/tour_final.png"></button>
      <FullCalendar ref="fullCalendar" :options="calendarOptions" style="padding-left: 100px"></FullCalendar>
      <v-tour name="myTour" :steps="steps" :options="myOptions"></v-tour>
    </div>
    <SaveCourseScheduling :visible="saveCourseSchedulingVisible"
                          :date="currentDate"
                          :attendTime="currentAttendTime"
                          @on-close="saveCourseSchedulingVisible=false"
                          @on-success="saveSuccess">
    </SaveCourseScheduling>
    <ViewCourseScheduling :visible="viewCourseSchedulingVisible"
                          :id="id"
                          @on-success="updateSuccess"
                          @on-close="viewCourseSchedulingVisible=false"></ViewCourseScheduling>
  </div>
</template>
<style>
</style>
<script>
import FullCalendar from '@fullcalendar/vue'
import dayGridPlugin from '@fullcalendar/daygrid'
import listPlugin from '@fullcalendar/list'
import timeGridPlugin from '@fullcalendar/timegrid'
import interactionPlugin from '@fullcalendar/interaction'
import moment from 'moment'
import { mapActions } from 'vuex'
import ViewCourseScheduling from '@/views/course-scheduling/ViewCourseScheduling'
import { Message } from 'element-ui'
import axios from 'axios'

export default {
  name: 'CourseScheduling',
  components: {
    ViewCourseScheduling,
    FullCalendar
  },
  computed: {
    message () {
      return `注意，您选择的时间段有 ${this.student_num} 名学生有时间冲突，是否继续？`
    }
  },
  data () {
    return {
      select_id: this.$route.query.id,
      stopData: {},
      select_date: this.$route.query.date,
      teacher_id: this.$store.state.teacherId,
      select_color: '',
      student_num: 999,
      isPopupVisible: false,
      confirmationMessage: '',
      select_data: {
        date: '',
        attendTime: '',
        finishTime: '',
        classroom: '',
        course_name: '',
        teacherName: ''
      },
      end_data: {
        date: '',
        attendTime: '',
        finishTime: '',
        classroom: '',
        course_name: '',
        teacherName: ''
      },
      changeNewDTO: {
        course_name: '',
        teacherName: '',
        old_date: '',
        new_date: '',
        old_start_time: '',
        old_end_time: '',
        new_start_time: '',
        new_end_time: '',
        old_classroom_name: '',
        new_classroom_name: '',
        approval_status: '审批通过'
      },
      dataNewDTO: {
        course_name: '',
        teacherName: '',
        date: '',
        attend_time: '',
        finish_time: '',
        classroom_name: ''
      },
      courseCountObj: {},
      weekList: [],
      classroomData: [],
      courseData: [],
      teacherData: [],
      params: {},
      newparams: {},
      saveCourseSchedulingVisible: false,
      viewCourseSchedulingVisible: false,
      saveBatchCourseSchedulingVisible: false,
      exportCourseSchedulingVisible: false,
      id: 0,
      currentDate: '',
      currentAttendTime: '',
      calendarOptions: {
        displayEventTime: false,
        plugins: [dayGridPlugin, listPlugin, timeGridPlugin, interactionPlugin],
        initialView: 'timeGridWeek',
        locale: 'zh',
        // 每周开始是周几，Sunday=0, Monday=1
        firstDay: '1',
        // 时间轴间距
        contentHeight: 'auto',
        slotMinTime: '08:00',
        slotMaxTime: '22:00',
        slotDuration: '00:' + this.$consts.COURSE_DURATION_STEP_MINUTE + ':00',
        // 是否显示第几周
        // weekNumbers: true,
        // weekText: '周',
        // 是否显示当前时间标记
        nowIndicator: true,
        // 是否显示全天插槽
        allDaySlot: false,
        // 日期否可点击
        navLinks: true,
        // 月视图，是否为指定周数高度，true 6周高度
        fixedWeekCount: false,
        // 月视图，是否显示非本月日期
        showNonCurrentDates: false,
        // 月视图，限制每天显示最大事件数，不包括+more链接，false 全部显示，true 限制为日单元格的高度，number 限制为指定行数高度
        dayMaxEvents: true,
        // 与dayMaxEvents类似，区别为包括+more链接
        // dayMaxEventRows: true,
        // 是否可拖拽
        editable: true,
        events: [],
        customButtons: {
          confirmButton: {
            text: '确认',
            click: function () {
              this.before_button_confirm()
              // Message.success('confirm')
            }.bind(this)
          },
          resetButton: {
            text: '重置',
            click: function () {
              this.button_rest()
              // Message.success('reset')
            }.bind(this)
          }
        },
        buttonText: {
          today: '今天',
          month: '月',
          week: '周',
          day: '日',
          list: '周列表'
        },
        slotLabelFormat: {
          hour: 'numeric',
          minute: '2-digit',
          hour12: false
        },
        datesSet: this.datesSet,
        dayHeaderContent: this.dayHeaderContent,
        eventClick: this.handleEventClick,
        dateClick: this.handleDateClick,
        // 当事件拖动时触发
        eventDragStart: this.eventDragStart,
        // 当事件拖动停止时触发
        eventDragStop: this.eventDragStop,
        // 当拖动停止并且事件移动到不同的日子/时间时触发
        eventDrop: this.eventDrop,
        // 当外部可拖动元素或来自另一个日历的事件被拖放到该日历中时调用
        drop: this.drop,
        // 当带有关联事件数据的外部可拖动元素拖放到日历中时调用，或来自另一个日历的事件
        eventReceive: this.eventReceive,
        // 当一个日历上的事件即将拖放到另一个日历上时触发
        eventLeave: this.eventLeave,
        // 开始缩放时触发
        eventResizeStart: this.eventResizeStart,
        // 停止缩放时触发
        eventResizeStop: this.eventResizeStop,
        // 当缩放停止且事件持续时间发生更改时触发
        eventResize: this.eventResize,
        headerToolbar: {
          start: 'prev,next today',
          center: 'title',
          end: 'resetButton confirmButton timeGridWeek,dayGridMonth'
        }
      },
      myOptions: {
        useKeyboardNavigation: false, // 是否通过键盘的←, → 和 ESC 控制指引
        labels: { // 指引项的按钮文案
          buttonSkip: '跳过指引', // 跳过文案
          buttonPrevious: '上一步', // 上一步文案
          buttonNext: '下一步', // 下一步文案
          buttonStop: '结束指引' // 结束文案
        },
        highlight: false // 是否高亮显示激活的的target项
      },
      steps: [
        {
          target: '.fc-toolbar-title', // 当前项的id或class或data-v-step属性
          content: '蓝色表示已选中的课程\n红色表示暂停的课程\n灰色表示无法修改的课程', // 当前项指引内容
          color: '',
          params: {
            placement: 'left', // 指引在target的位置，支持上、下、左、右
            highlight: false, // 当前项激活时是否高亮显示
            enableScrolling: false // 指引到当前项时是否滚动轴滚动到改项位置
          },
          // 在进行下一步时处理UI渲染或异步操作，例如打开弹窗，调用api等。当执行reject时，指引不会执行下一步
          before: type => new Promise((resolve, reject) => {
            // 耗时的UI渲染或异步操作
            resolve('foo')
          })
        },
        {
          target: '.fc-confirmButton-button', // 当前项的id或class或data-v-step属性
          content: '所有课程调动，必须点击确认按钮才能够保存', // 当前项指引内容
          params: {
            placement: 'top', // 指引在target的位置，支持上、下、左、右
            highlight: false, // 当前项激活时是否高亮显示
            enableScrolling: false // 指引到当前项时是否滚动轴滚动到改项位置
          },
          // 在进行下一步时处理UI渲染或异步操作，例如打开弹窗，调用api等。当执行reject时，指引不会执行下一步
          before: type => new Promise((resolve, reject) => {
            // 耗时的UI渲染或异步操作
            resolve('foo')
          })
        },
        {
          target: '.fc-resetButton-button', // 当前项的id或class或data-v-step属性
          content: '点击重置按钮可以返回到上一次保存的状态', // 当前项指引内容
          params: {
            placement: 'left', // 指引在target的位置，支持上、下、左、右
            highlight: false, // 当前项激活时是否高亮显示
            enableScrolling: false // 指引到当前项时是否滚动轴滚动到改项位置
          },
          // 在进行下一步时处理UI渲染或异步操作，例如打开弹窗，调用api等。当执行reject时，指引不会执行下一步
          before: type => new Promise((resolve, reject) => {
            // 耗时的UI渲染或异步操作
            resolve('foo')
          })
        }
      ]
    }
  },
  methods: {
    ...mapActions(['before_button_confirm', 'scrollToTargetDate', 'GetClassroomRefList', 'GetCourseRefList', 'GetTeacherRefList', 'GetCourseSchedulingList', 'GetCancleList', 'UpdateCourseSchedulingTimeById', 'GetCourseSchedulingCourseCount']),
    scrollToTargetDate () {
      const fullCalendar = this.$refs.fullCalendar.getApi()
      fullCalendar.gotoDate(this.select_date)
    },
    showPopup () {
      this.isPopupVisible = true
    },
    closePopup () {
      this.isPopupVisible = false
    },
    cancel () {
      // 取消操作逻辑
      this.closePopup()
      this.button_rest()
    },
    tour () {
      console.log('kdflakdf')
      this.$tours.myTour.start()
    },
    confirm () {
      // 确认操作逻辑
      this.closePopup()
      this.button_confirm()
      // ...其他操作逻辑...
    },
    init (info) {
      this.scrollToTargetDate()
      console.log('init')
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
      this.search()
    },
    search () {
      this.$store.commit('setSelectedId', this.select_id)
      this.params.startDate = moment(this.$refs.fullCalendar.getApi().view.currentStart).format('YYYY-MM-DD')
      this.params.endDate = moment(this.$refs.fullCalendar.getApi().view.currentEnd).endOf('month').format('YYYY-MM-DD')
      this.params.teacherIdList = [this.teacher_id]
      this.params.courseScheduleIdList = [this.select_id]
      this.newparams.teacherName = this.$store.state.userName
      this.GetCancleList(this.newparams).then(res => {
        if (res) {
          console.log(res)
          this.stopData = res
        }
      }).catch(() => {
      })
      this.GetCourseSchedulingList(this.params).then(res => {
        if (res) {
          this.calendarOptions.events = []
          res.forEach(item => {
            let backgroundColor
            if (item.id === this.select_id) {
              backgroundColor = item.backgroundColor
              if (this.select_data.date === '') {
                this.select_data.date = item.date
                this.select_data.attendTime = item.attendTime
                this.select_data.finishTime = item.finishTime
                this.select_data.classroom = item.classroomName
                this.select_data.course_name = item.courseName
                this.select_data.teacherName = item.teacherName
              } else {
                this.end_data.date = item.date
                this.end_data.attendTime = item.attendTime
                this.end_data.finishTime = item.finishTime
                this.end_data.classroom = item.classroomName
                this.end_data.course_name = item.courseName
                this.end_data.teacherName = item.teacherName
                this.UpdateCourseSchedulingTimeById({
                  id: this.select_id,
                  data: {
                    date: moment(this.select_data.date + ' ' + this.select_data.attendTime).format('YYYY-MM-DD'),
                    attendTime: moment(this.select_data.date + ' ' + this.select_data.attendTime).format('HH:mm:ss'),
                    finishTime: moment(this.select_data.date + ' ' + this.select_data.finishTime).format('HH:mm:ss')
                  }
                })
              }
              if (this.end_data.date !== '') {
                this.calendarOptions.events.push({
                  id: item.id,
                  title: this.end_data.course_name + '\n' + this.end_data.classroom + '\n' + this.end_data.teacherName,
                  start: this.end_data.date + ' ' + this.end_data.attendTime,
                  end: this.end_data.date + ' ' + this.end_data.finishTime,
                  extendedProps: item,
                  backgroundColor: backgroundColor,
                  borderColor: backgroundColor
                })
              } else {
                this.calendarOptions.events.push({
                  id: item.id,
                  title: item.courseName + '\n' + item.classroomName + '\n' + item.teacherName,
                  start: item.date + ' ' + item.attendTime,
                  end: item.date + ' ' + item.finishTime,
                  extendedProps: item,
                  backgroundColor: backgroundColor,
                  borderColor: backgroundColor
                })
              }
            } else {
              backgroundColor = '#bdc3c7'
              for (let i = 0; i < this.stopData.length; i++) {
                // 对每个元素做一些操作，比如打印它的长度
                if (item.classroomName === this.stopData.at(i).classroom_name) {
                  if (item.date === this.stopData.at(i).date) {
                    if (item.attendTime === this.stopData.at(i).attend_time) {
                      backgroundColor = '#F25022'
                    }
                  }
                }
              }
              this.calendarOptions.events.push({
                id: item.id,
                title: item.courseName + '\n' + item.classroomName + '\n' + item.teacherName,
                start: item.date + ' ' + item.attendTime,
                end: item.date + ' ' + item.finishTime,
                extendedProps: item,
                backgroundColor: backgroundColor,
                borderColor: backgroundColor
              })
            }
          })
        }
      }).catch(() => {
      })
      this.GetCourseSchedulingCourseCount(this.params).then(res => {
        if (res) {
          this.courseCountObj = res
        }
      }).catch(() => {
      })
    },
    searchPlus (info) {
      this.params.startDate = moment(this.$refs.fullCalendar.getApi().view.currentStart).format('YYYY-MM-DD')
      this.params.endDate = moment(this.$refs.fullCalendar.getApi().view.currentEnd).endOf('month').format('YYYY-MM-DD')
      this.GetCancleList(this.newparams).then(res => {
        if (res) {
          console.log(res)
          this.stopData = res
        }
      }).catch(() => {
      })
      this.GetCourseSchedulingList(this.params).then(res => {
        if (res) {
          this.calendarOptions.events = []
          res.forEach(item => {
            let backgroundColor
            if (item.id === this.select_id) {
              backgroundColor = item.backgroundColor
              if (this.select_data.date === '') {
                this.select_data.date = item.date
                this.select_data.attendTime = item.attendTime
                this.select_data.finishTime = item.finishTime
                this.select_data.classroom = item.classroomName
                this.select_data.course_name = item.courseName
                this.select_data.teacherName = item.teacherName
              } else {
                this.end_data.date = item.date
                this.end_data.attendTime = item.attendTime
                this.end_data.finishTime = item.finishTime
                this.end_data.classroom = item.classroomName
                this.end_data.course_name = item.courseName
                this.end_data.teacherName = item.teacherName
                this.UpdateCourseSchedulingTimeById({
                  id: this.select_id,
                  data: {
                    date: moment(this.select_data.date + ' ' + this.select_data.attendTime).format('YYYY-MM-DD'),
                    attendTime: moment(this.select_data.date + ' ' + this.select_data.attendTime).format('HH:mm:ss'),
                    finishTime: moment(this.select_data.date + ' ' + this.select_data.finishTime).format('HH:mm:ss')
                  }
                })
              }
            } else {
              backgroundColor = '#bdc3c7'
              for (let i = 0; i < this.stopData.length; i++) {
                // 对每个元素做一些操作，比如打印它的长度
                if (item.classroomName === this.stopData.at(i).classroom_name) {
                  if (item.date === this.stopData.at(i).date) {
                    if (item.attendTime === this.stopData.at(i).attend_time) {
                      backgroundColor = '#F25022'
                    }
                  }
                }
              }
            }
            if (info.view.type === 'timeGridWeek') {
              // 处理周视图
              this.calendarOptions.events.push({
                id: item.id,
                title: item.courseName + '\n' + item.classroomName + '\n' + item.teacherName,
                start: item.date + ' ' + item.attendTime,
                end: item.date + ' ' + item.finishTime,
                extendedProps: item,
                backgroundColor: backgroundColor,
                borderColor: backgroundColor
              })
            } else if (info.view.type === 'dayGridMonth') {
              // 处理月视图
              this.calendarOptions.events.push({
                id: item.id,
                title: item.courseName + '\n' + moment(item.date + ' ' + item.attendTime).format('HH:mm') + '——' + moment(item.date + ' ' + item.finishTime).format('HH:mm'),
                start: item.date + ' ' + item.attendTime,
                end: item.date + ' ' + item.finishTime,
                extendedProps: '',
                backgroundColor: backgroundColor,
                borderColor: backgroundColor
              })
            }
          })
        }
      }).catch(() => {
      })
      this.GetCourseSchedulingCourseCount(this.params).then(res => {
        if (res) {
          this.courseCountObj = res
        }
      }).catch(() => {
      })
    },
    saveSuccess () {
      this.search()
      this.saveCourseSchedulingVisible = false
    },
    saveBatchSuccess (info) {
      this.searchPlus(info)
      this.saveBatchCourseSchedulingVisible = false
    },
    updateSuccess () {
      this.search()
      this.viewCourseSchedulingVisible = false
    },
    datesSet (info) {
      this.searchPlus(info)
    },
    dayHeaderContent (info) {
      if (info.view.type === 'dayGridMonth') {
        return { html: `<div class="fc-scrollgrid-sync-inner"><a class="fc-col-header-cell-cushion">${info.text}</a></div>` }
      } else if (info.view.type === 'timeGridWeek') {
        return {
          html: `<div class="fc-scrollgrid-sync-inner">
                    <a class="fc-col-header-cell-cushion"
                        data-navlink="{&quot;date&quot;:&quot;${moment(info.date).format('YYYY-MM-DD')}&quot;,&quot;type&quot;:&quot;day&quot;}" tabindex="0">${info.text}</a>
                </div>`
        }
      }
      return {
        html: `<div class="fc-scrollgrid-sync-inner">
                     <a class="fc-col-header-cell-cushion">${info.text}</a>
                 </div>`
      }
    },
    getCourseCount (date) {
      const count = this.courseCountObj[moment(date).format('YYYY-MM-DD')]
      return count || 0
    },
    handleEventClick (info) {
      const courseId = info.event.id
      if (courseId === this.select_id) {
        this.id = info.event.id
        this.viewCourseSchedulingVisible = true
      } else {
        Message.warning('不可修改')
      }
    },
    handleDateClick (info) {
      this.currentDate = moment(info.date).format('YYYY-MM-DD')
      const infoDate = moment(info.date)
      if (infoDate.hour() > 0) {
        this.currentAttendTime = infoDate.format('HH:mm')
      }
      this.saveCourseSchedulingVisible = true
    },
    eventDragStart (info) {
      console.log('eventDragStart', info)
    },
    eventDragStop (info) {
      console.log('eventDragStop', info)
    },
    button_rest () {
      Message.success('已重置修改')
      this.UpdateCourseSchedulingTimeById({
        id: this.select_id,
        data: {
          date: moment(this.select_data.date + ' ' + this.select_data.attendTime).format('YYYY-MM-DD'),
          attendTime: moment(this.select_data.date + ' ' + this.select_data.attendTime).format('HH:mm:ss'),
          finishTime: moment(this.select_data.date + ' ' + this.select_data.finishTime).format('HH:mm:ss')
        }
      }).then(res => {
        this.search()
      }).catch(() => {
        this.search()
      })
    },
    before_button_confirm () {
      console.log('Date:', this.end_data.date)
      console.log('Attend Time:', this.end_data.attendTime)
      // 在这边进行判断，如果这个时间段本来就是空的，或者在经过检查后发现没有学生冲突，就直接调用this.button_confirm函数
      axios.post('http://localhost:12010/course-scheduling/conflict', null, {
        params: {
          courseId: this.select_id,
          date: moment(this.end_data.date).format('YYYY-MM-DD'),
          attendTime: this.end_data.attendTime
        }
      })
        .then((response) => {
          this.student_num = response.data.data
          console.log(response.data.data)
        })
        .catch((error) => {
          console.log(error)
        })
      // console.log(this.student_num + 'fdka;lfda;')
      if (this.student_num > 0) {
        this.showPopup()
      } else {
        this.button_confirm()
      }
      // this.showPopup()
    },
    button_confirm () {
      this.changeNewDTO.course_name = this.select_data.course_name
      this.changeNewDTO.teacherName = this.select_data.teacherName
      this.changeNewDTO.old_date = this.select_data.date
      this.changeNewDTO.new_date = this.end_data.date
      this.changeNewDTO.old_start_time = this.select_data.attendTime
      this.changeNewDTO.old_end_time = this.select_data.finishTime
      this.changeNewDTO.new_start_time = this.end_data.attendTime
      this.changeNewDTO.new_end_time = this.end_data.finishTime
      this.changeNewDTO.old_classroom_name = this.select_data.classroom
      this.changeNewDTO.new_classroom_name = this.select_data.classroom
      this.changeNewDTO.approval_status = '审批通过'
      this.dataNewDTO.date = this.select_data.date
      this.dataNewDTO.attend_time = this.select_data.attendTime
      this.dataNewDTO.finish_time = this.select_data.finishTime
      this.dataNewDTO.classroom_name = this.select_data.classroom
      this.dataNewDTO.teacherName = this.select_data.teacherName
      this.dataNewDTO.course_name = this.select_data.course_name
      this.addChange(this.changeNewDTO)
      this.deleteCancel(this.dataNewDTO)
      this.select_data.date = this.end_data.date
      this.select_data.attendTime = this.end_data.attendTime
      this.select_data.finishTime = this.end_data.finishTime
      this.UpdateCourseSchedulingTimeById({
        id: this.select_id,
        data: {
          date: moment(this.end_data.date + ' ' + this.end_data.attendTime).format('YYYY-MM-DD'),
          attendTime: moment(this.end_data.date + ' ' + this.end_data.attendTime).format('HH:mm:ss'),
          finishTime: moment(this.end_data.date + ' ' + this.end_data.finishTime).format('HH:mm:ss')
        }
      }).then(res => {
        this.search()
      }).catch(() => {
        this.search()
      })
      Message.success('调课已提交审批')
    },
    async deleteCancel (dataNewDTO) {
      try {
        const service = axios.create({
          baseURL: 'http://localhost:12011', // 后端的基础地址
          timeout: 5000, // 请求超时时间
          headers: {
            'Content-Type': 'application/json' // 请求头的类型
          }
        })
        // 发送 post 请求，传递 changeNewDTO 对象
        const response = await service.post('/cancle/deleteData', dataNewDTO)
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
    },
    async addChange (changeNewDTO) {
      try {
        const service = axios.create({
          baseURL: 'http://localhost:12011', // 后端的基础地址
          timeout: 5000, // 请求超时时间
          headers: {
            'Content-Type': 'application/json' // 请求头的类型
          }
        })
        // 发送 post 请求，传递 changeNewDTO 对象
        const response = await service.post('/change/new', changeNewDTO)
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
    },
    // eventDrop (info) {
    //   // 获取被拖动事件的课程ID
    //   const courseId = info.event.id
    //   // 在这里进行筛选判断
    //   if (courseId === this.select_id) {
    //     // 允许拖动的逻辑
    //     console.log('允许拖动')
    //     // 执行其他操作，如更新数据等
    //     this.UpdateCourseSchedulingTimeById({
    //       id: info.event.id,
    //       data: {
    //         date: moment(info.event.start).format('YYYY-MM-DD'),
    //         attendTime: moment(info.event.start).format('HH:mm:ss'),
    //         finishTime: moment(info.event.end).format('HH:mm:ss')
    //       }
    //     }).then(res => {
    //       this.searchPlus(info)
    //     }).catch(() => {
    //       this.searchPlus(info)
    //     })
    //   } else {
    //     // 不允许拖动的逻辑
    //     console.log('不允许拖动')
    //     Message.warning('不可修改')
    //     // 可以选择取消拖动操作，恢复事件的位置
    //     info.revert()
    //   }
    // },
    eventDrop (info) {
      // 获取被拖动事件的课程ID
      const courseId = info.event.id

      // 在这里进行筛选判断
      if (courseId === this.select_id) {
        // 允许拖动的逻辑
        console.log('允许拖动')
        // 执行其他操作，如更新数据等
        const date = moment(info.event.start).format('YYYY-MM-DD')
        const originalAttendTime = moment(info.event.start).format('HH:mm:ss')
        const originalFinishTime = moment(info.event.end).format('HH:mm:ss')

        // 定义可选的起始时间列表
        const availableStartTimes = ['08:00:00', '09:00:00', '10:10:00', '11:10:00', '14:00:00', '15:00:00', '16:10:00', '17:10:00', '18:30:00', '19:30:00', '20:30:00']

        // 查找最接近的起始时间
        const closestStartTime = availableStartTimes.reduce((prev, curr) => {
          const prevDiff = Math.abs(moment(prev, 'HH:mm:ss').diff(moment(originalAttendTime, 'HH:mm:ss')))
          const currDiff = Math.abs(moment(curr, 'HH:mm:ss').diff(moment(originalAttendTime, 'HH:mm:ss')))
          return currDiff <= prevDiff ? curr : prev
        })

        // 计算时间间隔
        const timeInterval = moment(originalFinishTime, 'HH:mm:ss').diff(moment(originalAttendTime, 'HH:mm:ss'))

        // 更新起始时间和结束时间
        const updatedAttendTime = closestStartTime
        const updatedFinishTime = moment(updatedAttendTime, 'HH:mm:ss').add(timeInterval).format('HH:mm:ss')
        this.end_data.date = date
        this.end_data.attendTime = updatedFinishTime
        this.end_data.finishTime = updatedFinishTime
        // 更新课程时间
        this.UpdateCourseSchedulingTimeById({
          id: info.event.id,
          data: {
            date: date,
            attendTime: updatedAttendTime,
            finishTime: updatedFinishTime
          }
        }).then(res => {
          this.searchPlus(info)
        }).catch(() => {
          this.searchPlus(info)
        })
      } else {
        // 不允许拖动的逻辑
        console.log('不允许拖动')
        Message.warning('不可修改')
        // 可以选择取消拖动操作，恢复事件的位置
        info.revert()
      }
    },
    drop (info) {
      console.log('drop', info)
    },
    eventReceive (info) {
      console.log('eventReceive', info)
    },
    eventLeave (info) {
      console.log('eventLeave', info)
    },
    eventResizeStart (info) {
      console.log('eventResizeStart', info)
    },
    eventResizeStop (info) {
      console.log('eventResizeStop', info)
    },
    getHeaderToolbar () {
      if (this.select_id !== null && this.select_id !== undefined) {
        return {
          start: 'prev,next today',
          center: 'title',
          end: 'resetButton confirmButton timeGridWeek,dayGridMonth'
        }
      } else {
        return {
          start: 'prev,next today',
          center: 'title',
          end: 'timeGridWeek,dayGridMonth'
        }
      }
    },
    updateCalendarOptions () {
      console.log('更新日历选项')
      this.calendarOptions.headerToolbar = this.getHeaderToolbar()
    },
    eventResize (info) {
      const courseId = info.event.id
      if (courseId === this.select_id) {
        console.log('eventResize', info)
        // 执行其他操作，如更新数据等
        this.UpdateCourseSchedulingTimeById({
          id: info.event.id,
          data: {
            date: moment(info.event.start).format('YYYY-MM-DD'),
            attendTime: moment(info.event.start).format('HH:mm:ss'),
            finishTime: moment(info.event.end).format('HH:mm:ss')
          }
        }).then(res => {
          this.searchPlus(info)
        }).catch(() => {
          this.searchPlus(info)
        })
      } else {
        // 不允许拖动的逻辑
        Message.warning('不可修改')
        // 可以选择取消拖动操作，恢复事件的位置
        info.revert()
      }
    }
  },
  mounted (info) {
    console.log('mounted')
    this.init(info)
    this.updateCalendarOptions()
  }
}
</script>
