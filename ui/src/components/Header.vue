<template>
  <div style="display: flex;align-items: center; justify-content: space-between; line-height: 50px;">
  <div style="flex: 1; text-align: center; font-size: 20px; ">
  </div>


  <div class="toolbar">
    <el-badge v-if="!mesNum" :value="mesNum" :show-zero="false" class="item" :offset="[, 8]" style="margin-right: 5px;" >
      <el-icon class="large-icon" @click="readMessage" ><MessageBox /></el-icon>
    </el-badge>
    <span class="toolbar-text">{{ userNickName }}</span>

    <el-dropdown>
      <el-icon style="margin-left: 5px;" class="large-icon">
        <UserFilled />
      </el-icon>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item @click="to_User">Profile</el-dropdown-item>
          <el-dropdown-item @click="to_Logout">Logout</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</div>

</template>

<script setup>
import { UserFilled,MessageBox } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import router from '@/router'
import store from '@/store'
import { ref, onMounted } from 'vue'


const mesNum = ref(3)
const userNickName = ref('')

const readMessage = () => {
  mesNum.value = 0
}


const to_User = () => {
  router.push({ name: "profile" });
}

const to_Logout = () => {
  ElMessageBox.confirm(
    'This operation will log out. Continue?',
    'Warning',
    {
      confirmButtonText: 'Yes',
      cancelButtonText: 'Cancel',
      type: 'warning',
    }
  )
    .then(async () => {
      ElMessage({
        type: 'success',
        message: 'Logout successfully!',
      })
    // 删除登录信息
    const loginResponse = await store.dispatch("LOGOUT"); // 必须先确保删除用户信息，再执行下面的操作
    console.log('loginResponse: ', loginResponse)
      router.push({ name: "login" });
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: 'Canceled',
      })
    })
}


onMounted( () =>{
  userNickName.value = JSON.parse(sessionStorage.getItem('loginUser')).nickName
})
</script>


<style scoped>

.layout-container-demo .toolbar {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  right: 20px;
}

#app {
  height: 100%;
}


.large-icon {
  font-size: 24px;
  cursor: pointer;
}


.toolbar-text {
  font-size: 20px;
}

/* .el-switch{
  --el-switch-on-color: #3c3c3c;
  --el-switch-off-color: #eaeaea;
} */

</style>