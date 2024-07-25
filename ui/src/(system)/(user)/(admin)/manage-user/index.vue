<template>
  <div>
    <div style="display: flex; justify-content: space-between; align-items: center;">
      <el-input style="width: 240px" placeholder="输入昵称" v-model="input" @keyup.enter="KeyupEnter">
        <template #append>
          <el-icon @click="searchUserLike" style="cursor: pointer">
            <Search />
          </el-icon>
        </template>
      </el-input>
      <el-button type="primary" @click="openCreateUserDialog">创建新用户</el-button>
    </div>

    <div v-if="0" style="margin-top: 10px;">
      <el-select v-model="selectedRole" placeholder="Select Role" style="width: 120px;">
        <el-option v-for="role in roleOptions" :key="role.value" :label="role.label" :value="role.value" />
      </el-select>
      <el-select v-model="selectedGroup" placeholder="Select Group" style="width: 120px; margin-left: 10px;">
        <el-option v-for="group in groupOptions" :key="group.value" :label="group.label" :value="group.value" />
      </el-select>
    </div>


    <el-scrollbar max-height="818px">
      <el-table :data="tableData" :row-style="{height:'60px'}">
        <el-table-column prop="userId" label="ID" width="120vw" />
        <el-table-column prop="nickName" label="昵称" width="200vw" />
        <el-table-column prop="sex" label="性別" width="200vw" />
        <el-table-column prop="phone" label="电话" width="200vw" />
        <el-table-column prop="roleId" label="职位" width="200vw">
          <template #default="scope">
            <el-tag :type="showRoleType(scope.row.roleId)" disable-transitions>{{ showRole(scope.row.roleId) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="teamId" label="组号" width="200vw" />
        <el-table-column label="操作" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.$index, scope.row)">Edit</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.$index, scope.row)">Del</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-scrollbar>
    
    <el-dialog v-model="dialogVisible" :title="title" width="30%">
      <el-form ref="userFormRef" :model="userForm" label-width="auto" style="max-width: 600px">
        <el-form-item label="Username">
          <el-input v-model="userForm.userName" />
        </el-form-item>
        <el-form-item label="NickName">
          <el-input v-model="userForm.nickName" />
        </el-form-item>
        <el-form-item label="Sex">
          <el-radio-group v-model="userForm.sex">
            <el-radio value='男'>男</el-radio>
            <el-radio value='女'>女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="Password" v-if="userForm.userId === null">
          <el-input v-model="userForm.password" type="password" />
        </el-form-item>
        <el-form-item label="Confirm the password" v-if="userForm.userId === null">
          <el-input v-model="userForm.confirmPassword" type="password" />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">Cancel</el-button>
          <el-button type="primary" @click="submitForm">
            Submit
          </el-button>
        </div>
      </template>
    </el-dialog>

    <el-pagination background small @size-change="handleSizeChange" @current-change="handleCurrentChange"
      :current-page="pageNum" :page-sizes="[5, 10, 20, 30]" :page-size="pageSize" :pager-count="5"
      layout="total, sizes, prev, pager, next, jumper" :total=total style="" />
  </div>
</template>

<script setup>
import { ref, onBeforeMount, onMounted, computed } from 'vue'
import { list, modify, listPage, findById, addUser, searchByNicknameLike, deleteUser } from '@/api/user';
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search } from '@element-plus/icons-vue'

const tableData = ref([])
let pageSize = ref(5)
let pageNum = ref(1)
let total = ref(50)
let dialogVisible = ref(false)
let title = ref('Edit user')
let input = ref('')
let selectedRole = ref('')
let selectedGroup = ref('')

const roleOptions = [
  { label: 'Role 0', value: '0' },
  { label: 'Role 1', value: '1' },
  { label: 'Role 2', value: '2' }
]


const groupOptions = [
  { label: 'Group 1', value: 1 },
  { label: 'Group 2', value: 2 },
  { label: 'Group 3', value: 3 }
]

const filteredUsers = computed(() => {
  let filtered = tableData.value
  if (selectedRole) {
    filtered = filtered.filter(user => user.role === selectedRole)
  }
  if (selectedGroup) {
    filtered = filtered.filter(user => user.group === selectedGroup)
  }
  return filtered
})

