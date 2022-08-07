<template>
  <el-scrollbar>
    <el-table v-loading="loading" :data="cartsData" empty-text="Carrinho vazio">
      <el-table-column prop="name" label="Nome" :fit="true" />
      <el-table-column prop="description" label="Descrição" :fit="true" />
      <el-table-column prop="priceFormated" label="Preço Unitário" />
      <el-table-column prop="sale" label="Promoção" />
      <el-table-column align="right" label="Quantia">
        <template #default="scope">
          <el-button disabled round type="primary">
            {{ carts[scope.row.id]?.amount || 0 }}
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
    <el-row justify="center">
      <el-col :span="4">
        <el-button type="success" disabled>
          TOTAL: {{ cartTotalPrice }}
        </el-button>
      </el-col>
      <el-col :span="4">
        <el-button type="primary" @click="finishRequest()">
          FINALIZAR PEDIDO!
        </el-button>
      </el-col>
    </el-row>
  </el-scrollbar>
</template>

<script>
import { defineComponent } from "vue";
import { ElMessage } from "element-plus";

export default defineComponent({
  name: "CartComponent",

  data() {
    return {
      cart: {},
      loading: true,
      cartPrice: 0,
    };
  },

  computed: {
    cartsData() {
      let cartsData = [];
      for (let key in this.cart) {
        cartsData.push(this.cart[key]);
      }
      return cartsData;
    },

    carts() {
      return this.cart;
    },

    cartTotalPrice() {
      const currentFormatter = new Intl.NumberFormat("pt-BR", {
        style: "currency",
        currency: "BRL",
      });
      return currentFormatter.format(this.cartPrice);
    },
  },

  methods: {
    async finishRequest() {
      if (!(this.cartsData.length > 0)) {
        ElMessage.error(
          "O carrinho não pode estar vazio para finalizar o pedido."
        );
        return;
      }

      this.loading = true;
      const items = this.getItems();
      const userDTO = JSON.parse(localStorage.getItem("userDTO"));
      const userToken = `Bearer ${userDTO.token}`;
      const headers = {
        "Content-Type": "application/json",
        Authorization: userToken,
      };
      const payload = JSON.stringify({ items });

      await fetch(`http://localhost:8000/api/cart/`, {
        method: "POST",
        headers: headers,
        body: payload,
      }).then(async (response) => {
        if (response.ok) {
          ElMessage.success("Pedido registrado com sucesso!");
          this.cart = {};
          this.cartPrice = 0;
          localStorage.setItem("actualCart", JSON.stringify(this.cart));
        }
      });
      this.loading = false;
    },

    async handleAdd(row) {
      this.loading = true;
      const index = row.id;
      const amount = this.cart[index].amount + 1;
      this.cart[index] = row;
      this.cart[index].amount = amount;
      localStorage.setItem("actualCart", JSON.stringify(this.cart));
      this.cartPrice = await this.getCartTotalPrice();
      this.loading = false;
    },

    async handleRm(row) {
      this.loading = true;
      const index = row.id;
      const amount = this.cart[index].amount - 1;
      if (amount === 0) {
        delete this.cart[index];
      } else {
        this.cart[index] = row;
        this.cart[index].amount = amount;
      }
      localStorage.setItem("actualCart", JSON.stringify(this.cart));
      this.cartPrice = await this.getCartTotalPrice();
      this.loading = false;
    },

    getItems() {
      let listItems = [];
      for (let key in this.cart) {
        listItems.push({
          product: this.cart[key].id,
          amount: this.cart[key].amount,
        });
      }
      return listItems;
    },

    async getCartTotalPrice() {
      const listItems = this.getItems();
      const userDTO = JSON.parse(localStorage.getItem("userDTO"));
      const userToken = `Bearer ${userDTO.token}`;
      const headers = {
        "Content-Type": "application/json",
        Authorization: userToken,
      };
      const payload = JSON.stringify(listItems);
      let result = 0.0;

      await fetch(`http://localhost:8000/api/product/calculate/`, {
        method: "POST",
        headers: headers,
        body: payload,
      }).then(async (response) => {
        if (response.ok) {
          result = await response.json();
        }
      });

      return result;
    },
  },

  async created() {
    const actualCart = JSON.parse(localStorage.getItem("actualCart")) || {};
    this.cart = actualCart;
    this.cartPrice = await this.getCartTotalPrice();
    this.loading = false;
  },
});
</script>

<style scoped>
.el-scrollbar {
  background-color: #ffffff;
}
.el-row {
  margin: 8px;
}
</style>
