<template>
  <el-card v-loading="loading">
    <template #header>
      <span class="card-title">Modificar Produtos</span>
    </template>

    <el-collapse accordion>
      <el-collapse-item
        v-for="(product, index) in actualProducts"
        v-bind:key="product.id"
        :title="product.name"
        :name="index"
      >
        <el-form :model="product" :rules="rules" ref="tempRuleForm">
          <el-row justify="center">
            <el-row justify="center" style="width: 80%">
              <el-form-item class="max-width" prop="name" label="Nome">
                <el-input
                  v-model="product.name"
                  placeholder="Nome..."
                ></el-input>
              </el-form-item>

              <el-form-item class="max-width" prop="sale" label="Promoção">
                <el-select
                  class="max-width"
                  v-model="product.sale"
                  placeholder="Promoção..."
                  filterable
                >
                  <el-option
                    v-for="option in saleOptions"
                    v-bind:key="option.id"
                    :value="option.id"
                    :label="option.description"
                  ></el-option>
                </el-select>
              </el-form-item>

              <el-form-item
                class="max-width"
                prop="description"
                label="Descrição"
              >
                <el-input
                  v-model="product.description"
                  placeholder="Descrição..."
                ></el-input>
              </el-form-item>

              <el-form-item class="max-width" prop="price" label="Preço">
                <el-input
                  type="number"
                  v-model="product.price"
                  placeholder="Preço..."
                ></el-input>
              </el-form-item>
            </el-row>

            <el-row justify="space-between" style="width: 60%">
              <el-form-item>
                <el-button
                  type="success"
                  @click="attBankAccount(product, index)"
                >
                  ATUALIZAR
                </el-button>
              </el-form-item>

              <el-form-item>
                <el-button
                  type="danger"
                  @click="removeBankAccount(product.id, index)"
                >
                  REMOVER
                </el-button>
              </el-form-item>
            </el-row>
          </el-row>
        </el-form>
      </el-collapse-item>
    </el-collapse>
  </el-card>
</template>

<script>
import { defineComponent } from "vue";

export default defineComponent({
  name: "AdminProductComponent",

  data() {
    return {
      productsData: [],
      loading: true,
      saleOptions: [],
      rules: {
        name: [{ required: true, message: "Insira um nome", trigger: "blur" }],
        description: [
          { required: true, message: "Insira uma descrição", trigger: "blur" },
        ],
        price: [
          { required: true, message: "Insira uma preço", trigger: "blur" },
        ],
      },
    };
  },

  computed: {
    actualProducts() {
      return this.productsData;
    },
  },

  created() {
    this.loadData();
  },

  methods: {
    async loadData() {
      const userDTO = JSON.parse(localStorage.getItem("userDTO"));
      const userToken = `Bearer ${userDTO.token}`;
      const headers = {
        "Content-Type": "application/json",
        Authorization: userToken,
      };

      await fetch(`http://localhost:8000/api/sale/`, {
        headers,
      }).then(async (response) => {
        if (response.ok) {
          this.saleOptions = await response.json();
        }
      });
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
  },
});
</script>

<style scoped>
.max-width {
  width: 100%;
}
.el-form-item {
  margin: 8px;
}
</style>
