import { axios } from '@/util/request'

export const getClassroomPage = (params) => {
  return axios({
    url: 'classroom',
    method: 'get',
    params
  })
}

export const getClassroomRefList = () => {
  return axios({
    url: 'classroom/list/ref',
    method: 'get'
  })
}

export const getClassroomById = (id) => {
  return axios({
    url: 'classroom/' + id,
    method: 'get'
  })
}

export const saveClassroom = (data) => {
  return axios({
    url: 'classroom',
    method: 'post',
    data
  })
}
