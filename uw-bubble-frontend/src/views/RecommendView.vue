<template>
  <el-container>
    <el-main>
      <el-space>

      </el-space>
    </el-main>
  </el-container>
</template>

<script>

import {ref} from "vue";

const getRecommendation = async () => {
  return [];
};

export default {
  name: "RecommendView",
  async setup() {
    const recommendation = ref(await getRecommendation());
  }
}
</script>

<style scoped>

</style>