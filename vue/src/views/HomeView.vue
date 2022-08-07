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
        <el-menu-item index="2">Catalogo</el-menu-item>
        <el-menu-item v-if="isAdmin" index="3">Administração</el-menu-item>
      </el-menu>
    </el-col>
    <el-col :span="20" :xs="{ span: 16 }">
      <cart-component v-if="cart"></cart-component>
      <cart-history-component v-if="history"></cart-history-component>
      <product-component v-if="products"></product-component>
      <admin-product-component v-if="adminEdit"></admin-product-component>
    </el-col>
  </el-row>
</template>

<script>
import { ref } from "vue";
import { ElMessage } from "element-plus";
import CartComponent from "../components/CartComponent.vue";
import CartHistoryComponent from "../components/CartHistoryComponent.vue";
import ProductComponent from "../components/ProductComponent.vue";
import AdminProductComponent from "../components/AdminProductComponent.vue";

export default {
  name: "HomeView",

  components: {
    CartComponent,
    CartHistoryComponent,
    ProductComponent,
    AdminProductComponent,
  },

  data() {
    return {
      activeIndex: ref("0"),
      components: [true, false, false, false],
      userDTO: {},
      client: {},
    };
  },

  computed: {
    isAdmin() {
      if (this.userDTO?.roles) {
        return this.userDTO.roles.includes("ADMIN");
      }
      return false;
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

    adminEdit() {
      return this.components[3];
    }
  },

  async created() {
    const self = this;
    const userDTO = JSON.parse(localStorage.getItem("userDTO"));
    this.userDTO = userDTO;
    const userToken = `Bearer ${userDTO?.token}`;
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

    handleSelect(key) {
      const index = parseInt(key);

      for (let i = 0; i <= 4; i++) {
        this.components[i] = i === index;
      }
    },
  },
};
</script>

<style scoped>
.el-menu {
  height: 100%;
}
</style>
