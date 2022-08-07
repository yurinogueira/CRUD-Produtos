<template>
  <el-scrollbar>
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
  </el-scrollbar>
</template>

<script>
import { defineComponent } from "vue";
export default defineComponent({
  name: "CartHistoryComponent",

  props: ["cartHistory"],

  computed: {
    cartsData() {
      return this.cartHistory;
    },
  },

  async created() {
    if (!this.cartHistory) {
      this.cartHistory == [];
    }
  },

  methods: {
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
.el-card {
  width: 95%;
  box-shadow: 0 0;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