function showRoleType(roleId) {
  return roleId === 0 ? 'danger' : roleId === 1 ? 'primary' : 'success';
}

function showRole(roleId){
  return roleId === 0 ? '超' : roleId === 1 ? '高' : '普通'
}

const querryParams = ref({
  page: 0,
  size: 2,
  userName: '',
  nickName: '',
})

let userForm = ref({
  userId: null,
  userName: '',
  nickName: '',
  sex: '女',
  password: '',
  confirmPassword: '',
})

function openCreateUserDialog() {
  userForm.value = {
    userId: null,
    userName: '',
    nickName: '',
    sex: '女',
    password: '',
    confirmPassword: '',
    role: '',
    group: ''
  }
  title.value = 'Create New User'
  dialogVisible.value = true
}

async function submitForm() {
  if (userForm.value.userId === null) {
    // 创建新用户
    if (userForm.value.password !== userForm.value.confirmPassword) {
      ElMessage.error('Passwords do not match!')
      return
    }
    const res = await addUser(userForm.value)
    if (checkResponse(res)) {
      ElMessage.success('User created successfully!')
      dialogVisible.value = false
      getPageList()
    } else {
      ElMessage.error('Failed to create user.')
    }
  } else {
    // 编辑用户
    const res = await modify(userForm.value)
    if (checkResponse(res)) {
      ElMessage.success('User edited successfully!')
      dialogVisible.value = false
      getPageList()
    } else {
      ElMessage.error('Failed to edit user.')
    }
  }
}

function KeyupEnter() {
  searchUserLike()
}

async function searchUserLike() {
  const res = await searchByNicknameLike({
      pageSize: pageSize.value,
      pageNum: pageNum.value,
      param: {
        nickName: input.value
      }
    })
  tableData.value = res.data.data
  total.value = res.data.total
}

function globalOrBlock(){
  if (input.value) {
    searchUserLike()
  } else {
    getPageList()
  }
}

function handleSizeChange(val) {
  console.log(`每页 ${val} 条`)
  pageNum.value = 1
  pageSize.value = val

  globalOrBlock()
}

function handleCurrentChange(val) {
  console.log(`当前页: ${val}`)
  pageNum.value = val
  
  globalOrBlock()
}

function checkResponse(res) {
  if (res.data.code !== 200) {
    ElMessage.error('Oops, this is a error message.')
    throw new Error(res.data.code)
  }
  return true
}


async function getPageList(index) {
  try {
    const res = await listPage({
      pageSize: pageSize.value,
      pageNum: pageNum.value
    })
    if (checkResponse(res)) {
      tableData.value = res.data.data
      total.value = res.data.total
    } else {
      ElMessage.error('Oops, this is a error message.')
    }
  } catch (err) {
    console.error('Error in getUserList: ', err)
  }
}

async function handleEdit(index, row) {
  try {
    const res = await findById(row.userId)
    if (checkResponse(res)) {
      userForm.value = res.data.data
      title.value = 'Edit user'
      dialogVisible.value = true
    } else {
      ElMessage.error('Oops, this is a error message.')
    }
  } catch (err) {
    console.error('Error in handleEdit: ', err)
  }
}

const handleDelete = function (index, row) {
  ElMessageBox.confirm(
    'this operation will permanently delete the user. Continue?',
    'Warning',
    {
      confirmButtonText: 'Yes',
      cancelButtonText: 'Cancel',
      type: 'warning',
    }
  )
    .then(() => {
      deleteUser(row.userId)
        .then(() => {
          getPageList()
          ElMessage({
            type: 'success',
            message: 'Delete completed',
          })
        })
        .catch((error) => {
          ElMessage({
            type: 'error',
            message: 'Delete failed',
          })
        })
    })
    .catch((error) => {
      ElMessage({
        type: 'info',
        message: 'Delete canceled',
      })
    })
}

onBeforeMount(async () => {
  getPageList()
})

onMounted(() => {
  getPageList()
})
</script>

<style scoped>
.el-table {
  width: 100%;
  height: 100%;
}

.el-form-item__label {
  margin-left: 2px !important;
}
</style>
