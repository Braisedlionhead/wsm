<template>
  <div>
    <el-button :icon="Plus" style="width: 120px;" type="primary" @click="addNewOrder">
      创建新订单
    </el-button>
    <el-input v-if="0" style="width: 240px;" placeholder="输入订单ID进行搜索" v-model="input" @keyup.enter="KeyupEnter">
      <template #append>
        <el-icon @click="searchOrderByParams" style="cursor: pointer">
          <Search />
        </el-icon>
      </template>
    </el-input>
    
    <div>
      <el-select v-model="selectedStatus" placeholder="选择状态" @change="searchOrderByParams" style="width: 200px; margin-right: 10px;">
        <el-option label="等待中" value="等待中"></el-option>
        <el-option label="进行中" value="进行中"></el-option>
        <el-option label="已完成" value="已完成"></el-option>
      </el-select>

      <el-select v-model="selectedTeamId" placeholder="选择组别" @change="searchOrderByParams" style="width: 200px;">
        <el-option v-for="team in teams" :label=team :value="team" :key="team"></el-option>
      </el-select>

      <el-icon style="margin-left: 2px; vertical-align: middle;" class="large-icon" @click="clearSearchParam">
        <Refresh />
      </el-icon>
    </div>

    <el-scrollbar max-height="700px">
      <el-table :data="tableData" :header-cell-style="{ background: '#2B2B2B' }" :row-style="{height:'60px'}" stripe>
        <el-table-column prop="orderId" label="订单ID" width="100" />
        <el-table-column prop="partId" label="商品号" width="120">
          <template v-slot="scope">
          {{ PartIdToName(scope.row.partId) }}
        </template>
        </el-table-column>
        <el-table-column prop="quantity" label="数量" width="120" />
        <el-table-column prop="deadline" label="截止日期" width="180" />
        <el-table-column prop="status" label="状态" width="120"/>
        <el-table-column prop="teamId" label="组别" width="100" />
        <el-table-column prop="creator" label="创建者" width="100" />
        <el-table-column label="操作" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-scrollbar>

    <el-dialog v-model="dialogVisible" :title="title" width="30%">
      <el-form ref="orderFormRef" :rules="rules" :model="orderForm" label-width="auto" style="max-width: 600px">
        <el-form-item label="订单ID">
          <el-input  v-if="orderForm.orderId !== null" v-model="orderForm.orderId" disabled />
          <el-input v-else value="自动生成，无需填写" disabled/>
        </el-form-item>
        
        <el-form-item label="Part" prop="partId" required>
          <el-select v-model="orderForm.partId" placeholder="Select a part">
              <el-option
                  v-for="part in parts"
                  :key="part.partId"
                  :label="part.partName"
                  :value="part.partId">
              </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="数量">
          <el-input v-model="orderForm.quantity" type="number" />
        </el-form-item>
        <el-form-item label="截止日期">
          <el-date-picker v-model="orderForm.deadline" type="date" placeholder="选择日期"></el-date-picker>
        </el-form-item>
        <el-form-item label="状态" v-if="orderForm.status !==''">
          <el-select v-model="orderForm.status" placeholder="请选择">
            <el-option label="等待中" value="等待中"></el-option>
            <el-option label="进行中" value="进行中"></el-option>
            <el-option label="已完成" value="已完成"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="チーム" prop="teamId" required>
          <el-select v-if="orderForm.teamId" v-model="orderForm.teamId" placeholder="选择组别" style="width: 200px;">
              <el-option v-for="team in teamsFree" :label=team :value="team" :key="team"></el-option>
          </el-select>
          <el-select v-else-if="teamsFree.length > 0" v-model="newFormTeamId" placeholder="选择组别" @change="setNewFormTeamId" style="width: 200px;">
              <el-option v-for="team in teamsFree" :label=team :value="team" :key="team"></el-option>
          </el-select>
          <el-button v-else type="info" @click="to_router('create_team')">无空闲小组，点击创建</el-button>
        </el-form-item>
        <el-form-item label="创建者" v-show="0">
          <el-input v-if="orderForm.creator !== null" v-model="orderForm.creator" disabled/>
          <el-input v-else value="自动生成，无需填写" disabled/>
        </el-form-item>
      </el-form>
      

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">cancel</el-button>
          <el-button type="primary" @click="submitForm">commit</el-button>
        </div>
      </template>

    </el-dialog>

    <el-pagination background small @size-change="handleSizeChange" @current-change="handleCurrentChange"
      :current-page="pageNum" :page-sizes="[5, 10, 20, 30]" :page-size="pageSize" :pager-count="5"
      layout="total, sizes, prev, pager, next, jumper" :total="total" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { listPageOrder, addOrder, modifyOrder, deleteOrder, listPageByStatusAndTeam } from '@/api/order';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Search, Plus, Refresh } from '@element-plus/icons-vue';
