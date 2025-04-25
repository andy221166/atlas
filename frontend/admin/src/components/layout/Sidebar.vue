<template>
  <el-menu
    default-active="1"
    class="sidebar-menu"
    background-color="#304156"
    text-color="#fff"
    active-text-color="#409EFF"
    router
  >
    <el-menu-item index="/users">
      <el-icon><User /></el-icon>
      <span>Users</span>
    </el-menu-item>
    <el-menu-item index="/products">
      <el-icon><Goods /></el-icon>
      <span>Products</span>
    </el-menu-item>
    <el-menu-item index="/orders">
      <el-icon><List /></el-icon>
      <span>Orders</span>
    </el-menu-item>
  </el-menu>
</template>

<script setup lang="ts">
import { User, Goods, List } from '@element-plus/icons-vue'
</script>

<style scoped>
.sidebar-menu {
  height: 100%;
  border-right: none;
}
</style>
