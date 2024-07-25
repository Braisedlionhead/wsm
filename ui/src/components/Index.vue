<template>
  <el-container class="layout-container-demo"  >
    <el-aside :width="aside_width" >
      <Aside></Aside>
    </el-aside>

    <el-container>
      <el-header style="text-align: right; font-size: 12px; border-bottom: darkgray 1px solid;" >
        <Header></Header>
      </el-header>
      <el-main class="main">
        <RouterView/>
      </el-main>
    </el-container>

  </el-container>
</template>


<script setup>
import Aside from './Aside.vue'
import Header from './Header.vue'
// import store from '@/store';

const aside_width = 'store.collapse ? "200px" : "64px"' 

</script>

<style scoped>
.layout-container-demo .el-main {
  height: 100%;
  padding: 0;
  /* 此处只要不为0， 效果都一样？ */
}

#app {
  height: 100%;
}


.layout-container-demo .el-aside {
  margin-left: -1px;
}

.el-header{
  --el-header-height: 50px;
}

</style>