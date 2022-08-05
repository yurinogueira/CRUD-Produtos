<template>
  <el-row type="flex" justify="center">
    <el-card>
      <template #header>
        <span class="card-title">Cadastro Inicial</span>
      </template>

      <el-form :model="model" :rules="rules" ref="ruleForm">
        <el-form-item prop="name">
          <el-input
            placeholder="Nome..."
            :prefix-icon="user"
            v-model="model.name"
          ></el-input>
        </el-form-item>

        <el-form-item prop="surname">
          <el-input
            placeholder="Sobrenome..."
            :prefix-icon="userFilled"
            v-model="model.surname"
          ></el-input>
        </el-form-item>

        <el-form-item prop="document">
          <el-input
            placeholder="CPF..."
            :prefix-icon="user"
            v-model="model.document"
          ></el-input>
        </el-form-item>

        <el-form-item prop="email">
          <el-input
            placeholder="Email..."
            :prefix-icon="message"
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

        <el-form-item prop="confirmPassword">
          <el-input
            placeholder="Confirme sua senha..."
            type="password"
            :prefix-icon="lock"
            v-model="model.confirmPassword"
            show-password
          ></el-input>
        </el-form-item>

        <el-row type="flex" justify="center">
          <el-col>
            <el-button type="primary" :loading="loading" @click="register">
              CADASTRAR-SE
            </el-button>
          </el-col>
          <el-col>
            <el-link type="primary" @click="login">
              Já possui cadastro?
            </el-link>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
  </el-row>
</template>

<script>
import { shallowRef } from "vue";
import { cpf } from "cpf-cnpj-validator";
import { Lock, Message, User, UserFilled } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";

export default {
  name: "RegisterView",

  data() {
    const validatePass = (rule, value, callback) => {
      if (!value) {
        callback(new Error("Insira sua senha novamente"));
      } else if (value !== this.model.password) {
        callback(new Error("As senhas não são iguais"));
      } else {
        callback();
      }
    };

    const validateDocument = (rule, value, callback) => {
      if (!value) {
        callback(new Error("Insira um documento"));
      } else if (!cpf.isValid(value)) {
        callback(new Error("CPF inválido"));
      } else {
        callback();
      }
    };

    return {
      loading: false,
      lock: shallowRef(Lock),
      message: shallowRef(Message),
      user: shallowRef(User),
      userFilled: shallowRef(UserFilled),
      model: {
        email: "",
        password: "",
      },
      rules: {
        name: [
          { required: true, message: "Insira seu nome", trigger: "blur" },
          { min: 3, message: "Insira um nome válido", trigger: "blur" },
        ],
        surname: [
          { required: true, message: "Insira seu sobrenome", trigger: "blur" },
          { min: 3, message: "Insira um sobrenome válido", trigger: "blur" },
        ],
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
        document: [
          {
            required: true,
            validator: validateDocument,
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
        confirmPassword: [
          { validator: validatePass, required: true, trigger: "blur" },
        ],
      },
    };
  },

  methods: {
    async register() {
      const self = this;
      const form = this.$refs.ruleForm;
      self.loading = true;

      let isValid = await form.validate((valid, fields) => {
        if (!valid) {
          self.loading = false;
          console.log(fields);
          ElMessage({
            message: "Preencha todos os campos corretamente.",
            type: "warning",
          });
        }
        return valid;
      });
      if (!isValid) {
        return;
      }

      const headers = { "Content-Type": "application/json" };
      const payload = JSON.stringify({
        login: this.model.email,
        password: this.model.password,
        name: `${this.model.name} ${this.model.surname}`,
        document: this.model.document,
      });
      await fetch("http://localhost:8000/api/user/", {
        method: "POST",
        headers: headers,
        body: payload,
      })
        .then(async (response) => {
          const data = await response.json();
          if (!response.ok) {
            ElMessage.error("Já existe um usuário com esse e-mail!");
            isValid = false;
            return;
          }
          localStorage.setItem("userDTO", JSON.stringify(data));
        })
        .catch(() => {
          isValid = false;
          return;
        });

      if (isValid) {
        this.$router.push({ path: "/inicio" });
      }
    },

    login() {
      this.$router.push({ path: "/" });
    },
  },
};
</script>

<style scoped>
.el-link {
  margin-top: 16px;
}
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
  width: 128px;
  height: 48px;
  font-size: large;
}
</style>
