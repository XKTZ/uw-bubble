<template>
  <el-container>
    <el-header style="padding-top: 6%; padding-bottom: 4%;">
      <div style="font-size: 180%;">
        UWaterloo Bubble!
      </div>
    </el-header>
    <el-main style="margin: auto;">
      <div v-if="mode === Modes.Register">
        <div class="interact-component options" style="font-size: 120%; width: 100%;">
          Start to register a UWBubble account
        </div>
        <el-input class="interact-component options" v-model="email" placeholder="Email"/>
        <el-input class="interact-component options" v-model="password" placeholder="Password" show-password/>
        <el-button class="interact-component options" style="width: 60%;" @click="register" type="primary">
          Register
        </el-button>
        <el-button class="interact-component options" style="width: 60%; margin-left: 0;" @click="toLogin" type="info">
          To Login
        </el-button>
      </div>

      <div v-if="mode === Modes.Info">
        <el-input class="interact-component options" v-model="name" placeholder="Name" size="large"/>
        <el-input class="interact-component options" v-model="username" placeholder="Username" size="large"/>
        <el-input-number class="interact-component options" v-model="age" placeholder="Age" size="large"/>
        <el-select class="interact-component options" v-model="gender" placeholder="Gender" size="large">
          <el-option
              v-for="g in Genders"
              :key="g.id"
              :label="g.name"
              :value="g.id"
          />
        </el-select>
        <el-select class="interact-component options" v-model="faculty" placeholder="Faculty" size="large">
          <el-option
              v-for="f in Faculties"
              :key="f.value"
              :label="f.name"
              :value="f.value"
          />
        </el-select>
        <el-button class="interact-component options" @click="uploadInfo" type="primary"
                   style="width: 60%; height: 45px; font-weight: 600;">
          Submit
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
              :value="i.id"
          />
        </el-select>
        <el-button class="interact-component options" @click="uploadInterest"
                   style="width: 60%; height: 45px; font-weight: 600;">
          Finish
        </el-button>
      </div>

      <div v-if="mode === Modes.Success">
        <div style="width: 100%; font-size: 125%; font-weight: 600;">
          Register Success! Welcome.
        </div>
        <el-button class="interact-component options" @click="toLogin">Go to page</el-button>
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
import {Genders, Faculties, Interests} from "@/utils/UserUtil";
import axios from "axios";

const Modes = {
  Register: 0,
  Info: 1,
  Interests: 2,
  Success: 3
};

export default {
  name: "RegisterView",
  setup() {
    const mode = ref(Modes.Register);

    const email = ref(null);
    const password = ref(null);

    const name = ref(null);
    const username = ref(null);
    const faculty = ref(null);
    const age = ref(null);
    const gender = ref(null);
    const interest = ref([]);

    const register = () => {
      if (email.value === null || password.value === null) {
        ElMessage("Information not fully provided. Please fill all info and try again");
      } else {
        mode.value = Modes.Info;
      }
    };

    const uploadInfo = () => {
      if (faculty.value === null || age.value === null || gender.value === null) {
        ElMessage("Information not fully provided. Please fill all info and try again");
      } else {
        mode.value = Modes.Interests;
      }
    };

    const uploadInterest = async () => {
      if (interest.value.length < 3) {
        ElMessage("Too few interests. There should be 3 of them")
      } else if (interest.value.length > 3) {
        ElMessage("Too much interests. There should be 3 of them")
      } else {
        let interests = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
        for (let id of interest.value) {
          interests[id] = 1;
        }
        try {
          let result = await axios({
            method: 'post',
            url: '/users/signup',
            data: {
              name: name.value,
              username: username.value,
              email: email.value,
              password: password.value,
              faculty: faculty.value,
              gender: gender.value,
              age: age.value,
              interests: interests
            }
          });
          ElMessage(result.data)
          router.push("/login");
        } catch (e) {
          console.log(e)
          ElMessage(e.response.data)

          name.value = null;
          username.value = null;
          email.value = null;
          password.value = null;
          faculty.value = null;
          gender.value = null;
          age.value = null;
          interest.value = [];
          mode.value = Modes.Register;
        }
      }
    };

    const toLogin = () => {
      router.push("/login");
    };

    return {
      email,
      password,

      name,
      username,
      faculty,
      age,
      gender,
      interest,

      register,
      uploadInfo,
      uploadInterest,
      toLogin,

      Genders,
      Faculties,
      Interests,

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
  margin-top: 1%;
  margin-bottom: 1%;
}

</style>