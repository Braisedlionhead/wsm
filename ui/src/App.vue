<template>
  <RouterView/>
</template>


<script setup>
import {onMounted} from 'vue'
import store from '@/store'

onMounted(() => {
  store.dispatch('RECOVERY_USER') // 暂时先放这里
})
</script>