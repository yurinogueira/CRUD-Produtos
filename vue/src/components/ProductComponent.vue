<template>
  <el-scrollbar>
    <el-table :data="tableData">
      <el-table-column prop="name" label="Nome" :fit="true" />
      <el-table-column prop="description" label="Descrição" :fit="true" />
      <el-table-column prop="price" label="Preço" />
      <el-table-column prop="sale" label="Promoção" />
      <el-table-column align="right">
        <template #header>
          <el-input
            v-model="search"
            size="small"
            placeholder="Pesquisar por nome do produto..."
          />
        </template>
        <template #default="scope">
          <el-button
            size="small"
            type="danger"
            @click="handleDelete(scope.$index, scope.row)"
            >ADICIONAR AO CARRINHO</el-button
          >
        </template>
      </el-table-column>
    </el-table>
  </el-scrollbar>
</template>

<script>
import { defineComponent } from "vue";

export default defineComponent({
  name: "ProductComponent",

  props: ["productsData"],

  data() {
    return {
      search: "",
    };
  },

  computed: {
    tableData() {
      return this.productsData.filter(
        (product) =>
          !this.search ||
          product.name.toLowerCase().includes(this.search.toLowerCase())
      );
    },
  },
});
</script>
