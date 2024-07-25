<template>
  <div>

    <el-input v-if="1" style="width: 240px;" placeholder="输入产品名称搜索" v-model="input" @keyup.enter="KeyupEnter">
      <template #append>
        <el-icon @click="searchUserLike" style="cursor: pointer">
          <Search />
        </el-icon>
      </template>
    </el-input>


    <el-scrollbar max-height="700px">
      <el-table :data="tableData" :header-cell-style="{ background: '#2B2B2B' }" :row-style="{ height: '60px' }" stripe>
        <el-table-column prop="partId" label="产品ID" width="100" />
        <el-table-column prop="partName" label="名称" width="120" />
        <el-table-column prop="description" label="描述" width="120" />
        <el-table-column prop="cost" label="价格" width="120" />
        <el-table-column prop="supplierId" label="供应商" width="120" />
        <el-table-column prop="stock" label="库存" width="120" />
        <el-table-column label="操作" fixed="right">
          <template #default="scope">
            <el-button size="small" type="info" @click="Delivery(scope.$index, scope.row)">交付</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-scrollbar>

    <el-dialog v-model="dialogVisible" :title="title" width="30%">


      <el-checkbox-group v-model="selectedOrderIds">
        <div v-for="(order, index) in orders" :key="order.orderId">
          <el-checkbox :value="order.orderId">
            订单{{ order.orderId }}：需求量：{{ order.quantity }}
          </el-checkbox>
        </div>
      </el-checkbox-group>

      <template #footer>
        <div class="dialog-footer">
          <span :style="stockStyle">库存：{{ remainingStock }}</span> <br />
          <el-button @click="cancelForm">cancel</el-button>
          <el-button type="primary" @click="submitForm" :disabled="isStockNegative">commit</el-button>
        </div>
      </template>
    </el-dialog>

    <el-pagination background small @size-change="handleSizeChange" @current-change="handleCurrentChange"
      :current-page="pageNum" :page-sizes="[5, 10, 20, 30]" :page-size="pageSize" :pager-count="5"
      layout="total, sizes, prev, pager, next, jumper" :total="total" />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { ElMessage } from 'element-plus';
import { Search} from '@element-plus/icons-vue';
import { listPagePart, likePagePart, delivery } from '@/api/part.js';
import { findByPartId } from '@/api/order.js';


const tableData = ref([]);
const pageSize = ref(5);
const pageNum = ref(1);
const total = ref(50);
const dialogVisible = ref(false);
const title = ref('交付订单');
const input = ref('');
const stock = ref(0);
const orders = ref([]);
const partId = ref(null);
const selectedOrderIds = ref([]);

function KeyupEnter() {
  searchUserLike();
}

async function searchUserLike() {
  const res = await likePagePart({
    pageSize: pageSize.value,
    pageNum: pageNum.value,
    param: {
      partName: input.value
    }
  })
  tableData.value = res.data.data
  total.value = res.data.total
}


function globalOrBlock() {
  if(input.value !== ''){
    searchUserLike()
  }else{
    getPageListPart();
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



async function getPageListPart() {
  const res = await listPagePart({
    pageSize: pageSize.value,
    pageNum: pageNum.value,
  });

  console.log(res.data)
  if (res.data.code === 200) {
    tableData.value = res.data.data;
    total.value = res.data.total;
  } else {
    ElMessage.error('获取订单列表失败');
  }
}


function cancelForm(){
  clearSelectedOrders();
  dialogVisible.value = false
}


async function submitForm() {
  const res = await delivery({
    partId: partId.value,
    orderIds: selectedOrderIds.value
  });

  if(res.data.code === 200){
    clearSelectedOrders();
    dialogVisible.value = false; 
    getPageListPart();
    ElMessage.success("success")
  }else{
    ElMessage.error("failed")
  }

}

// 清除选中数据
function clearSelectedOrders() {
  selectedOrderIds.value = []
}

// 是否报警
let stockStyle = computed(() => {
  return remainingStock.value < 0 ? { color: 'red' } : {};
});

// 剩余库存
let remainingStock = computed(() => {
  let totalQuantity = 0;
  orders.value.forEach(order => {
    if (selectedOrderIds.value.includes(order.orderId)) {
      totalQuantity += order.quantity;
    }
  });
  return stock.value - totalQuantity;
});

// 能否提交
let isStockNegative = computed(() => {
  return remainingStock.value < 0;
});

// 交付
async function Delivery(index, row) {
  partId.value = row.partId
  stock.value = row.stock
  const res = await findByPartId(row.partId);
  orders.value = res.data.data
  dialogVisible.value = true;
}



onMounted(() => {
  getPageListPart()
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
