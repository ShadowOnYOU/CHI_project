<!--这个是主界面-->

<template>
  <div id="course-scheduling" >
    <div>
      <el-row style="padding-left: 100px;">
        <el-col :span="20">
          <el-form inline>
            <el-form-item label="教室：">
              <el-select ref="classroomSelect" clearable multiple size="mini" style="width: 250px;"
                         v-model="params.classroomIdList" @change="search()">
                <el-option v-for="item in classroomData" :key="item.id" :label="item.name" :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="课程：">
              <el-select ref="courseSelect" clearable multiple size="mini" style="width: 250px;"
                         v-model="params.courseIdList" @change="search()">
                <el-option v-for="item in courseData" :key="item.id" :label="item.name" :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
      <FullCalendar ref="fullCalendar" :options="calendarOptions" style="padding-left: 100px;" class="custom-background"></FullCalendar>
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
import ViewCourseScheduling from '@/views/home/HomeViewCourseScheduling.vue'
export default {
  name: 'CourseScheduling',
  components: {
    ViewCourseScheduling,
    FullCalendar
  },
  data () {
    return {
      teacherId: '',
      courseCountObj: {},
      weekList: [],
      classroomData: [],
      courseData: [],
      teacherData: [],
      stopData: [],
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
        contentHeight: 'auto',
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
        editable: false,
        events: [],
        headerToolbar: {
          start: 'prev,next today',
          center: 'title',
          end: 'timeGridWeek,dayGridMonth'
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
        eventResize: this.eventResize
      }
    }
  },
  methods: {
    ...mapActions(['GetClassroomRefList', 'GetCourseRefList', 'GetCourseById', 'GetTeacherRefList', 'GetCourseSchedulingList', 'GetCancleList', 'UpdateCourseSchedulingTimeById', 'GetCourseSchedulingCourseCount']),
    init (info) {
      this.GetClassroomRefList().then(res => {
        if (res) {
          this.classroomData = res
        }
      }).catch(() => {
      })
      this.GetCourseById(this.$store.state.teacherId).then(res => {
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
      this.searchPlus(info)
    },
    search () {
      this.params.startDate = moment(this.$refs.fullCalendar.getApi().view.currentStart).format('YYYY-MM-DD')
      this.params.endDate = moment(this.$refs.fullCalendar.getApi().view.currentEnd).endOf('month').format('YYYY-MM-DD')
      if (this.$store.state.isLoggedIn) {
        this.params.teacherIdList = [this.$store.state.teacherId]
      }
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
            let backgroundColor = item.backgroundColor
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
      if (this.$store.state.isLoggedIn) {
        this.params.teacherIdList = [this.$store.state.teacherId]
      }
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
          if (info.view.type === 'timeGridWeek') {
            // 处理周视图
            this.calendarOptions.events = []
            res.forEach(item => {
              let backgroundColor
              backgroundColor = item.backgroundColor
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
            })
          } else if (info.view.type === 'dayGridMonth') {
            // 处理月视图
            this.calendarOptions.events = []
            res.forEach(item => {
              this.calendarOptions.events.push({
                id: item.id,
                title: item.courseName + '\n' + moment(item.date + ' ' + item.attendTime).format('HH:mm') + '——' + moment(item.date + ' ' + item.finishTime).format('HH:mm'),
                start: item.date + ' ' + item.attendTime,
                end: item.date + ' ' + item.finishTime,
                extendedProps: '',
                backgroundColor: item.backgroundColor,
                borderColor: item.backgroundColor
              })
            })
          }
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
    saveSuccess (info) {
      this.searchPlus(info)
      this.saveCourseSchedulingVisible = false
    },
    saveBatchSuccess (info) {
      this.searchPlus(info)
      this.saveBatchCourseSchedulingVisible = false
    },
    updateSuccess (info) {
      this.searchPlus(info)
      this.viewCourseSchedulingVisible = false
    },
    datesSet (info) {
      this.searchPlus(info)
    },
    dayHeaderContent (info) {
      if (info.view.type === 'dayGridMonth') {
        return { html: `<div class="fc-scrollgrid-sync-inner"><span class="fc-col-header-cell-cushion" disabled>${info.text}</span></div>` }
      } else if (info.view.type === 'timeGridWeek') {
        return {
          html: `<div class="fc-scrollgrid-sync-inner">
                    <span class="fc-col-header-cell-cushion"
                        data-navlink="{&quot;date&quot;:&quot;${moment(info.date).format('YYYY-MM-DD')}&quot;,&quot;type&quot;:&quot;day&quot;}" tabindex="0" disabled>${info.text}</span>
                </div>`
        }
      }
      return {
        html: `<div class="fc-scrollgrid-sync-inner">
                     <a class="fc-col-header-cell-cushion">${info.text}</a>
                     <span id="tams-course-count-${moment(info.date).format('YYYY-MM-DD')}" style="cursor: default;" disabled >${this.getCourseCount(info.date)}</span>节课
                 </div>`
      }
    },
    getCourseCount (date) {
      const count = this.courseCountObj[moment(date).format('YYYY-MM-DD')]
      return count || 0
    },
    handleEventClick (info) {
      this.id = info.event.id
      this.viewCourseSchedulingVisible = true
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
    eventDrop (info) {
      console.log('eventDrop', info)
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
    eventResize (info) {
      console.log('eventResize', info)
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
    }

  },
  mounted (info) {
    this.init(info)
  }
}

</script>
