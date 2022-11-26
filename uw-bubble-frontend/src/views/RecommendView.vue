<template>
  <el-container>
    <el-header>
      <el-button @click="toCommunication" style="width: 100%; height: 40px; margin-top: 2%; margin-bottom: 2%;">
        To Communication
      </el-button>
    </el-header>
    <el-main>
      <!--<el-space direction="vertical">-->
      <user-card v-for="recom in recommendation"
                 :name="recom.name"
                 :id="recom.id"
                 :faculty="recom.faculty"
                 :gender="recom.gender"
                 :interests="recom.interests"
                 :last-page="0"/>
      <!--</el-space>-->
    </el-main>
  </el-container>
</template>

<script>

import {onMounted, ref} from "vue";
import {assertLogin, needLogin} from "@/utils/UserUtil";
import {ElMessage} from "element-plus";
import router from "@/router";
import UserCard from "@/components/UserCard";
import axios from "axios";

const getRecommendation = async () => {
  return [];
};

export default {
  name: "RecommendView",
  components: {UserCard},
  setup() {
    const recommendation = ref([]);
    onMounted(async () => {
      await assertLogin();
      const jwt = localStorage.getItem("jwt");
      const recommends = (await axios({
        method: 'GET',
        url: '/users/recommend',
        headers: {
          authorization: `Bearer ${jwt}`
        }
      })).data;
      recommends.sort((a, b) => {
        return b.matchRate - a.matchRate;
      });
      recommendation.value = recommends.map(x => x.pair);
    })

    const toCommunication = () => {
      router.push("/communications");
    }

    return {
      recommendation,
      toCommunication
    }
  }
}
</script>

<style scoped>

</style>