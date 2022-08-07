<template>
  <el-scrollbar></el-scrollbar>
</template>

<script>
import { defineComponent } from "vue";
export default defineComponent({
  name: "CartComponent",

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
});
</script>
