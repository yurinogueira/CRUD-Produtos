<template>
  <el-scrollbar>
    <el-table v-loading="loading" :data="tableData">
      <el-table-column prop="name" label="Nome" :fit="true" />
      <el-table-column prop="description" label="Descrição" :fit="true" />
      <el-table-column prop="priceFormated" label="Preço Unitário" />
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
          <el-button disabled round type="primary">
            {{ cartAmount[scope.row.id] || 0 }}
          </el-button>
          <el-button size="small" type="success" @click="handleAdd(scope.row)">
            +
          </el-button>
          <el-button size="small" type="danger" @click="handleRm(scope.row)">
            -
          </el-button>
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
      loading: true,
      search: "",
      cart: {},
    };
  },

  async created() {
    const actualCart = JSON.parse(localStorage.getItem("actualCart")) || {};
    this.cart = actualCart;
    this.loading = false;
  },

  methods: {
    handleAdd(row) {
      const index = row.id;
      if (!this.cart[index]) {
        this.cart[index] = 0;
      }
      this.cart[index] += 1;
      localStorage.setItem("actualCart", JSON.stringify(this.cart));
    },

    handleRm(row) {
      const index = row.id;
      if (!this.cart[index] || this.cart[index] === 0) {
        return;
      }
      this.cart[index] -= 1;
      localStorage.setItem("actualCart", JSON.stringify(this.cart));
    },
  },

  computed: {
    cartAmount() {
      return this.cart;
    },

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
