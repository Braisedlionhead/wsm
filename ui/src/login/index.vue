<template>
  <div class="isDark">
  <el-card class="box-card login-card">
    <span class="login-title">▶️⏸️⏯️⏩⏪🔂🔀🔁</span>
    <span class="login-tip">👁️👁️</span>
    <el-form ref="userRef" :model="user" label-width="80px" :rules="rules">
      <el-form-item label="Username" prop="userName">
        <el-input v-model="user.userName" placeholder="Please enter the username"></el-input>
      </el-form-item>
      <el-form-item label="Password" prop="password">
        <el-input v-model="user.password" type="password" placeholder="Please enter the password"
          show-password></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="doLogin()">Login!!</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</div>
</template>

<script setup>
import { ElMessage } from 'element-plus'
import { ref } from 'vue'
import router from '@/router';
import store from '@/store';
import { useDark } from '@vueuse/core'


const isDark = useDark()

const rules = {
  userName: [
    {
      required: true,
      message: 'username could not be empty',
      trigger: 'blur'
    },
    {
      min: 2,
      max: 10,
      message: 'username length should be 2-10 characters',
      trigger: 'blur'
    },
  ],
  password: [{
    required: true,
    message: 'password could not be empty',
    trigger: 'blur',
  }]
}

const userRef = ref(null)
let user = ref({
  userName: 'sa',
  password: 'root'
});


const doLogin = async () => {
  const valid = await userRef.value.validate();
  if (valid) {
    try {
      const loginResponse = await store.dispatch("LOGIN", user.value);
      if (loginResponse.data.code === 200) {
        router.push({ name: 'home' });
        ElMessage.success('Login success!');
      } else {
        ElMessage.error('Login failed1.');
      }
    } catch (err) {
      ElMessage.error('Login failed2.');
      console.log('err2: ', err)
      return;
    }
  }
};

</script>

<style scope>
.login-card {
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  margin: auto;
  width: 480px;
  height: 400px;
  padding: 50px;
}

.login-title {
  width: 459px;
  height: 70px;
  font-size: 40px;
  font-family: AlibabaPuHuiTiB;
  color: #333333;
  line-height: 90px;
  letter-spacing: 1px;
  font-weight: 800;
  display: block;
  text-align: left;
}

.login-tip {
  width: 319px;
  height: 70px;
  font-size: 30px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #999999;
  line-height: 90px;
  letter-spacing: 1px;
  display: block;
  text-align: left;
  margin-bottom: 30px;
}
</style>