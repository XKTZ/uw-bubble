<template>
  <el-container>
    <el-header>
      <el-button @click="toRecommend" style="width: 100%; height: 40px; margin-top: 2%; margin-bottom: 2%;">
        To Recommend
      </el-button>
    </el-header>
    <el-main>
      <!--<el-space direction="vertical">-->
      <user-card v-for="comm in communications"
                 :name="comm.name"
                 :id="comm.id"
                 :faculty="comm.faculty"
                 :gender="comm.gender"
                 :interests="comm.interests"
                 :last-page="1"/>
      <!--</el-space>-->
    </el-main>
  </el-container>
</template>

<script>

import UserCard from "@/components/UserCard";
import axios from "axios";
import {onMounted, ref} from "vue";
import {assertLogin} from "@/utils/UserUtil";
import router from "@/router";

export default {
  name: "CommunicationsView",
  components: {UserCard},
  setup() {
    const communications = ref([]);
    onMounted(async () => {
      await assertLogin();
      const jwt = localStorage.getItem("jwt");
      const communi = (await axios({
        method: 'GET',
        url: '/users/communications',
        headers: {
          authorization: `Bearer ${jwt}`
        }
      })).data;
      communi.sort((a, b) => {
        return b.matchRate - a.matchRate;
      });
      communications.value = communi;
    })

    const toRecommend = () => {
      router.push("/recommend");
    }

    return {
      communications,
      toRecommend
    }
  }
}
</script>

<style scoped>

</style>