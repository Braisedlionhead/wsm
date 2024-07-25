<template>
    <AsideMenu @click="OpenOnClose"></AsideMenu>
    <!-- <el-menu :collapse="storage.collapse" :collapse-transition="true" :default-active="$route.path" style="padding-right: 20px;"> -->
    <el-menu :collapse="storage.collapse" :collapse-transition="true" :default-active="$route.path">

        <el-menu-item index="/main" @click="to_router('home')">
            <el-icon>
                <House />
            </el-icon>
            <span>临时主页</span>
        </el-menu-item>

        <el-menu-item index="/member" @click.capture="to_router('member')">
            <el-icon>
                <User />
            </el-icon>
            <span>User Center</span>
        </el-menu-item>

        <el-menu-item index="/part_view" @click="to_router('part_view')">
            <el-icon>
                <Van />
            </el-icon>
            <span>状态信息</span>
        </el-menu-item>

        <el-menu-item index="/order" @click="to_router('order')">
            <el-icon>
                <Tickets />
            </el-icon>
            <span>订单</span>
        </el-menu-item>

        <el-menu-item index="/part" @click="to_router('part')">
            <el-icon>
                <PieChart />
            </el-icon>
            <span>零部件</span>
        </el-menu-item>

        <el-popover placement="right" :width="180" >
            <template #reference>
                <el-menu-item class="menu-bottom" >
                    <el-icon>
                        <setting />
                    </el-icon>
                    <span>設定</span>
                </el-menu-item>
            </template>

            <div style="padding-top: 6px; padding-bottom: 6px;">
                <el-icon style="padding-left: 10px;">
                    <Moon />
                </el-icon>
                <span>深色主题</span>
                <el-switch v-model="isDark" class="mt-2" style="margin-left: 40px;" inline-prompt />
            </div>
        </el-popover>

    </el-menu>

</template>


<script setup>
import { Setting, House, User, Van, Tickets, PieChart } from '@element-plus/icons-vue'
import storage from '@/store/store.js'
import AsideMenu from '@/components/icons/AsideMenu.vue'
import { useDark, useColorMode } from '@vueuse/core'
import { Moon } from '@element-plus/icons-vue'
import to_router from '@/util/common'



const isDark = useDark() // 获取当前系统主题模式 true: dark, false: light
const colorMode = useColorMode() // 获取当前系统主题模式 dark: dark, light: light

const OpenOnClose = () => {
    storage.collapse = !storage.collapse
}

</script>


<style scoped>
.layout-container-demo .el-menu {
    /* height: 100vh; */
    height: calc(100vh - 68px);
    /*这样的话如果屏幕无法显示全部menu，滚动栏会只在menu所在区域显示，而不是整个页面*/
    border-right: none;
}

.el-icon {
    width: 20px;
}

.large-icon {
    font-size: 24px;
    cursor: pointer;
}



.menu-bottom {
  position: absolute; /* 相对于父级定位 */
  bottom: 0; /* 定位到底部 */
  align-self: end;
  width: 100%;
  /* margin-bottom: 60px; */
}


</style>