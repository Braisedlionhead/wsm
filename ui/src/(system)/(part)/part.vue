<template>
    <div class="container">
        <div class="left">
            <div class="cards">
                <el-card class="card" v-for="(item, index) in cardData" :key="index" :style="{gridColumn: index === 0 ? 'span 2' : 'span 1', gridRow: index < 2 ? 1 : 2}" shadow="hover">
                    <p>{{ item.title }}</p>
                    <div :style="{color: item.color}">
                    <p class="contents" >{{ item.number }}</p>
                    <p class="info">total</p>
                </div>
                </el-card>
            </div>
            <el-card shadow="hover">
                <div ref="chart4" class="chart"></div>
            </el-card>
        </div>

        <div class="right">
            <el-card class="custom-card" shadow="hover">
                <div ref="chart1" class="chart"></div>
            </el-card>
            <el-card class="custom-card" shadow="hover">
                <div ref="chart2" class="chart"></div>
            </el-card>
        </div>
    </div>
</template>


<script setup>
import { ref, onMounted, watch } from 'vue';
import * as echarts from 'echarts';
import { list } from '@/api/part.js'
import { showStatus } from '@/api/order.js'
import { useColorMode } from '@vueuse/core'
import { getWorkingTeams } from '@/api/team.js'


const theme = useColorMode()
const chart1 = ref(null);
const chart2 = ref(null);
const chart4 = ref(null);

var myChart1 = null;
var myChart2 = null;
var myChart4 = null;


const orderStatusNum = ref([]);
const chartsData1 = ref([]);
const chartsData2 = ref([]);


const cardData = ref([
    { title: '生产线', number: 0, color: 'green', key:'total' },
    { title: '异常', number: 0, color: 'red', key: 'error' },
    { title: '进行中', number: 0, color: 'blue', key: 'processing' },
    { title: '已完成', number: 0, color: 'skyblue', key: 'completed'},
    { title: '等待中', number: 0, color: 'orange', key: 'waiting'},
]);


const option1 = {
  backgroundColor: 'transparent',
  title: {
      text: '生产数量'
  },
  tooltip: {},
  xAxis: {
      data: chartsData1.value.map(item => item.teamId),
      axisLabel: {
          fontSize: 10, // 减小字体大小
          rotate: 45, // 旋转标签
      }
  },
  yAxis: {},
  series: [{
      type: 'bar',
      data: chartsData1.value.map(item => item.producedQuantity),
  }]
}

const option2 = {
  backgroundColor: 'transparent',
    title: {
        text: '库存',
        subtext: 'Real Data',
        left: 'center',
    },
    tooltip: {
        trigger: 'item'
    },
    legend: {
        // 此处若不定义data，则会默认使用series中的data
        orient: 'vertical',
        left: 'left',
    },
    series: [
        {
            type: 'pie',
            radius: '50%',
            data: chartsData2.value.map(item => ({ value: item.stock, name: item.partName })),
            emphasis: {
                itemStyle: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            }
        }
    ]
};

const option4 = {
  backgroundColor: 'transparent',
  title: {
    text: 'Comming soon'
  },
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    data: ['Step Start', 'Step Middle', 'Step End']
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  toolbox: {
    feature: {
      saveAsImage: {}
    }
  },
  xAxis: {
    type: 'category',
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
  },
  yAxis: {
    type: 'value'
  },
  // series: [
  //   {
  //     name: 'Step Start',
  //     type: 'line',
  //     step: 'start',
  //     data: [120, 132, 101, 134, 90, 230, 210]
  //   },
  //   {
  //     name: 'Step Middle',
  //     type: 'line',
  //     step: 'middle',
  //     data: [220, 282, 201, 234, 290, 430, 410]
  //   },
  //   {
  //     name: 'Step End',
  //     type: 'line',
  //     step: 'end',
  //     data: [450, 432, 401, 454, 590, 530, 510]
  //   }
  // ]
};


const getChartsData1 = async () => {
    const res = await getWorkingTeams();
    if(res.data.code !== 200) {
        return;
    }
    chartsData1.value = res.data.data;
    myChart1.setOption({
        xAxis: {
            data: res.data.data.map(item => item.teamId),
        },
        series: [
            {
                data: res.data.data.map(item => item.producedQuantity),
            }
        ]
    });
}

const getChartsData2 = async () => {
    const res = await list(); // 获取图表数据
    if(res.data.code !== 200) {
        return;
    }
    chartsData2.value = res.data.data;
    myChart2.setOption({
        series: [
            {
                data: chartsData2.value.map(item => ({ value: item.stock, name: item.partName })),
            }
        ]
    });
    myChart2.hideLoading();
}


const getOrdersStatus = async() => {
  const res = await showStatus();
  if(res.data.code !== 200) {
    return;
  }
  orderStatusNum.value = res.data.data;
  cardData.value = cardData.value.map(item => {
  return {
    ...item,
    number: orderStatusNum.value[item.key]
  };
  });
}




const initializeCharts = (async() => {
  myChart1 = echarts.init(chart1.value, theme.value);
  myChart2 = echarts.init(chart2.value, theme.value);
  myChart1.setOption(option1);
  myChart2.showLoading();
  myChart2.setOption(option2);
  getChartsData1();
  getChartsData2();
})

// 改变echarts主题
watch(theme, async ()=>{
  myChart1.dispose();
  myChart2.dispose();
  initializeCharts();
})


onMounted( async () => {
  initializeCharts();
  // 获取图表数据
  getChartsData1();
  getChartsData2();
  // 获取订单状态
  getOrdersStatus();
});

</script>



<style scoped>
.container {
    display: flex;
    flex-direction: row;
    gap: 20px;
    margin-top: 10px;
    height: 800px;
}


.contents{
    font-size: 48px;
    text-align: center;
    margin: 30px;
}

.info{
    font-size: 16px;
    margin-top: -30px;
    margin-bottom: 20px;
    text-align: center;
}

.left {
    display: flex;
    flex-direction: column;
    gap: 20px;
    flex: 1;
}

.right {
    display: flex;
    flex-direction: column;
    gap: 20px;
    flex: 1;
}

.cards {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    grid-template-rows: 1fr 1fr;
    gap: 20px;
}

.custom-card {
    flex: 1;
    margin: 0;
    padding: 0;
}

.chart {
    flex: 1;
    height: 390px;
    color: #CFD3DC;
}

.el-card.dark{
    --el-card-padding: 10px;
}


</style>