<template>
  <el-container>
    <el-header style="padding-top: 6%; padding-bottom: 6%;">
      <div style="font-size: 200%;">
        UWaterloo Bubble!
      </div>
    </el-header>
    <el-main style="margin: auto;">
      <el-input class="interact-component options" v-model="username" placeholder="Username"/>
      <el-input class="interact-component options" v-model="password" placeholder="Password" show-password/>
      <el-button class="interact-component options" style="width: 60%;" @click="login" type="primary">
        Login
      </el-button>
      <el-button class="interact-component options" style="width: 60%; margin-left: 0;" @click="toRegister" type="info">
        To Register
      </el-button>
    </el-main>
    <el-footer>

    </el-footer>
  </el-container>
</template>

<script>
import {ref} from "vue";
import {ElButton, ElContainer, ElInput} from 'element-plus';
import router from "@/router";
import axios from "axios";

const LOGIN_MODE = 0;
const REGISTER_MODE = 1;

export default {
  name: "LoginView",
  components: {},
  setup() {
    const username = ref("");
    const password = ref("");
    const mode = ref(LOGIN_MODE);

    const login = async () => {
      try {
        let result = await axios.post('/users/authenticate', {
          username: username.value,
          password: password.value
        });
        localStorage.setItem('jwt', result.data['jwttoken']);
        await router.push("/recommend");
      } catch (e) {
        console.log(e)
      }
    };

    const toRegister = ref(() => {
      router.push("/register");
    });

    return {
      login,

      mode,
      LOGIN_MODE,
      REGISTER_MODE,
      toRegister,

      username,
      password
    }
  }
}
</script>

<style scoped>
.options {
  width: 60%;
  height: 40px;
}

.interact-component {
  margin-top: 2%;
  margin-bottom: 2%;
}
</style>