import { getTeamCounts, getFreeTeamId } from '@/api/team';
import to_router from '@/util/common.js';
import { list } from '@/api/part.js';


const tableData = ref([]);
const pageSize = ref(5);
const pageNum = ref(1);
const total = ref(50);
const dialogVisible = ref(false);
const title = ref('编辑订单');
const input = ref('');
let date = new Date();
const teams = ref([]);
const teamsFree = ref([]);
const parts = ref([])

const newFormTeamId = ref()
let addOrMod = ref('add')

const queryParams = ref({
  page: 0,
  size: 2,
});

const orderForm = ref({})

const selectedStatus = ref(null)
const selectedTeamId = ref(null)

const rules = {
  teamId: [{
     required: true,
      message: '请选择组别',
       trigger: 'change' 
  }],
  partId: [{
     required: true,
      message: '请选择Part',
       trigger: 'change'
  }],
}




function PartIdToName(partId) {
  const part = parts.value.find(part => part.partId === partId);
  return part ? part.partName : '';
}

function KeyupEnter() {
  searchOrderByParams();
}

async function getParts(){
  const res = await list();
  parts.value = res.data.data;
}



async function showAllTeamNum() {
  const res = await getTeamCounts();
  teams.value = Array.from({ length: res.data.data }, (v, i) => i + 1);
}


async function showFreeTeamNum() {
  const res = await getFreeTeamId();
  teamsFree.value = res.data.data;
}


async function searchOrderByParams() {
  const res = await listPageByStatusAndTeam({
    pageSize: pageSize.value,
    pageNum: pageNum.value,
    param: {
      status: selectedStatus.value,
      teamId: selectedTeamId.value,
    }
  }
);
  tableData.value = res.data.data;
  total.value = res.data.total;
}

function clearSearchParam() {
  input.value = '';
  selectedStatus.value = null;
  selectedTeamId.value = null;
  getPageListOrder();
}


function globalOrBlock(){
  if(selectedStatus.value || selectedTeamId.value){
    searchOrderByParams();
  }else{
    getPageListOrder();
  }
}


function handleSizeChange(val) {
  pageNum.value = 1;
  pageSize.value = val;
  globalOrBlock();
}

function handleCurrentChange(val) {
  pageNum.value = val;
  globalOrBlock();
}



async function getPageListOrder() {
  const res = await listPageOrder({
    pageSize: pageSize.value,
    pageNum: pageNum.value,
    orderId: input.value,
  });

  if (res.data.code === 200) {
    tableData.value = res.data.data;
    total.value = res.data.total;
  } else {
    ElMessage.error('获取订单列表失败');
  }
}

const orderFormRef = ref(null);


function addNewOrder() {
  addOrMod.value = 'add'
  date.setDate(date.getDate() + 10);
  // 清空表单
  orderForm.value = {
    orderId: null,
    partId: null,
    quantity: '',
    deadline: date,
    status: '',
    roleId: null,
    teamId: null,
  };
  title.value = '创建新订单';
  dialogVisible.value = true;
}

function setNewFormTeamId() {
  orderForm.value.teamId = newFormTeamId.value
}

async function submitForm() {
  let res;
  if(addOrMod.value === 'add'){
    try {
      await orderFormRef.value.validate();
    } catch (error) {
      console.log('Validation failed:', error);
      ElMessage.error('error!');
      return;
    }
    setNewFormTeamId()
    res = await addOrder(orderForm.value);
  }else{
    res = await modifyOrder(orderForm.value);
  }
    if (res.data.code === 200) {
    ElMessage.success('success!');
    getPageListOrder();
  } else {
    ElMessage.error('error');
  }
  dialogVisible.value = false; 
}


async function handleEdit(index, row) {
  addOrMod.value = 'mod'
  orderForm.value = { ...row };
  title.value = '编辑订单';
  dialogVisible.value = true;
}

async function handleDelete(index, row) {
  ElMessageBox.confirm('此操作将永久删除该订单, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })
    .then(async () => {
      const res = await deleteOrder(row.orderId);
      if (res.data.code === 200) {
        ElMessage.success('订单删除成功');
        getPageListOrder();
      } else {
        ElMessage.error('订单删除失败');
      }
    })
    .catch(() => {
      ElMessage.info('已取消删除');
    });
}

onMounted(() => {
  getPageListOrder()
  showAllTeamNum()
  showFreeTeamNum()
  getParts()
});


</script>

<style scoped>
.el-table {
  width: 100%;
  height: 100%;
}


.el-form-item__label {
  margin-left: 2px !important;
}


.large-icon {
  font-size: 24px;
  cursor: pointer;
}

</style>
