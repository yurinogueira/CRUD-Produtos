<template>
  <el-row type="flex" justify="center">
    <el-card>
      <template #header>
        <span class="card-title">Já possui um cadastro? Faça login</span>
      </template>

      <el-form :model="model" :rules="rules" ref="ruleForm">
        <el-form-item prop="email">
          <el-input
            placeholder="Email..."
            :prefix-icon="lock"
            v-model="model.email"
          ></el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            placeholder="Senha..."
            type="password"
            :prefix-icon="lock"
            v-model="model.password"
            show-password
          ></el-input>
        </el-form-item>

        <el-row type="flex" justify="center">
          <el-button type="primary" :loading="loading" @click="login">ENTRAR</el-button>
        </el-row>
      </el-form>
    </el-card>
  </el-row>
</template>

<script>
import { shallowRef } from "vue";
import { Lock, Message } from "@element-plus/icons-vue";
import { ElMessage, } from "element-plus";


export default {
  name: "EnterView",

  data() {
    return {
      loading: false,
      lock: shallowRef(Lock),
      message: shallowRef(Message),
      model: {
        email: "",
        password: "",
      },
      rules: {
        email: [
          {
            required: true,
            message: "Insira um endereço de email",
            trigger: "blur",
          },
          {
            type: "email",
            message: "Insira um endereço de email válido",
            trigger: "blur",
          },
        ],
        password: [
          { required: true, message: "Insira uma senha", trigger: "blur" },
          {
            min: 5,
            max: 20,
            message: "Escolha uma senha entre 5 à 20 caracteres",
            trigger: "blur",
          },
        ],
      },
    };
  },

  async created() {
    const userToken = localStorage.getItem("userToken");
    if (!userToken) {
      return;
    }

    const headers = { "Content-Type": "application/json" };
    const payload = JSON.stringify({ token: userToken });
    let isValid = true;

    await fetch("http://localhost:8000/api/user/check/", {
      method: "POST",
      headers: headers,
      body: payload,
    }).then((response) => {
      if (!response.ok) {
        this.alertExpired();
        isValid = false;
        return;
      }
    }).catch(() => {
      this.alertExpired();
      isValid = false;
      return;
    });

    if (isValid) {
      this.$router.push({ path: "/home" });
    }
  },

  methods: {
    async login() {
      const self = this;
      const form = this.$refs.ruleForm;
      self.loading = true;

      await form.validate((valid, fields) => {
        if (!valid) {
          self.loading = false;
          ElMessage({
            message: "Preencha todos os campos corretamente.",
            type: "warning",
          });
        }
      });
    },

    alertExpired() {
      localStorage.removeItem("userToken");
      ElMessage.error("Sua sessão expirou, entre novamente por favor.");
    },
  },
};
</script>

<style scoped>
.el-row {
  padding-top: 32px;
  padding-bottom: 32px;
}
.el-card {
  width: 512px;
}
.card-title {
  color: #4d5758;
  font-weight: bolder;
  font-size: large;
}
.el-form-item {
  text-align: center;
}
.el-button {
  width: 256px;
  height: 48px;
  font-size: large;
}
</style>
