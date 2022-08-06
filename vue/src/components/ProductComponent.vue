<template>
  <el-scrollbar>
    <el-table :data="tableData">
      <el-table-column prop="name" label="Nome" />
      <el-table-column prop="description" label="Descrição" />
      <el-table-column prop="price" label="Preço" />
      <el-table-column prop="sale" label="Promoção" />
    </el-table>
  </el-scrollbar>
</template>

<script>
import { defineComponent } from "vue";
export default defineComponent({
  name: "ProductComponent",

  props: ["productsData"],

  computed: {
    tableData() {
      return this.productsData;
    },
  },
});
</script>
