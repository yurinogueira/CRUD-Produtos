<!-- eslint-disable prettier/prettier -->
<script>
import { defineComponent } from "vue";

export default defineComponent({ name: "BaseFooter" });
</script>

<template>
  <section>
    <p>
      Projeto feito como parte do processo seletivo da <b>Siteware</b>
      <br />
      <br />
      Copyright © 2022 <b>Yuri Nogueira Moreira da Silva</b>
      <br />
      <br />
      <br />
      <el-link
        href="https://github.com/yurinogueira/CRUD-Produtos"
        target="_blank"
      >
        Acesse o GitHub do projeto, clicando aqui, para saber mais sobre
      </el-link>
    </p>
  </section>
</template>

<style scoped>
section {
  background-color: #f1f1f1;
  padding-top: 64px;
  padding-bottom: 64px;
}
</style>
