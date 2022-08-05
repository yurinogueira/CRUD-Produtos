<script setup>
import BaseHeader from "./layouts/BaseHeader.vue";
import BaseFooter from "./layouts/BaseFooter.vue";
</script>

<template>
  <el-header><BaseHeader /></el-header>
  <el-main>
    <router-view />
  </el-main>
  <el-footer><BaseFooter /></el-footer>
</template>

<style>
#app {
  text-align: center;
}
body {
  margin: 0;
  background-color: #e2e2e2;
}
.el-header {
  padding: 0;
}
.el-footer {
  padding: 0;
}
.el-main {
  padding: 0;
  width: 100%;
  margin-top: 32px;
  margin-bottom: 32px;
}
</style>
