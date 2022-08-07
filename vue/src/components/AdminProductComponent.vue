<template>
  <el-card v-loading="loading">
    <template #header>
      <span class="card-title">Modificar Produtos</span>
    </template>

    <el-collapse accordion>
      <el-collapse-item title="Adicionar um novo produto" name="add">
        <el-form :model="model" :rules="rules" ref="ruleForm">
          <el-row justify="center">
            <el-row justify="center" style="width: 80%">
              <el-form-item class="max-width" prop="name" label="Nome">
                <el-input v-model="model.name" placeholder="Nome..."></el-input>
              </el-form-item>

              <el-form-item class="max-width" prop="sale" label="Promoção">
                <el-select
                  class="max-width"
                  v-model="model.sale"
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
                  v-model="model.description"
                  placeholder="Descrição..."
                ></el-input>
              </el-form-item>

              <el-form-item class="max-width" prop="price" label="Preço">
                <el-input
                  type="number"
                  v-model="model.price"
                  placeholder="Preço..."
                ></el-input>
              </el-form-item>
            </el-row>

            <el-row justify="center" style="width: 60%">
              <el-form-item>
                <el-button type="success" @click="addProduct()">
                  ADICIONAR
                </el-button>
              </el-form-item>
            </el-row>
          </el-row>
        </el-form>
      </el-collapse-item>

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
                <el-button type="success" @click="attProduct(product)">
                  ATUALIZAR
                </el-button>
              </el-form-item>

              <el-form-item>
                <el-button type="danger" @click="rmProduct(product.id)">
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
import { ElMessage } from "element-plus";
import { defineComponent } from "vue";

export default defineComponent({
  name: "AdminProductComponent",

  data() {
    return {
      productsData: [],
      loading: true,
      saleOptions: [],
      model: {
        name: "",
        description: "",
        price: 0,
        sale: -1,
      },
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
    async addProduct() {
      this.loading = true;
      const userDTO = JSON.parse(localStorage.getItem("userDTO"));
      const userToken = `Bearer ${userDTO.token}`;
      const headers = {
        "Content-Type": "application/json",
        Authorization: userToken,
      };
      let sale = this.model.sale;
      if (sale === "Sem promoção" || sale === -1) {
        sale = null;
      }
      const payload = JSON.stringify({
        name: this.model.name,
        description: this.model.description,
        sale: sale,
        price: this.model.price,
      });

      await fetch(`http://localhost:8000/api/product/`, {
        method: "POST",
        headers: headers,
        body: payload,
      })
        .then(async (response) => {
          if (response.ok) {
            ElMessage.success("Produto adicionado com sucesso");
          } else {
            ElMessage.error("Houve um erro ao adicionar o produto");
          }
        })
        .catch(() => ElMessage.error("Ocorreu um erro ao adicionar o produto"));

      this.model.name = "";
      this.model.description = "";
      this.model.price = 0;
      this.model.sale = -1;

      await this.loadProducts();
      this.loading = false;
    },

    async rmProduct(productId) {
      this.loading = true;
      const userDTO = JSON.parse(localStorage.getItem("userDTO"));
      const userToken = `Bearer ${userDTO.token}`;
      const headers = {
        "Content-Type": "application/json",
        Authorization: userToken,
      };

      await fetch(`http://localhost:8000/api/product/${productId}/`, {
        method: "DELETE",
        headers: headers,
      })
        .then(async (response) => {
          if (response.ok) {
            ElMessage.success("Produto removido com sucesso");
          } else {
            ElMessage.error("Ocorreu um erro ao remover o produto");
          }
        })
        .catch(() =>
          ElMessage.error(
            "Não é possível remover esse produto pois ele está contido em um pedido"
          )
        );

      await this.loadProducts();
      this.loading = false;
    },

    async attProduct(product) {
      this.loading = true;
      const userDTO = JSON.parse(localStorage.getItem("userDTO"));
      const userToken = `Bearer ${userDTO.token}`;
      const headers = {
        "Content-Type": "application/json",
        Authorization: userToken,
      };
      let sale = product.sale;
      if (sale === "Sem promoção" || sale === -1) {
        sale = null;
      }
      const payload = JSON.stringify({
        name: product.name,
        description: product.description,
        sale: sale,
        price: product.price,
      });

      await fetch(`http://localhost:8000/api/product/${product.id}/`, {
        method: "PUT",
        headers: headers,
        body: payload,
      })
        .then(async (response) => {
          if (response.ok) {
            ElMessage.success("Produto atualizado com sucesso");
          } else {
            ElMessage.error("Ocorreu um erro ao atualizar o produto");
          }
        })
        .catch(() => ElMessage.error("Ocorreu um erro ao atualizar o produto"));

      await this.loadProducts();
      this.loading = false;
    },

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
      this.saleOptions.push({
        id: -1,
        description: "Sem promoção",
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
          self.productsData.reverse();
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
