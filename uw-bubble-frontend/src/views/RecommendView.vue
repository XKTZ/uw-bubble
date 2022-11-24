<template>
  <el-container>
    <el-main>
      <el-space>

      </el-space>
    </el-main>
  </el-container>
</template>

<script>

import {onMounted, ref} from "vue";
import {assertLogin, needLogin} from "@/utils/UserUtil";
import {ElMessage} from "element-plus";
import router from "@/router";

const getRecommendation = async () => {
  return [];
};

export default {
  name: "RecommendView",
  setup() {
    const recommendation = ref([]);
    onMounted(async () => {
      // await assertLogin();
    })
    return {

    }
  }
}
</script>

<style scoped>

</style>