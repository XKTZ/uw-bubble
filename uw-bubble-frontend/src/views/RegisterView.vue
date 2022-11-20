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
        <div class="interact-component options" style="font-size: 120%; width: 100%;">
          Choose 3 things that interest you the most
        </div>
        <el-select
          v-model="interest"
          multiple
          placeholder="Your Interest"
          class="interact-component"
          style="width: 100%;"
        >
          <el-option
              v-for="i in Interests"
              :key="i.id"
              :label="i.name"
              :value="i.name"
          />
        </el-select>
        <el-button class="interact-component options" @click="uploadInterest" style="width: 60%; height: 45px; font-weight: 600;">
          Finish
        </el-button>
      </div>

      <div v-if="mode === Modes.Success">
        <div style="font-size: 125%; font-weight: 600;">
          Register Success! Welcome.
        </div>
        <el-button @click="">Go to page</el-button>
      </div>
    </el-main>
    <el-footer>

    </el-footer>
  </el-container>
</template>

<script>
import {ref} from "vue";
import router from "@/router";
import {ElMessage} from "element-plus";

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

const Interests = ref([
  {
    name: "Interest 0",
    id: 0
  },
  {
    name: "Interest 1",
    id: 1
  },
  {
    name: "Interest 2",
    id: 2
  },
  {
    name: "Interest 3",
    id: 3
  },
  {
    name: "Interest 4",
    id: 4
  },
  {
    name: "Interest 5",
    id: 5
  },
  {
    name: "Interest 6",
    id: 6
  },
  {
    name: "Interest 7",
    id: 7
  },
  {
    name: "Interest 8",
    id: 8
  },
  {
    name: "Interest 9",
    id: 9
  },
  {
    name: "Interest 10",
    id: 10
  },
  {
    name: "Interest 11",
    id: 11
  },
  {
    name: "Interest 12",
    id: 12
  },
  {
    name: "Interest 13",
    id: 13
  },
  {
    name: "Interest 14",
    id: 14
  },
  {
    name: "Interest 15",
    id: 15
  },
]);

const Modes = {
  Register: 0,
  Interests: 1,
  Success: 2
}

export default {
  name: "RegisterView",
  setup() {
    const mode = ref(Modes.Interests);

    const email = ref("");
    const password = ref("");
    const faculty = ref(0);
    const interest = ref([]);

    const register = async () => {
      mode.value = Modes.Interests
    };

    const uploadInterest = async () => {
      if (interest.value.length < 3) {
        ElMessage("Too little interests. There should be 3 of them")
      } else if (interest.value.length > 3) {
        ElMessage("Too much interests. There should be 3 of them")
      } else {
        mode.value = 3;
      }
    };

    const toMain = async () => {
      router.push("/main")
    };

    const toLogin = () => {
      router.push("/login");
    };

    return {
      email,
      password,
      faculty,
      interest,

      register,
      uploadInterest,
      toLogin,

      Faculties,
      Interests,

      mode,
      Modes
    }
  }
}
</script>

<style scoped>


.interact-component {
  margin-top: 4%;
  margin-bottom: 4%;
}

</style>