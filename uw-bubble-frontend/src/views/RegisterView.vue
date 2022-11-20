<template>
  <el-container>
    <el-header style="padding-top: 6%; padding-bottom: 4%;">
      <div style="font-size: 180%;">
        Welcome to UWaterloo Bubble!
      </div>
    </el-header>
    <el-main style="margin: auto;">
      <div v-if="mode === Modes.Register">
        <el-input class="interact-component options" v-model="email" placeholder="Email"/>
        <el-input class="interact-component options" v-model="password" placeholder="Password" show-password/>
        <el-select class="interact-component options" v-model="faculty" placeholder="Faculty" size="large">
          <el-option
              v-for="f in Faculties"
              :key="f.value"
              :label="f.name"
              :value="f.value"
          />
        </el-select>
        <br>
        <el-button class="interact-component options" style="width: 60%;" @click="register" type="primary">
          Register
        </el-button>
        <el-button class="interact-component options" style="width: 60%; margin-left: 0;" @click="toLogin" type="info">
          To Login
        </el-button>
      </div>

      <div v-if="mode === Modes.Interests">

      </div>
    </el-main>
    <el-footer>

    </el-footer>
  </el-container>
</template>

<script>
import {ref} from "vue";
import router from "@/router";

const Faculties = ref([
  {
    name: "Arts",
    value: 0
  },
  {
    name: "Engineering",
    value: 1
  },
  {
    name: "Environment",
    value: 2
  },
  {
    name: "Health",
    value: 3
  },
  {
    name: "Mathematics",
    value: 4
  },
  {
    name: "Science",
    value: 5
  }
]);

const Modes = {
  Register: 0,
  Interests: 1
}

export default {
  name: "RegisterView",
  setup() {
    const mode = ref(Modes.Register);

    const email = ref("");
    const password = ref("");
    const faculty = ref(0);

    const register = async () => {
      mode.value = Modes.Interests
    };

    const toLogin = () => {
      router.push("/login");
    }

    return {
      email,
      password,
      faculty,

      register,
      toLogin,

      Faculties,

      mode,
      Modes
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