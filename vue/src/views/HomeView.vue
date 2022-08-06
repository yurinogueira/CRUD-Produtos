<template>
  <el-row>
    <el-col :span="4" :xs="{ span: 8 }">
      <el-menu
        :default-active="activeIndex"
        mode="vertical"
        @select="handleSelect"
      >
        <el-menu-item index="0">Carrinho</el-menu-item>
        <el-menu-item index="1">Histórico</el-menu-item>
        <el-menu-item index="2">Produtos</el-menu-item>
      </el-menu>
    </el-col>
    <el-col :span="20" :xs="{ span: 16 }">
      <cart-component :cartId="1" v-if="cart"></cart-component>
      <product-component
        :productsData="productsDataC"
        v-if="products"
      ></product-component>
    </el-col>
  </el-row>
</template>

<script>
import { ref } from "vue";
import { ElMessage } from "element-plus";
import CartComponent from "../components/CartComponent.vue";
import ProductComponent from "../components/ProductComponent.vue";

export default {
  name: "HomeView",

  components: {
    CartComponent,
    ProductComponent,
  },

  data() {
    return {
      activeIndex: ref("0"),
      components: [true, false, false],
      tableData: [],
      productsData: [],
      userDTO: {},
      client: {},
    };
  },

  computed: {
    productsDataC() {
      return this.productsData;
    },

    cart() {
      return this.components[0];
    },

    history() {
      return this.components[1];
    },

    products() {
      return this.components[2];
    },
  },

  async created() {
    const self = this;
    const userDTO = JSON.parse(localStorage.getItem("userDTO"));
    if (!userDTO) {
      this.$router.push({ path: "/" });
      return;
    }

    const userToken = `Bearer ${userDTO.token}`;
    const headers = {
      "Content-Type": "application/json",
      Authorization: userToken,
    };
    let isValid = false;

    await fetch(`http://localhost:8000/api/client/`, {
      headers,
    })
      .then(async (response) => {
        if (response.ok) {
          isValid = true;
          const data = await response.json();
          localStorage.setItem("client", JSON.stringify(data));
          self.client = data;
          return;
        }
        self.alertExpired();
      })
      .catch(() => {
        self.alertExpired();
      });

    if (!isValid) {
      self.$router.push({ path: "/" });
    }
  },

  methods: {
    alertExpired() {
      localStorage.removeItem("userDTO");
      ElMessage.error("Sua sessão expirou, entre novamente por favor.");
    },

    async handleSelect(key, keyPath) {
      console.log(key, keyPath);
      const index = parseInt(key);
      await this.loadProducts();

      for (let i = 0; i <= 3; i++) {
        this.components[i] = i === index;
      }
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
};
</script>

<style scoped>
.el-menu {
  height: 100%;
}
</style>
