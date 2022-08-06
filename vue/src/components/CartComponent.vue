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

  props: ["cartId"],

  data() {
    return {
      table: [],
    };
  },

  computed: {
    tableData() {
      return this.table;
    },
  },

  async created() {
    this.table = [
      {
        name: "Debug",
        amount: 1,
        description: "oláaa  sadas",
        price: "124,23",
        sale: "2 por 2",
        totalPrice: "124,23",
      },
    ];
  },
});
</script>
