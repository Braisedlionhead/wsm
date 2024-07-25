<template>
    <div class="personal-center">
      <el-card class="personal-info">
        <div slot="header" class="clearfix">
          <span>个人信息</span>
        </div>
        <el-row>
          <el-col :span="6">
            <el-avatar :size="80" :src="userInfo.avatar" fit="cover"></el-avatar>
          </el-col>
          <el-col :span="18">
            <div class="info-item">
              <span class="label">用户名:</span>
              <span>{{ userInfo.username }}</span>
            </div>
            <div class="info-item">
              <span class="label">职位:</span>
              <span>{{ userInfo.position }}</span>
            </div>
            <div class="info-item">
              <span class="label">邮箱:</span>
              <span>{{ userInfo.email }}</span>
            </div>
            <div class="info-item">
              <span class="label">电话:</span>
              <span>{{ userInfo.phone }}</span>
            </div>
          </el-col>
        </el-row>
      </el-card>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  
  const userInfo = ref({
    avatar: '/images/1.png',
    username: 'xx',
    position: 'プロダクションマネージャー',
    email: 'murakami@xmail.com',
    phone: '008114866432234'
  });
  </script>
  
  <style scoped>
  .personal-center {
    padding: 20px;
  }
  
  .personal-info {
    width: 600px;
    margin: 0 auto;
  }
  
  .info-item {
    margin-bottom: 10px;
  }
  
  .label {
    font-weight: bold;
    margin-right: 10px;
  }
  </style>
  