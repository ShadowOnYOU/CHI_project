import Vue from 'vue'
import Vuex from 'vuex'
import teacher from '@/store/teacher'
import course from '@/store/course'
import classroom from '@/store/classroom'
import courseScheduling from '@/store/course-scheduling'
import color from '@/store/color'
import createPersistedState from 'vuex-persistedstate'
// import report from '@/store/report'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    isLoggedIn: false,
    userName: '',
    teacherId: '',
    selectedId: ''
  },
  mutations: {
    setLoggedIn (state, value) {
      state.isLoggedIn = value
    },
    setUserName (state, value) {
      state.userName = value
    },
    setTeacherId (state, value) {
      state.teacherId = value
      console.log('Teacher ID:', value)
    },
    setSelectedId (state, value) {
      state.selectedId = value
      console.log('Teacher ID:', value)
    }
  },
  actions: {
  },
  modules: {
    teacher,
    course,
    classroom,
    courseScheduling,
    color
  },
  plugins: [createPersistedState()]
})
//  plugins: [createPersistedState({ storage: window.sessionStorage })]
