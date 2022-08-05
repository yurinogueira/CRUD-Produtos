<template>
  <div class="about">
    <h1>This is an about page</h1>
  </div>
</template>

<script>
import { ElMessage } from "element-plus";

export default {
  name: "HomeView",

  data() {
    return {
      userDTO: {},
      client: {},
    };
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
  },
};
</script>

<style scoped>
@media (min-width: 1024px) {
  .about {
    min-height: 100vh;
    display: flex;
    align-items: center;
  }
}
</style>
