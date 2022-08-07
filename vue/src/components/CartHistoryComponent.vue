<template>
  <el-card v-loading="loading">
    <template #header>
      <span class="card-title">Histórico de Pedidos</span>
    </template>
    <el-collapse accordion>
      <el-collapse-item
        v-for="cart in cartsData"
        v-bind:key="cart.id"
        :name="cart.id"
      >
        <template #title>
          <span>
            <b>Pedido de: </b>{{ cart.cartDate }} <b>Preço Total: </b>
            {{ cart.totalPriceFormated }}
          </span>
        </template>
        <el-row justify="center">
          <el-table :data="cart.items" style="width: 100%">
            <el-table-column prop="name" label="Nome" />
            <el-table-column prop="description" label="Descrição" />
            <el-table-column prop="unitPriceFormated" label="Unitário" />
            <el-table-column prop="totalPriceFormated" label="Preço" />
            <el-table-column prop="amount" label="Quantia" />
            <el-table-column prop="promotionDescription" label="Promoção" />
          </el-table>
          <span><b>Nome do receptor: </b>{{ cart.clientName }}</span>
          <span>
            <b>Documento do receptor: </b>
            {{ formatCpf(cart.clientDocument) }}
          </span>
        </el-row>
      </el-collapse-item>
    </el-collapse>
  </el-card>
</template>

<script>
import { defineComponent } from "vue";

export default defineComponent({
  name: "CartHistoryComponent",

  data() {
    return {
      cartHistory: [],
      loading: true,
    };
  },

  computed: {
    cartsData() {
      return this.cartHistory;
    },
  },

  created() {
    this.loadData();
  },

  methods: {
    async loadData() {
      await this.loadCarts();
      if (!this.cartHistory) {
        this.cartHistory == [];
      }
      this.loading = false;
    },

    async loadCarts() {
      const self = this;
      const userDTO = JSON.parse(localStorage.getItem("userDTO"));
      const userToken = `Bearer ${userDTO.token}`;
      const headers = {
        "Content-Type": "application/json",
        Authorization: userToken,
      };

      await fetch(`http://localhost:8000/api/cart/list/`, {
        headers,
      }).then(async (response) => {
        if (response.ok) {
          const data = await response.json();
          const currentFormatter = new Intl.NumberFormat("pt-BR", {
            style: "currency",
            currency: "BRL",
          });
          self.cartHistory = data;
          self.cartHistory.forEach((cart) => {
            cart.totalPriceFormated = currentFormatter.format(cart.totalPrice);
            cart.items.forEach((item) => {
              item.unitPriceFormated = currentFormatter.format(item.unitPrice);
              item.totalPriceFormated = currentFormatter.format(
                item.totalPrice
              );
              if (!item.promotionDescription) {
                item.promotionDescription = "Sem promoção";
              }
            });
          });
          self.cartHistory.reverse();
        }
      });
    },

    formatCpf(value) {
      return value.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, "$1.$2.$3-$4");
    },
  },
});
</script>

<style scoped>
span {
  font-size: 16px;
  margin: 8px;
}
.el-row {
  padding: 0;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

<style scoped>
.el-scrollbar {
  background-color: #ffffff;
}
</style>
