<template>
  <el-scrollbar>
    <el-table :data="tableData">
      <el-table-column prop="name" label="Nome" />
      <el-table-column prop="amount" label="Quantia" />
      <el-table-column prop="price" label="Preço" />
      <el-table-column prop="description" label="Descrição" />
      <el-table-column prop="sale" label="Promoção" />
      <el-table-column prop="totalPrice" label="Total" />
    </el-table>
  </el-scrollbar>
</template>

<script>
import { defineComponent } from "vue";
export default defineComponent({
  name: "CartComponent",
})
</script>
