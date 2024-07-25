<template>
    <div class="home-container">
        <h1 class="title">Create a new Order and Team</h1>
        <p class="description">\>_.../</p>
        <p class="description">\$_.../</p>
        <p class="description">\#_.../</p>
    </div>
    <div class="form-container">
    <el-form ref="teamFormRef" :model="teamForm" @submit.native.prevent="submitForm" label-width="auto" style="max-width: 400px;">
        <el-form-item label="Team ID">
            <el-input value="自动生成" readonly />
        </el-form-item>
        <el-form-item label="Order ID">
            <el-input value="自动生成" disabled  />
        </el-form-item>
        <el-form-item label="Part ID" required>
          <el-select v-model="teamForm.partId" placeholder="Select a part">
              <el-option
                  v-for="part in parts"
                  :key="part.partId"
                  :label="part.partName"
                  :value="part.partId">
              </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="数" required>
            <el-input-number v-model="teamForm.quantity" controls-position="right" :min="1" />
        </el-form-item>
        <el-form-item label="Deadline" required>
            <el-date-picker v-model="teamForm.deadline" type="date" placeholder="Pick a date" />
        </el-form-item>

        <el-form-item label="Team Members" required>
            <el-input v-model="teamForm.teamMembersId" />
        </el-form-item>

        <el-form-item label="Team Members" required style="display: flex">
          <el-select v-model="teamForm.teamMembersId" placeholder="Select" multiple >
            <el-option
              v-for="user in users"
              :key="user.userId"
              :label="user.userName"
              :value="user.userId">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
            <el-button type="primary" native-type="submit">Create Team</el-button>
        </el-form-item>
    </el-form>
  </div>
  {{teamForm.deadline}}
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getNoTeamUser } from '@/api/user.js';
import { list } from '@/api/part.js';
import { addTeam } from '@/api/team.js';
import { ElMessage } from 'element-plus'


const teamForm = ref({
  deadline: new Date(),
  quantity: 10,
});

const users = ref([])
const parts = ref([])
const requiredUserNums = ref(10);


const submitForm = async () => {
    const res = await addTeam(teamForm.value);
    if(res.data.code === 200) {
      ElMessage.success('Success!');
    }
    else {
      ElMessage.error('Create Faild!.');
    }
};


const initForm = async () => {
  const resUsers = await getNoTeamUser(requiredUserNums.value);
  users.value = resUsers.data.data;
  const resParts = await list();
  parts.value = resParts.data.data;
};


onMounted(async () => {
  const newDeadline = new Date(teamForm.value.deadline);
  newDeadline.setDate(newDeadline.getDate() + 10);
  teamForm.value.deadline = new Date(newDeadline);
  initForm();
});

</script>

<style scoped>
.home-container {
  padding: 20px;
}

.title {
  font-size: 32px;
  margin-bottom: 20px;
}

.description {
  font-size: 18px;
  color: #666;
}

.form-container{
  /* display: flex; */
  /* justify-content: center; */
  /* text-align: center; */
  /* align-items: center; */
}

</style>
