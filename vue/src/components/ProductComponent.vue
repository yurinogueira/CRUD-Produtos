<template>
  <el-card v-loading="loading">
    <template #header>
      <span class="card-title">Catalogo</span>
    </template>
    <el-table :data="tableData" empty-text="Nenhum produto cadastrado">
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
            {{ cartAmount[scope.row.id]?.amount || 0 }}
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
  </el-card>
</template>

<script>
import { defineComponent } from "vue";

export default defineComponent({
  name: "ProductComponent",

  data() {
    return {
      loading: true,
      productsData: [],
      search: "",
      cart: {},
    };
  },

  created() {
    this.loadData();
  },

  methods: {
    async loadData() {
      const actualCart = JSON.parse(localStorage.getItem("actualCart")) || {};
      this.cart = actualCart;
      await this.loadProducts();
      this.loading = false;
    },

    async loadProducts() {
      const self = this;
      const userDTO = JSON.parse(localStorage.getItem("userDTO"));
      const userToken = `Bearer ${userDTO.token}`;
      const headers = {
        "Content-Type": "application/json",
        Authorization: userToken,
      };

      await fetch(`http://localhost:8000/api/product/`, {
        headers,
      }).then(async (response) => {
        if (response.ok) {
          const data = await response.json();
          const currentFormatter = new Intl.NumberFormat("pt-BR", {
            style: "currency",
            currency: "BRL",
          });
          self.productsData = data;
          self.productsData.forEach((product) => {
            product.priceFormated = currentFormatter.format(product.price);
            if (!product.sale) {
              product.sale = "Sem promoção";
            } else {
              product.sale = product.sale.description;
            }
          });
        }
      });
    },

    handleAdd(row) {
      const index = row.id;
      if (!this.cart[index]) {
        this.cart[index] = { amount: 0 };
      }
      const amount = this.cart[index].amount + 1;
      this.cart[index] = row;
      this.cart[index].amount = amount;
      localStorage.setItem("actualCart", JSON.stringify(this.cart));
    },

    handleRm(row) {
      const index = row.id;
      if (!this.cart[index] || this.cart[index].amount === 0) {
        return;
      }
      const amount = this.cart[index].amount - 1;
      this.cart[index] = row;
      this.cart[index].amount = amount;
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

<style scoped>
.el-scrollbar {
  background-color: #ffffff;
}
</style>
