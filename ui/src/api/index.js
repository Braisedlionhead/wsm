import axios from 'axios'

// ---要通过request.defaults来获取其中的属性---
const request = axios.create({
  baseURL: 'http://localhost:8080',
  // timeout: 10000,
  headers: { 'Content-Type': 'application/json;charset=utf-8' }
})

export default request