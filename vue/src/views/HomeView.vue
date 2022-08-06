<template>
  <el-row>
    <el-col :span="4" :xs="{ span: 8 }">
      <el-menu
        :default-active="activeIndex"
        mode="vertical"
        @select="handleSelect"
      >
        <el-menu-item index="1">Início</el-menu-item>
        <el-menu-item index="2">Produtos</el-menu-item>
        <el-menu-item index="3">Promoções</el-menu-item>
      </el-menu>
    </el-col>
    <el-col :span="20" :xs="{ span: 16 }">
      <cart-component v-if="cart"></cart-component>
    </el-col>
  </el-row>
</template>

<script>
import { ref } from "vue";
import { ElMessage } from "element-plus";
import CartComponent from "../components/CartComponent.vue";

export default {
  name: "HomeView",

  components: {
    CartComponent,
  },

  data() {
    return {
      activeIndex: ref("1"),
      components: [true, false, false],
      userDTO: {},
      client: {},
    };
  },

  computed: {
    cart() {
      return this.components[0];
    },

    products() {
      return this.components[1];
    },

    sales() {
      return this.sales[1];
    },
  },

  async created() {
    const self = this;
    const userDTO = JSON.parse(localStorage.getItem("userDTO"));
    if (!userDTO) {
      console.log("Oiasod");
      this.$router.push({ path: "/" });
      return;
    }

    const userToken = `Bearer ${userDTO.token}`;
    const headers = {
      "Content-Type": "application/json",
      Authorization: userToken,
    };
    let isValid = false;

    await fetch(`http://localhost:8000/api/client/${userDTO.clientId}/`, {
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

    handleSelect(key, keyPath) {
      console.log(key, keyPath);
      const index = parseInt(key) - 1;

      for (let i = 0; i <= 3; i++) {
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
